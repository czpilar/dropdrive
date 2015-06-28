package net.czpilar.dropdrive.core.request;

import java.io.IOException;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;

/**
 * Interface for file request.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IFileRequest {

    /**
     * Execute file request and return file.
     *
     * @return uploaded file
     * @throws IOException
     * @throws DbxException
     */
    DbxEntry.File execute() throws IOException, DbxException;
}
