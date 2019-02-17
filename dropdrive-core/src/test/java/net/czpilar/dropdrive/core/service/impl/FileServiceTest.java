package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import net.czpilar.dropdrive.core.exception.FileHandleException;
import net.czpilar.dropdrive.core.listener.IFileUploadProgressListener;
import net.czpilar.dropdrive.core.request.FileRequest;
import net.czpilar.dropdrive.core.service.IDirectoryService;
import net.czpilar.dropdrive.core.util.EqualUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.context.ApplicationContext;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({DbxClientV2.class, DbxUserFilesRequests.class, EqualUtils.class, FileRequest.class})
public class FileServiceTest {

    private FileService service = new FileService(3);

    @Mock
    private FileService serviceMock;

    @Mock
    private IDirectoryService directoryService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DbxClientV2 dbxClient;

    @Mock
    private IDropDriveCredential dropDriveCredential;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        service.setApplicationContext(applicationContext);
        service.setDropDriveCredential(dropDriveCredential);
        service.setDirectoryService(directoryService);

        when(serviceMock.getDirectoryService()).thenReturn(directoryService);
        when(applicationContext.getBean(DbxClientV2.class)).thenReturn(dbxClient);

        PowerMockito.mockStatic(EqualUtils.class);
        PowerMockito.mockStatic(FileRequest.class);
    }

    @Test
    public void testGetDirectoryService() {
        IDirectoryService result = service.getDirectoryService();

        assertNotNull(result);
        assertEquals(directoryService, result);
    }

    @Test
    public void testGetUploadDir() {
        String uploadDirName = "test-upload-dir";

        String result = service.getUploadDir(uploadDirName);

        assertNotNull(result);
        assertEquals(uploadDirName, result);

        verifyZeroInteractions(dropDriveCredential);
    }

    @Test
    public void testGetUploadDirWithNullUploadDir() {
        String uploadDirName = "test-default-upload-dir";

        when(dropDriveCredential.getUploadDir()).thenReturn(uploadDirName);

        String result = service.getUploadDir(null);

        assertNotNull(result);
        assertEquals(uploadDirName, result);

        verify(dropDriveCredential).getUploadDir();

        verifyNoMoreInteractions(dropDriveCredential);
    }

    @Test
    public void testUploadFileWithStringFilenameAndNullParent() throws IOException, DbxException {
        String filename = "test-filename";
        FolderMetadata parentDir = null;
        FileMetadata file = mock(FileMetadata.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(Metadata.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(FolderMetadata.class))).thenReturn(null);
        when(FileRequest.createInsert(any(DbxClientV2.class), anyString(), any(File.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(file);

        FileMetadata result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).getPath(filename, parentDir);
        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(insert).execute();
        verify(insert).setProgressListener(any(IFileUploadProgressListener.class));
        verify(file).getRev();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(file);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParent() throws IOException, DbxException {
        String filename = "test-filename";
        FolderMetadata parentDir = mock(FolderMetadata.class);
        FileMetadata file = mock(FileMetadata.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(Metadata.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(FolderMetadata.class))).thenReturn(null);
        when(FileRequest.createInsert(any(DbxClientV2.class), anyString(), any(File.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(file);

        FileMetadata result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).getPath(filename, parentDir);
        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(insert).execute();
        verify(insert).setProgressListener(any(IFileUploadProgressListener.class));
        verify(file).getRev();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test(expected = FileHandleException.class)
    public void testUploadFileWithStringFilenameAndNullParentAndThrownException() throws IOException, DbxException {
        String filename = "test-filename";
        FolderMetadata parentDir = null;
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(Metadata.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(FolderMetadata.class))).thenReturn(null);
        when(FileRequest.createInsert(any(DbxClientV2.class), anyString(), any(File.class))).thenReturn(insert);
        when(insert.execute()).thenThrow(DbxException.class);

        try {
            serviceMock.uploadFile(filename, parentDir);
        } catch (FileHandleException e) {
            verify(serviceMock).getPath(filename, parentDir);
            verify(serviceMock).uploadFile(filename, parentDir);
            verify(serviceMock).getDbxClient();
            verify(serviceMock).findFile(filename, parentDir);
            verify(serviceMock).getRetries();
            verify(insert).execute();
            verify(insert).setProgressListener(any(IFileUploadProgressListener.class));

            verifyNoMoreInteractions(serviceMock);
            verifyNoMoreInteractions(dbxClient);
            verifyNoMoreInteractions(insert);

            throw e;
        }
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentWhereUpdateOnlyContent() throws IOException, DbxException {
        String filename = "test-filename";
        FolderMetadata parentDir = mock(FolderMetadata.class);
        FileMetadata file = mock(FileMetadata.class);
        FileRequest update = mock(FileRequest.class);

        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(FolderMetadata.class))).thenReturn(file);
        when(EqualUtils.notEquals(any(FileMetadata.class), any(Path.class))).thenReturn(true);
        when(FileRequest.createUpdate(any(DbxClientV2.class), any(FileMetadata.class), any(File.class))).thenReturn(update);
        when(update.execute()).thenReturn(file);

        FileMetadata result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(update).execute();
        verify(update).setProgressListener(any(IFileUploadProgressListener.class));
        verify(file).getRev();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(update);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentWhereNothingToUpdate() {
        String filename = "test-filename";
        FolderMetadata parentDir = mock(FolderMetadata.class);
        FileMetadata file = mock(FileMetadata.class);

        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(FolderMetadata.class))).thenReturn(file);
        when(EqualUtils.notEquals(any(FileMetadata.class), any(Path.class))).thenReturn(false);

        FileMetadata result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).findFile(filename, parentDir);
        verify(file).getRev();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentAndRetryApplied() throws IOException, DbxException {
        String filename = "test-filename";
        FolderMetadata parentDir = mock(FolderMetadata.class);
        final FileMetadata file = mock(FileMetadata.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(Metadata.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(FolderMetadata.class))).thenReturn(null);
        when(serviceMock.getRetries()).thenReturn(3);
        when(FileRequest.createInsert(any(DbxClientV2.class), anyString(), any(File.class))).thenReturn(insert);

        Answer<FileMetadata> answer = new Answer<FileMetadata>() {
            private int retry = 0;

            @Override
            public FileMetadata answer(InvocationOnMock invocation) throws Throwable {
                if (retry == 0) {
                    retry++;
                    throw mock(DbxException.class);
                }
                return file;
            }
        };
        when(insert.execute()).thenAnswer(answer);

        FileMetadata result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).getPath(filename, parentDir);
        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(serviceMock).getRetries();
        verify(insert, times(2)).execute();
        verify(insert).setProgressListener(any(IFileUploadProgressListener.class));
        verify(file).getRev();

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test(expected = FileHandleException.class)
    public void testUploadFileWithStringFilenameAndFileParentAndRetryExceeded() throws IOException, DbxException {
        String filename = "test-filename";
        FolderMetadata parentDir = mock(FolderMetadata.class);
        final FileMetadata file = mock(FileMetadata.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(Metadata.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(FolderMetadata.class))).thenReturn(null);
        when(serviceMock.getRetries()).thenReturn(3);
        when(FileRequest.createInsert(any(DbxClientV2.class), anyString(), any(File.class))).thenReturn(insert);
        when(insert.execute()).thenThrow(DbxException.class);

        try {
            serviceMock.uploadFile(filename, parentDir);
        } catch (FileHandleException e) {

            assertTrue(e.getCause() instanceof DbxException);

            verify(serviceMock).getPath(filename, parentDir);
            verify(serviceMock).uploadFile(filename, parentDir);
            verify(serviceMock).getDbxClient();
            verify(serviceMock).findFile(filename, parentDir);
            verify(serviceMock, times(4)).getRetries();
            verify(insert, times(4)).execute();
            verify(insert).setProgressListener(any(IFileUploadProgressListener.class));

            verifyNoMoreInteractions(serviceMock);
            verifyNoMoreInteractions(dbxClient);
            verifyNoMoreInteractions(insert);
            verifyNoMoreInteractions(file);
            verifyNoMoreInteractions(parentDir);

            throw e;
        }
    }

    @Test
    public void testUploadFilesWithNullFilenames() {
        FolderMetadata parent = mock(FolderMetadata.class);

        List<FileMetadata> result = service.uploadFiles(null, parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUploadFilesWithEmptyListOfFilenames() {
        FolderMetadata parent = mock(FolderMetadata.class);

        List<FileMetadata> result = service.uploadFiles(new ArrayList<>(), parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUploadFilesWhereUploadFileThrowsException() {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(serviceMock.uploadFiles(anyListOf(String.class), any(FolderMetadata.class))).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenThrow(FileHandleException.class);

        List<FileMetadata> result = serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(serviceMock).uploadFiles(anyListOf(String.class), any(FolderMetadata.class));
        verify(serviceMock).uploadFile("filename1", parent);
        verify(serviceMock).uploadFile("filename2", parent);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testUploadFilesWithListOfFilenames() {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(serviceMock.uploadFiles(anyListOf(String.class), any(FolderMetadata.class))).thenCallRealMethod();
        FileMetadata file1 = mock(FileMetadata.class);
        FileMetadata file2 = mock(FileMetadata.class);
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenReturn(file1, file2);

        List<FileMetadata> result = serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(file1, result.get(0));
        assertEquals(file2, result.get(1));

        verify(serviceMock).uploadFiles(anyListOf(String.class), any(FolderMetadata.class));
        verify(serviceMock).uploadFile("filename1", parent);
        verify(serviceMock).uploadFile("filename2", parent);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testUploadFileWithStringFilenameAndStringParentDir() {
        String pathname = "test-parent-dir";
        String filename = "test-filename";
        FileMetadata file = mock(FileMetadata.class);
        FolderMetadata parent = mock(FolderMetadata.class);

        when(serviceMock.uploadFile(anyString(), anyString())).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenReturn(file);
        when(serviceMock.getUploadDir(anyString())).thenReturn(pathname);
        when(directoryService.findOrCreateDirectory(anyString())).thenReturn(parent);

        FileMetadata result = serviceMock.uploadFile(filename, pathname);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, pathname);
        verify(serviceMock).uploadFile(filename, parent);
        verify(serviceMock).getUploadDir(pathname);
        verify(serviceMock).getDirectoryService();
        verify(directoryService).findOrCreateDirectory(anyString());

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(directoryService);
        verifyZeroInteractions(file);
        verifyZeroInteractions(parent);
    }

    @Test
    public void testUploadFileWithStringFilename() {
        String filename = "test-filename";
        FileMetadata file = mock(FileMetadata.class);

        when(serviceMock.uploadFile(anyString())).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(FolderMetadata.class))).thenReturn(file);

        FileMetadata result = serviceMock.uploadFile(filename);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename);
        verify(serviceMock).uploadFile(filename, (FolderMetadata) null);

        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(file);
    }

    @Test
    public void testUploadFilesWithStringFilenames() {
        List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
        FileMetadata file1 = mock(FileMetadata.class);
        FileMetadata file2 = mock(FileMetadata.class);
        List<FileMetadata> files = Arrays.asList(file1, file2);

        when(serviceMock.uploadFiles(anyListOf(String.class))).thenCallRealMethod();
        when(serviceMock.uploadFiles(anyListOf(String.class), any(FolderMetadata.class))).thenReturn(files);

        List<FileMetadata> result = serviceMock.uploadFiles(filenames);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(files, result);

        verify(serviceMock).uploadFiles(filenames);
        verify(serviceMock).uploadFiles(filenames, (FolderMetadata) null);

        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(file1);
        verifyZeroInteractions(file2);
    }

    @Test
    public void testUploadFilesWithStringFilenamesAndStringParentDir() {
        String parentDir = "test-parent-dir";
        List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
        FileMetadata file1 = mock(FileMetadata.class);
        FileMetadata file2 = mock(FileMetadata.class);
        List<FileMetadata> files = Arrays.asList(file1, file2);
        FolderMetadata parent = mock(FolderMetadata.class);

        when(serviceMock.uploadFiles(anyListOf(String.class), anyString())).thenCallRealMethod();
        when(serviceMock.uploadFiles(anyListOf(String.class), any(FolderMetadata.class))).thenReturn(files);
        when(serviceMock.getUploadDir(anyString())).thenReturn(parentDir);
        when(directoryService.findOrCreateDirectory(anyString())).thenReturn(parent);

        List<FileMetadata> result = serviceMock.uploadFiles(filenames, parentDir);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(files, result);

        verify(serviceMock).uploadFiles(filenames, parentDir);
        verify(serviceMock).uploadFiles(filenames, parent);
        verify(serviceMock).getUploadDir(parentDir);
        verify(serviceMock).getDirectoryService();
        verify(directoryService).findOrCreateDirectory(anyString());

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(directoryService);
        verifyZeroInteractions(file1);
        verifyZeroInteractions(file2);
        verifyZeroInteractions(parent);
    }

}
