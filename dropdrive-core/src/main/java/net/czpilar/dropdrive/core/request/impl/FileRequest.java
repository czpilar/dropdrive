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

/**
 * Template class implementing file request using chunk file upload.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class FileRequest implements IFileRequest {

    public static final int CHUNK_SIZE = 4194304; // 4MB

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
        int offset = 0;

        progress(IFileUploadProgressListener.State.INITIATION, offset);

        FileInputStream stream = new FileInputStream(localFile);

        byte[] data = new byte[CHUNK_SIZE];
        try {
            String chunkId = null;
            int read;
            while ((read = stream.read(data)) != -1) {
                if (offset == 0) {
                    progress(IFileUploadProgressListener.State.IN_PROGRESS, offset);
                    chunkId = dbxClient.chunkedUploadFirst(data, 0, read);
                } else {
                    dbxClient.chunkedUploadAppend(chunkId, offset, data, 0, read);
                }
                offset += read;
                progress(IFileUploadProgressListener.State.IN_PROGRESS, offset);
            }
            DbxEntry.File file = dbxClient.chunkedUploadFinish(remoteFilePath, dbxWriteMode, chunkId);
            progress(IFileUploadProgressListener.State.COMPLETE, offset);
            return file;
        } finally {
            stream.close();
        }
    }

    private void progress(IFileUploadProgressListener.State state, long uploaded) {
        if (progressListener != null) {
            progressListener.progressChanged(state, uploaded);
        }
    }
}

