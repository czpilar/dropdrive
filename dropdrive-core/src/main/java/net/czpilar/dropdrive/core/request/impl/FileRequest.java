package net.czpilar.dropdrive.core.request.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWriteMode;
import net.czpilar.dropdrive.core.listener.IFileUploadProgressListener;
import net.czpilar.dropdrive.core.request.IFileRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Template class implementing file request using chunk file upload.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class FileRequest implements IFileRequest {

    private static final Logger LOG = LoggerFactory.getLogger(FileRequest.class);

    public static final int CHUNK_SIZE = 4194304; // 4MB
    public static final int CHUNK_RETRIES = 3;

    public static class Insert extends FileRequest {
        public Insert(DbxClient dbxClient, String remoteFilePath, File localFile) {
            super(dbxClient, remoteFilePath, localFile, DbxWriteMode.add());
        }
    }

    public static class Update extends FileRequest {
        public Update(DbxClient dbxClient, DbxEntry.File remoteFile, File localFile) {
            super(dbxClient, remoteFile.path, localFile, DbxWriteMode.update(remoteFile.rev));
        }
    }

    private final DbxClient dbxClient;
    private final String remoteFilePath;
    private final File localFile;
    private final DbxWriteMode dbxWriteMode;

    private IFileUploadProgressListener progressListener;

    public FileRequest(DbxClient dbxClient, String remoteFilePath, File localFile, DbxWriteMode dbxWriteMode) {
        this.dbxClient = dbxClient;
        this.remoteFilePath = remoteFilePath;
        this.localFile = localFile;
        this.dbxWriteMode = dbxWriteMode;
    }

    public void setProgressListener(IFileUploadProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public DbxEntry.File execute() throws IOException, DbxException {
        int offsetBytes = 0;

        progress(IFileUploadProgressListener.State.INITIATION, offsetBytes);

        FileInputStream stream = new FileInputStream(localFile);

        byte[] data = new byte[CHUNK_SIZE];
        try {
            String chunkId = null;
            int readBytes;
            while ((readBytes = stream.read(data)) != -1) {
                chunkId = uploadChunkWithRetries(offsetBytes, data, chunkId, readBytes);
                offsetBytes += readBytes;
                progress(IFileUploadProgressListener.State.IN_PROGRESS, offsetBytes);
            }
            DbxEntry.File file = dbxClient.chunkedUploadFinish(remoteFilePath, dbxWriteMode, chunkId);
            progress(IFileUploadProgressListener.State.COMPLETE, offsetBytes);
            return file;
        } finally {
            stream.close();
        }
    }

    private String uploadChunkWithRetries(int offsetBytes, byte[] data, String chunkId, int readBytes) throws DbxException {
        int retry = 0;
        while (true) {
            try {
                return uploadChunk(offsetBytes, data, chunkId, readBytes);
            } catch (DbxException e) {
                retry++;
                if (retry > CHUNK_RETRIES) {
                    throw e;
                }
                LOG.warn("Error during executing uploading chunk file, offset bytes {}, retrying for {} time(s), message: {}", offsetBytes, retry, e.getMessage());
            }
        }
    }

    private String uploadChunk(int offset, byte[] data, String chunkId, int read) throws DbxException {
        if (offset == 0) {
            progress(IFileUploadProgressListener.State.IN_PROGRESS, offset);
            chunkId = dbxClient.chunkedUploadFirst(data, 0, read);
        } else {
            dbxClient.chunkedUploadAppend(chunkId, offset, data, 0, read);
        }
        return chunkId;
    }

    private void progress(IFileUploadProgressListener.State state, long uploaded) {
        if (progressListener != null) {
            progressListener.progressChanged(state, uploaded);
        }
    }
}

