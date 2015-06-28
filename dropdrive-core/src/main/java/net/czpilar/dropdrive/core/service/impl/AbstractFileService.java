package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import net.czpilar.dropdrive.core.exception.FileHandleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * Base service for file and directory common functions.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractFileService extends AbstractService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractFileService.class);

    protected String getPath(String filename, DbxEntry parent) {
        Assert.notNull(filename);

        StringBuilder path = new StringBuilder();
        if (parent != null) {
            path.append(parent.path);
        }
        path.append("/").append(filename);

        return path.toString();
    }

    protected DbxEntry.Folder findFolder(String filename, DbxEntry.Folder parent) {
        DbxEntry entry = findEntry(filename, parent);
        return entry != null && entry.isFolder() ? (DbxEntry.Folder) entry : null;
    }

    protected DbxEntry.File findFile(String filename, DbxEntry.Folder parent) {
        DbxEntry entry = findEntry(filename, parent);
        return entry != null && entry.isFile() ? (DbxEntry.File) entry : null;
    }

    private DbxEntry findEntry(String filename, DbxEntry.Folder parent) {
        DbxEntry file;
        try {
            file = getDbxClient().getMetadata(getPath(filename, parent));
        } catch (DbxException e) {
            LOG.error("Unable to find {}.", filename);
            throw new FileHandleException("Unable to find file.", e);
        }
        return file;
    }

}
