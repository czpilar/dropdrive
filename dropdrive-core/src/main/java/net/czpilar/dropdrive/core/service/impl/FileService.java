package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import net.czpilar.dropdrive.core.exception.FileHandleException;
import net.czpilar.dropdrive.core.listener.impl.FileUploadProgressListener;
import net.czpilar.dropdrive.core.request.FileRequest;
import net.czpilar.dropdrive.core.service.IDirectoryService;
import net.czpilar.dropdrive.core.service.IFileService;
import net.czpilar.dropdrive.core.util.EqualUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Service with methods for handling files in Dropbox.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Service
public class FileService extends AbstractFileService implements IFileService {

    private static final Logger LOG = LoggerFactory.getLogger(FileService.class);

    private final int retries;

    private IDirectoryService directoryService;

    public FileService(@Value("${dropdrive.file.upload.retries}") int retries) {
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
    public FileMetadata uploadFile(String filename, String pathname) {
        FolderMetadata parentDir = getDirectoryService().findOrCreateDirectory(getUploadDir(pathname));
        return uploadFile(filename, parentDir);
    }

    private FileMetadata insertFile(Path pathToFile, FolderMetadata parentDir) throws Exception {
        String filename = pathToFile.getFileName().toString();
        LOG.info("Uploading new file {}", filename);
        FileRequest request = FileRequest.createInsert(getDbxClient(), getPath(filename, parentDir), pathToFile.toFile());
        request.setProgressListener(new FileUploadProgressListener(filename, pathToFile.toFile().length()));
        return execute(request);
    }

    private FileMetadata updateFile(FileMetadata currentFile, Path pathToFile) throws Exception {
        String filename = pathToFile.getFileName().toString();
        LOG.info("Uploading updated file {}", filename);
        FileRequest request = FileRequest.createUpdate(getDbxClient(), currentFile, pathToFile.toFile());
        request.setProgressListener(new FileUploadProgressListener(filename, pathToFile.toFile().length()));
        return execute(request);
    }

    private FileMetadata execute(FileRequest request) throws Exception {
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
    public FileMetadata uploadFile(String filename, FolderMetadata parentDir) {
        try {
            Path pathToFile = Paths.get(filename);
            FileMetadata currentFile = findFile(pathToFile.getFileName().toString(), parentDir);

            if (currentFile == null) {
                currentFile = insertFile(pathToFile, parentDir);
            } else if (EqualUtils.notEquals(currentFile, pathToFile)) {
                currentFile = updateFile(currentFile, pathToFile);
            } else {
                LOG.info("There is nothing to upload.");
            }

            LOG.info("Finished uploading file {} - remote revision is {}", filename, currentFile.getRev());
            return currentFile;
        } catch (Exception e) {
            LOG.error("Unable to upload file {}.", filename);
            throw new FileHandleException("Unable to upload file.", e);
        }
    }

    @Override
    public FileMetadata uploadFile(String filename) {
        return uploadFile(filename, (FolderMetadata) null);
    }

    @Override
    public List<FileMetadata> uploadFiles(List<String> filenames) {
        return uploadFiles(filenames, (FolderMetadata) null);
    }

    @Override
    public List<FileMetadata> uploadFiles(List<String> filenames, String pathname) {
        FolderMetadata parentDir = getDirectoryService().findOrCreateDirectory(getUploadDir(pathname));
        return uploadFiles(filenames, parentDir);
    }

    @Override
    public List<FileMetadata> uploadFiles(List<String> filenames, FolderMetadata parentDir) {
        List<FileMetadata> files = new ArrayList<>();
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
