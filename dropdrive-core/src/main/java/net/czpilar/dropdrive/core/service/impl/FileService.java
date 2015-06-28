package net.czpilar.dropdrive.core.service.impl;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxWriteMode;
import net.czpilar.dropdrive.core.exception.FileHandleException;
import net.czpilar.dropdrive.core.service.IDirectoryService;
import net.czpilar.dropdrive.core.service.IFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service with methods for handling files in Dropbox.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class FileService extends AbstractFileService implements IFileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private final int retries;

    private IDirectoryService directoryService;

    public FileService(int retries) {
        this.retries = retries;
    }

    public int getRetries() {
        return retries;
    }

    @Autowired
    public void setDirectoryService(IDirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    protected IDirectoryService getDirectoryService() {
        return directoryService;
    }

    protected String getUploadDir(String uploadDirname) {
        if (uploadDirname == null) {
            uploadDirname = getDropDriveCredential().getUploadDir();
        }
        return uploadDirname;
    }

    @Override
    public DbxEntry.File uploadFile(String filename, String pathname) {
        DbxEntry.Folder parentDir = getDirectoryService().findOrCreateDirectory(getUploadDir(pathname));
        return uploadFile(filename, parentDir);
    }

    private DbxEntry.File insertFile(Path pathToFile, DbxEntry.Folder parentDir) throws Exception {
        String filename = pathToFile.getFileName().toString();
        LOG.info("Uploading new file {}", filename);
        return execute(getPath(filename, parentDir), DbxWriteMode.add(), pathToFile.toFile().length(), new FileInputStream(pathToFile.toFile()));
    }

    private DbxEntry.File updateFile(DbxEntry.File currentFile, Path pathToFile) throws Exception {
        String filename = pathToFile.getFileName().toString();
        LOG.info("Uploading updated file {}", filename);
        return execute(currentFile.path, DbxWriteMode.update(currentFile.rev), pathToFile.toFile().length(), new FileInputStream(pathToFile.toFile()));
    }

    private DbxEntry.File execute(String filename, DbxWriteMode mode, long length, FileInputStream stream) throws Exception {
        int retry = 0;
        while (true) {
            try {
                return getDbxClient().uploadFile(filename, mode, length, stream);
            } catch (Exception e) {
                retry++;
                if (retry > getRetries()) {
                    throw e;
                }
                LOG.warn("Error during executing uploading file, retrying for {} time(s), message: {}", retry, e.getMessage());
            }
        }
    }

    @Override
    public DbxEntry.File uploadFile(String filename, DbxEntry.Folder parentDir) {
        try {
            Path pathToFile = Paths.get(filename);
            DbxEntry.File currentFile = findFile(pathToFile.getFileName().toString(), parentDir);

            if (currentFile == null) {
                currentFile = insertFile(pathToFile, parentDir);
            } else {
                currentFile = updateFile(currentFile, pathToFile);
            }
            LOG.info("Finished uploading file {} - remote revision is {}", filename, currentFile.rev);
            return currentFile;
        } catch (Exception e) {
            LOG.error("Unable to upload file {}.", filename);
            throw new FileHandleException("Unable to upload file.", e);
        }
    }

    @Override
    public DbxEntry.File uploadFile(String filename) {
        return uploadFile(filename, (DbxEntry.Folder) null);
    }

    @Override
    public List<DbxEntry.File> uploadFiles(List<String> filenames) {
        return uploadFiles(filenames, (DbxEntry.Folder) null);
    }

    @Override
    public List<DbxEntry.File> uploadFiles(List<String> filenames, String pathname) {
        DbxEntry.Folder parentDir = getDirectoryService().findOrCreateDirectory(getUploadDir(pathname));
        return uploadFiles(filenames, parentDir);
    }

    @Override
    public List<DbxEntry.File> uploadFiles(List<String> filenames, DbxEntry.Folder parentDir) {
        List<DbxEntry.File> files = new ArrayList<DbxEntry.File>();
        if (filenames != null) {
            for (String filename : filenames) {
                try {
                    files.add(uploadFile(filename, parentDir));
                } catch (FileHandleException e) {
                    LOG.error("Error during uploading file.", e);
                }
            }
        }
        return files;
    }
}
