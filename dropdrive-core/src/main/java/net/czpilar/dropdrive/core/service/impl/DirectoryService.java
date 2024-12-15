package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.FolderMetadata;
import net.czpilar.dropdrive.core.exception.DirectoryHandleException;
import net.czpilar.dropdrive.core.service.IDirectoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service with methods for handling directories in Dropbox.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Service
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

    protected FolderMetadata createOneDirectory(String dirname, FolderMetadata parentDir) {
        String path = getPath(dirname, parentDir);
        try {
            return getDbxClient().files().createFolderV2(path).getMetadata();
        } catch (DbxException e) {
            LOG.error("Unable to create directory {}.", dirname);
            throw new DirectoryHandleException("Unable to create directory.", e);
        }
    }

    protected FolderMetadata findOrCreateOneDirectory(String dirname, FolderMetadata parentDir) {
        FolderMetadata dir = findFolder(dirname, parentDir);
        if (dir == null) {
            dir = createOneDirectory(dirname, parentDir);
        }
        return dir;
    }

    @Override
    public FolderMetadata findDirectory(String pathname) {
        return findDirectory(pathname, null);
    }

    @Override
    public FolderMetadata findDirectory(String pathname, FolderMetadata parentDir) {
        pathname = normalizePathname(pathname);
        String dirname = getCurrentDirname(pathname);
        FolderMetadata currentDir = parentDir;
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
    public FolderMetadata findOrCreateDirectory(String pathname) {
        return findOrCreateDirectory(pathname, null);
    }

    @Override
    public FolderMetadata findOrCreateDirectory(String pathname, FolderMetadata parentDir) {
        pathname = normalizePathname(pathname);
        String dirname = getCurrentDirname(pathname);
        FolderMetadata currentDir = parentDir;
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
