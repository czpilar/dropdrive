package net.czpilar.dropdrive.core.request;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import net.czpilar.dropdrive.core.exception.DropDriveException;
import net.czpilar.dropdrive.core.listener.IFileUploadProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * File request implementation for uploading file using chunk file upload.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class FileRequest {

    private static final Logger LOG = LoggerFactory.getLogger(FileRequest.class);

    public static final int CHUNK_SIZE = 4194304; // 4MB
    public static final int CHUNK_RETRIES = 5;

    public static FileRequest createInsert(DbxClientV2 dbxClient, String remoteFilePath, File localFile) {
        return new FileRequest(dbxClient, remoteFilePath, localFile, WriteMode.ADD);
    }

    public static FileRequest createUpdate(DbxClientV2 dbxClient, FileMetadata remoteFile, File localFile) {
        return new FileRequest(dbxClient, remoteFile.getPathDisplay(), localFile, WriteMode.update(remoteFile.getRev()));
    }

    private final DbxClientV2 dbxClient;
    private final String remoteFilePath;
    private final File localFile;
    private final WriteMode writeMode;

    private IFileUploadProgressListener progressListener;

    private FileRequest(DbxClientV2 dbxClient, String remoteFilePath, File localFile, WriteMode writeMode) {
        this.dbxClient = dbxClient;
        this.remoteFilePath = remoteFilePath;
        this.localFile = localFile;
        this.writeMode = writeMode;
    }

    public void setProgressListener(IFileUploadProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    public FileMetadata execute() throws IOException, DbxException {
        long offsetBytes = 0;

        progress(IFileUploadProgressListener.State.INITIATION, offsetBytes);

        try (FileInputStream stream = new FileInputStream(localFile)) {
            long size = localFile.length();
            String chunkId = null;

            while (offsetBytes < size) {
                long readBytes = size - offsetBytes;
                if (readBytes > CHUNK_SIZE) {
                    readBytes = CHUNK_SIZE;
                }
                chunkId = uploadChunkWithRetries(offsetBytes, stream, chunkId, readBytes);
                offsetBytes += readBytes;
                progress(IFileUploadProgressListener.State.IN_PROGRESS, offsetBytes);
            }

            if (chunkId == null) {
                throw new DropDriveException("No chunk found");
            }

            UploadSessionCursor cursor = new UploadSessionCursor(chunkId, offsetBytes);
            CommitInfo commitInfo = CommitInfo.newBuilder(remoteFilePath)
                    .withMode(writeMode)
                    .withClientModified(new Date(localFile.lastModified()))
                    .build();
            try (UploadSessionFinishUploader uploadSessionFinishUploader = dbxClient.files().uploadSessionFinish(cursor, commitInfo)) {
                FileMetadata file = uploadSessionFinishUploader.finish();
                progress(IFileUploadProgressListener.State.COMPLETE, offsetBytes);
                return file;
            }
        }
    }

    private String uploadChunkWithRetries(long offsetBytes, InputStream stream, String chunkId, long readBytes) throws DbxException, IOException {
        int retry = 0;
        while (true) {
            try {
                return uploadChunk(offsetBytes, stream, chunkId, readBytes);
            } catch (DbxException e) {
                retry++;
                if (retry > CHUNK_RETRIES) {
                    throw e;
                }
                LOG.warn("Error during executing uploading chunk file, offset bytes {}, retrying for {} time(s), message: {}", offsetBytes, retry, e.getMessage());
            }
        }
    }

    private String uploadChunk(long offset, InputStream stream, String chunkId, long read) throws DbxException, IOException {
        if (offset == 0) {
            progress(IFileUploadProgressListener.State.IN_PROGRESS, offset);
            try (UploadSessionStartUploader uploadSessionStartUploader = dbxClient.files().uploadSessionStart()) {
                return uploadSessionStartUploader.uploadAndFinish(stream, read).getSessionId();
            }
        } else {
            try (UploadSessionAppendV2Uploader uploadSessionAppendUploader = dbxClient.files().uploadSessionAppendV2(new UploadSessionCursor(chunkId, offset))) {
                uploadSessionAppendUploader.uploadAndFinish(stream, read);
            }
        }
        return chunkId;
    }

    private void progress(IFileUploadProgressListener.State state, long uploaded) {
        if (progressListener != null) {
            progressListener.progressChanged(state, uploaded);
        }
    }
}

