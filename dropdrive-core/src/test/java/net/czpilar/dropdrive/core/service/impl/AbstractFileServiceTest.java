package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import net.czpilar.dropdrive.core.exception.FileHandleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DbxClientV2.class, DbxUserFilesRequests.class})
public class AbstractFileServiceTest {

    @Mock
    private AbstractFileService service;

    @Mock
    private DbxClientV2 dbxClient;

    @Mock
    private DbxUserFilesRequests files;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        when(service.getDbxClient()).thenReturn(dbxClient);
        when(dbxClient.files()).thenReturn(files);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPathWithNullFilename() {
        Metadata entry = mock(Metadata.class);

        when(service.getPath(anyString(), any(Metadata.class))).thenCallRealMethod();

        try {
            service.getPath(null, entry);
        } catch (IllegalArgumentException e) {
            verify(service).getPath(null, entry);

            verifyNoMoreInteractions(service);

            throw e;
        }
    }

    @Test
    public void testGetPathWithFilenameAndNullParent() {
        when(service.getPath(anyString(), any(Metadata.class))).thenCallRealMethod();

        String result = service.getPath("filename", null);

        assertNotNull(result);
        assertEquals("/filename", result);

        verify(service).getPath("filename", null);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetPathWithFilenameStartingWithSlashAndNullParent() {
        when(service.getPath(anyString(), any(Metadata.class))).thenCallRealMethod();

        String result = service.getPath("/filename", null);

        assertNotNull(result);
        assertEquals("/filename", result);

        verify(service).getPath("/filename", null);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetPathWithFilenameAndExistingParent() {
        FolderMetadata parent = new FolderMetadata("icon", "/path/to/parent", "/path/to/parent", "id");

        when(service.getPath(anyString(), any(Metadata.class))).thenCallRealMethod();

        String result = service.getPath("filename", parent);

        assertNotNull(result);
        assertEquals("/path/to/parent/filename", result);

        verify(service).getPath("filename", parent);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindFolderWhenEntryIsNull() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(service.findFolder(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(Metadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenReturn(null);

        FolderMetadata result = service.findFolder("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFolder("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
    }

    @Test
    public void testFindFolderWhenEntryIsFile() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);
        FileMetadata entry = mock(FileMetadata.class);

        when(service.findFolder(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(FileMetadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenReturn(entry);

        FolderMetadata result = service.findFolder("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFolder("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test
    public void testFindFolderWhenEntryIsFolder() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);
        FolderMetadata entry = mock(FolderMetadata.class);

        when(service.findFolder(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(FileMetadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenReturn(entry);

        FolderMetadata result = service.findFolder("filename", parent);

        assertNotNull(result);
        assertEquals(entry, result);

        verify(service).getDbxClient();
        verify(service).findFolder("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test(expected = FileHandleException.class)
    public void testFindFolderWhenException() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(service.findFolder(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(FileMetadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenThrow(DbxException.class);

        try {
            service.findFolder("filename", parent);
        } catch (FileHandleException e) {
            verify(service).getDbxClient();
            verify(service).findFolder("filename", parent);
            verify(service).getPath("filename", parent);
            verify(dbxClient).files();
            verify(files).getMetadata("/path/to/entry");

            verifyNoMoreInteractions(service);
            verifyNoMoreInteractions(dbxClient);

            throw e;
        }
    }

    @Test
    public void testFindFileWhenEntryIsNull() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(service.findFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(FileMetadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenReturn(null);

        FileMetadata result = service.findFile("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFile("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
    }

    @Test
    public void testFindFileWhenEntryIsFolder() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);
        FolderMetadata entry = mock(FolderMetadata.class);

        when(service.findFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(FileMetadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenReturn(entry);

        FileMetadata result = service.findFile("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFile("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test
    public void testFindFileWhenEntryIsFile() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);
        FileMetadata entry = mock(FileMetadata.class);

        when(service.findFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(FileMetadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenReturn(entry);

        FileMetadata result = service.findFile("filename", parent);

        assertNotNull(result);
        assertEquals(entry, result);

        verify(service).getDbxClient();
        verify(service).findFile("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test(expected = FileHandleException.class)
    public void testFindFileWhenException() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(service.findFile(anyString(), any(FolderMetadata.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(FileMetadata.class))).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenThrow(DbxException.class);

        try {
            service.findFile("filename", parent);
        } catch (FileHandleException e) {
            verify(service).getDbxClient();
            verify(service).findFile("filename", parent);
            verify(service).getPath("filename", parent);
            verify(dbxClient).files();
            verify(files).getMetadata("/path/to/entry");

            verifyNoMoreInteractions(service);
            verifyNoMoreInteractions(dbxClient);

            throw e;
        }
    }
}