package net.czpilar.dropdrive.core.service.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import net.czpilar.dropdrive.core.exception.FileHandleException;
import net.czpilar.dropdrive.core.listener.impl.FileUploadProgressListener;
import net.czpilar.dropdrive.core.request.FileRequest;
import net.czpilar.dropdrive.core.service.IDirectoryService;
import net.czpilar.dropdrive.core.service.IFileService;
import net.czpilar.dropdrive.core.util.EqualUtils;
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
        FileRequest request = FileRequest.createInsert(getDbxClient(), getPath(filename, parentDir), pathToFile.toFile());
        request.setProgressListener(new FileUploadProgressListener(filename, pathToFile.toFile().length()));
        return execute(request);
    }

    private DbxEntry.File updateFile(DbxEntry.File currentFile, Path pathToFile) throws Exception {
        String filename = pathToFile.getFileName().toString();
        LOG.info("Uploading updated file {}", filename);
        FileRequest request = FileRequest.createUpdate(getDbxClient(), currentFile, pathToFile.toFile());
        request.setProgressListener(new FileUploadProgressListener(filename, pathToFile.toFile().length()));
        return execute(request);
    }

    private DbxEntry.File execute(FileRequest request) throws Exception {
        int retry = 0;
        while (true) {
            try {
                return request.execute();
            } catch (DbxException e) {
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
            } else if (EqualUtils.notEquals(currentFile, pathToFile)) {
                currentFile = updateFile(currentFile, pathToFile);
            } else {
                LOG.info("There is nothing to upload.");
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
