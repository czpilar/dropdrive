package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.*;
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

    protected String getPath(String filename, Metadata parent) {
        Assert.notNull(filename, "Filename must not be null.");

        StringBuilder path = new StringBuilder();
        if (parent != null) {
            path.append(parent.getPathDisplay());
        }
        if (!filename.startsWith("/")) {
            path.append("/");
        }
        path.append(filename);

        return path.toString();
    }

    protected FolderMetadata findFolder(String filename, FolderMetadata parent) {
        Metadata entry = findEntry(filename, parent);
        return entry instanceof FolderMetadata ? (FolderMetadata) entry : null;
    }

    protected FileMetadata findFile(String filename, FolderMetadata parent) {
        Metadata entry = findEntry(filename, parent);
        return entry instanceof FileMetadata ? (FileMetadata) entry : null;
    }

    private Metadata findEntry(String filename, FolderMetadata parent) {
        Metadata entry;
        try {
            entry = getDbxClient().files().getMetadata(getPath(filename, parent));
        } catch (GetMetadataErrorException e) {
            if (e.errorValue.getPathValue().tag() == LookupError.Tag.NOT_FOUND) {
                entry = null;
            } else {
                LOG.error("Unable to find {}.", filename);
                throw new FileHandleException("Unable to find file.", e);
            }
        } catch (DbxException e) {
            LOG.error("Unable to find {}.", filename);
            throw new FileHandleException("Unable to find file.", e);
        }
        return entry;
    }

}
