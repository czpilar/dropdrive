package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import net.czpilar.dropdrive.core.exception.DirectoryHandleException;
import net.czpilar.dropdrive.core.service.IDirectoryService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service with methods for handling directories in Dropbox.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DirectoryService extends AbstractFileService implements IDirectoryService {

    private static final String DIRECTORY_SEPARATOR = "/";

    private static final Logger LOG = LoggerFactory.getLogger(DirectoryService.class);

    private static String normalizePathname(String pathname) {
        return StringUtils.replace(pathname, "\\", DIRECTORY_SEPARATOR);
    }

    private static String getCurrentDirname(String pathname) {
        return StringUtils.trimToNull(StringUtils.substringBefore(pathname, DIRECTORY_SEPARATOR));
    }

    private static String getNextPathname(String pathname) {
        return StringUtils.trimToNull(StringUtils.substringAfter(pathname, DIRECTORY_SEPARATOR));
    }

    protected DbxEntry.Folder createOneDirectory(String dirname, DbxEntry.Folder parentDir) {
        String path = getPath(dirname, parentDir);
        try {
            return getDbxClient().createFolder(path);
        } catch (DbxException e) {
            LOG.error("Unable to create directory {}.", dirname);
            throw new DirectoryHandleException("Unable to create directory.", e);
        }
    }

    protected DbxEntry.Folder findOrCreateOneDirectory(String dirname, DbxEntry.Folder parentDir) {
        DbxEntry.Folder dir = findFolder(dirname, parentDir);
        if (dir == null) {
            dir = createOneDirectory(dirname, parentDir);
        }
        return dir;
    }

    @Override
    public DbxEntry.Folder findDirectory(String pathname) {
        return findDirectory(pathname, null);
    }

    @Override
    public DbxEntry.Folder findDirectory(String pathname, DbxEntry.Folder parentDir) {
        pathname = normalizePathname(pathname);
        String dirname = getCurrentDirname(pathname);
        DbxEntry.Folder currentDir = parentDir;
        if (dirname != null) {
            currentDir = findFolder(dirname, parentDir);
        }
        String nextPathname = getNextPathname(pathname);
        if (currentDir != null && nextPathname != null) {
            currentDir = findDirectory(nextPathname, currentDir);
        }
        return currentDir;
    }

    @Override
    public DbxEntry.Folder findOrCreateDirectory(String pathname) {
        return findOrCreateDirectory(pathname, null);
    }

    @Override
    public DbxEntry.Folder findOrCreateDirectory(String pathname, DbxEntry.Folder parentDir) {
        pathname = normalizePathname(pathname);
        String dirname = getCurrentDirname(pathname);
        DbxEntry.Folder currentDir = parentDir;
        if (dirname != null) {
            currentDir = findOrCreateOneDirectory(dirname, parentDir);
        }
        String nextPathname = getNextPathname(pathname);
        if (nextPathname != null) {
            currentDir = findOrCreateDirectory(nextPathname, currentDir);
        }
        return currentDir;
    }
}
