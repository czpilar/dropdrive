package net.czpilar.dropdrive.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
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

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DbxClient.class, DbxEntry.File.class, DbxEntry.Folder.class, EqualUtils.class, FileRequest.class })
public class FileServiceTest {

    private FileService service = new FileService(3);

    @Mock
    private FileService serviceMock;

    @Mock
    private IDirectoryService directoryService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private DbxClient dbxClient;

    @Mock
    private IDropDriveCredential dropDriveCredential;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        service.setApplicationContext(applicationContext);
        service.setDropDriveCredential(dropDriveCredential);
        service.setDirectoryService(directoryService);

        when(serviceMock.getDirectoryService()).thenReturn(directoryService);
        when(applicationContext.getBean(DbxClient.class)).thenReturn(dbxClient);

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
        DbxEntry.Folder parentDir = null;
        DbxEntry.File file = mock(DbxEntry.File.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(DbxEntry.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(null);
        when(FileRequest.createInsert(any(DbxClient.class), anyString(), any(File.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(file);

        DbxEntry.File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).getPath(filename, parentDir);
        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(insert).execute();
        verify(insert).setProgressListener(any(IFileUploadProgressListener.class));

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(file);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParent() throws IOException, DbxException {
        String filename = "test-filename";
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.File file = mock(DbxEntry.File.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(DbxEntry.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(null);
        when(FileRequest.createInsert(any(DbxClient.class), anyString(), any(File.class))).thenReturn(insert);
        when(insert.execute()).thenReturn(file);

        DbxEntry.File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).getPath(filename, parentDir);
        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(insert).execute();
        verify(insert).setProgressListener(any(IFileUploadProgressListener.class));

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test(expected = FileHandleException.class)
    public void testUploadFileWithStringFilenameAndNullParentAndThrownException() throws IOException, DbxException {
        String filename = "test-filename";
        DbxEntry.Folder parentDir = null;
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(DbxEntry.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(null);
        when(FileRequest.createInsert(any(DbxClient.class), anyString(), any(File.class))).thenReturn(insert);
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
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.File file = mock(DbxEntry.File.class);
        FileRequest update = mock(FileRequest.class);

        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(file);
        when(EqualUtils.notEquals(any(DbxEntry.File.class), any(Path.class))).thenReturn(true);
        when(FileRequest.createUpdate(any(DbxClient.class), any(DbxEntry.File.class), any(File.class))).thenReturn(update);
        when(update.execute()).thenReturn(file);

        DbxEntry.File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(update).execute();
        verify(update).setProgressListener(any(IFileUploadProgressListener.class));

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(update);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentWhereNothingToUpdate() throws IOException {
        String filename = "test-filename";
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.File file = mock(DbxEntry.File.class);

        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(file);
        when(EqualUtils.notEquals(any(DbxEntry.File.class), any(Path.class))).thenReturn(false);

        DbxEntry.File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).findFile(filename, parentDir);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test
    public void testUploadFileWithStringFilenameAndFileParentAndRetryApplied() throws IOException, DbxException {
        String filename = "test-filename";
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        final DbxEntry.File file = mock(DbxEntry.File.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(DbxEntry.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(null);
        when(serviceMock.getRetries()).thenReturn(3);
        when(FileRequest.createInsert(any(DbxClient.class), anyString(), any(File.class))).thenReturn(insert);

        Answer<DbxEntry.File> answer = new Answer<DbxEntry.File>() {
            private int retry = 0;

            @Override
            public DbxEntry.File answer(InvocationOnMock invocation) throws Throwable {
                if (retry == 0) {
                    retry++;
                    throw mock(DbxException.class);
                }
                return file;
            }
        };
        when(insert.execute()).thenAnswer(answer);

        DbxEntry.File result = serviceMock.uploadFile(filename, parentDir);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).getPath(filename, parentDir);
        verify(serviceMock).uploadFile(filename, parentDir);
        verify(serviceMock).getDbxClient();
        verify(serviceMock).findFile(filename, parentDir);
        verify(serviceMock).getRetries();
        verify(insert, times(2)).execute();
        verify(insert).setProgressListener(any(IFileUploadProgressListener.class));

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(insert);
        verifyNoMoreInteractions(file);
        verifyNoMoreInteractions(parentDir);
    }

    @Test(expected = FileHandleException.class)
    public void testUploadFileWithStringFilenameAndFileParentAndRetryExceeded() throws IOException, DbxException {
        String filename = "test-filename";
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        final DbxEntry.File file = mock(DbxEntry.File.class);
        FileRequest insert = mock(FileRequest.class);

        when(serviceMock.getPath(anyString(), any(DbxEntry.class))).thenReturn(filename);
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
        when(serviceMock.findFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(null);
        when(serviceMock.getRetries()).thenReturn(3);
        when(FileRequest.createInsert(any(DbxClient.class), anyString(), any(File.class))).thenReturn(insert);
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
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        List<DbxEntry.File> result = service.uploadFiles(null, parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUploadFilesWithEmptyListOfFilenames() {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        List<DbxEntry.File> result = service.uploadFiles(new ArrayList<String>(), parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUploadFilesWhereUploadFileThrowsException() {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(serviceMock.uploadFiles(anyListOf(String.class), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenThrow(FileHandleException.class);

        List<DbxEntry.File> result = serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(serviceMock).uploadFiles(anyListOf(String.class), any(DbxEntry.Folder.class));
        verify(serviceMock).uploadFile("filename1", parent);
        verify(serviceMock).uploadFile("filename2", parent);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testUploadFilesWithListOfFilenames() {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(serviceMock.uploadFiles(anyListOf(String.class), any(DbxEntry.Folder.class))).thenCallRealMethod();
        DbxEntry.File file1 = mock(DbxEntry.File.class);
        DbxEntry.File file2 = mock(DbxEntry.File.class);
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(file1, file2);

        List<DbxEntry.File> result = serviceMock.uploadFiles(Arrays.asList("filename1", "filename2"), parent);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(file1, result.get(0));
        assertEquals(file2, result.get(1));

        verify(serviceMock).uploadFiles(anyListOf(String.class), any(DbxEntry.Folder.class));
        verify(serviceMock).uploadFile("filename1", parent);
        verify(serviceMock).uploadFile("filename2", parent);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testUploadFileWithStringFilenameAndStringParentDir() {
        String pathname = "test-parent-dir";
        String filename = "test-filename";
        DbxEntry.File file = mock(DbxEntry.File.class);
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(serviceMock.uploadFile(anyString(), anyString())).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(file);
        when(serviceMock.getUploadDir(anyString())).thenReturn(pathname);
        when(directoryService.findOrCreateDirectory(anyString())).thenReturn(parent);

        DbxEntry.File result = serviceMock.uploadFile(filename, pathname);

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
        DbxEntry.File file = mock(DbxEntry.File.class);

        when(serviceMock.uploadFile(anyString())).thenCallRealMethod();
        when(serviceMock.uploadFile(anyString(), any(DbxEntry.Folder.class))).thenReturn(file);

        DbxEntry.File result = serviceMock.uploadFile(filename);

        assertNotNull(result);
        assertEquals(file, result);

        verify(serviceMock).uploadFile(filename);
        verify(serviceMock).uploadFile(filename, (DbxEntry.Folder) null);

        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(file);
    }

    @Test
    public void testUploadFilesWithStringFilenames() {
        List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
        DbxEntry.File file1 = mock(DbxEntry.File.class);
        DbxEntry.File file2 = mock(DbxEntry.File.class);
        List<DbxEntry.File> files = Arrays.asList(file1, file2);

        when(serviceMock.uploadFiles(anyListOf(String.class))).thenCallRealMethod();
        when(serviceMock.uploadFiles(anyListOf(String.class), any(DbxEntry.Folder.class))).thenReturn(files);

        List<DbxEntry.File> result = serviceMock.uploadFiles(filenames);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(files, result);

        verify(serviceMock).uploadFiles(filenames);
        verify(serviceMock).uploadFiles(filenames, (DbxEntry.Folder) null);

        verifyNoMoreInteractions(serviceMock);
        verifyZeroInteractions(file1);
        verifyZeroInteractions(file2);
    }

    @Test
    public void testUploadFilesWithStringFilenamesAndStringParentDir() {
        String parentDir = "test-parent-dir";
        List<String> filenames = Arrays.asList("test-filename1", "test-filename2");
        DbxEntry.File file1 = mock(DbxEntry.File.class);
        DbxEntry.File file2 = mock(DbxEntry.File.class);
        List<DbxEntry.File> files = Arrays.asList(file1, file2);
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(serviceMock.uploadFiles(anyListOf(String.class), anyString())).thenCallRealMethod();
        when(serviceMock.uploadFiles(anyListOf(String.class), any(DbxEntry.Folder.class))).thenReturn(files);
        when(serviceMock.getUploadDir(anyString())).thenReturn(parentDir);
        when(directoryService.findOrCreateDirectory(anyString())).thenReturn(parent);

        List<DbxEntry.File> result = serviceMock.uploadFiles(filenames, parentDir);

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
