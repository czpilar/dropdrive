package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.Metadata;
import net.czpilar.dropdrive.core.exception.FileHandleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AbstractFileServiceTest {

    @Mock
    private AbstractFileService service;

    @Mock
    private DbxClientV2 dbxClient;

    @Mock
    private DbxUserFilesRequests files;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);

        when(service.getDbxClient()).thenReturn(dbxClient);
        when(dbxClient.files()).thenReturn(files);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testGetPathWithNullFilename() {
        Metadata entry = mock(Metadata.class);

        when(service.getPath(any(), any())).thenCallRealMethod();

        assertThrows(IllegalArgumentException.class, () -> service.getPath(null, entry));

        verify(service).getPath(null, entry);
        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetPathWithFilenameAndNullParent() {
        when(service.getPath(anyString(), any())).thenCallRealMethod();

        String result = service.getPath("filename", null);

        assertNotNull(result);
        assertEquals("/filename", result);

        verify(service).getPath("filename", null);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetPathWithFilenameStartingWithSlashAndNullParent() {
        when(service.getPath(anyString(), any())).thenCallRealMethod();

        String result = service.getPath("/filename", null);

        assertNotNull(result);
        assertEquals("/filename", result);

        verify(service).getPath("/filename", null);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetPathWithFilenameAndExistingParent() {
        FolderMetadata parent = new FolderMetadata("icon", "id", "/path/to/parent", "/path/to/parent", null, null, null, null, null);

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

        when(service.findFolder(anyString(), any())).thenCallRealMethod();
        when(service.getPath(anyString(), any())).thenReturn("/path/to/entry");
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

        when(service.findFolder(anyString(), any())).thenCallRealMethod();
        when(service.getPath(anyString(), any())).thenReturn("/path/to/entry");
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

    @Test
    public void testFindFolderWhenException() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(service.findFolder(anyString(), any())).thenCallRealMethod();
        when(service.getPath(anyString(), any())).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenThrow(DbxException.class);

        assertThrows(FileHandleException.class, () -> service.findFolder("filename", parent));

        verify(service).getDbxClient();
        verify(service).findFolder("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
    }

    @Test
    public void testFindFileWhenEntryIsNull() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(service.findFile(anyString(), any())).thenCallRealMethod();
        when(service.getPath(anyString(), any())).thenReturn("/path/to/entry");
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

        when(service.findFile(anyString(), any())).thenCallRealMethod();
        when(service.getPath(anyString(), any())).thenReturn("/path/to/entry");
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

        when(service.findFile(anyString(), any())).thenCallRealMethod();
        when(service.getPath(anyString(), any())).thenReturn("/path/to/entry");
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

    @Test
    public void testFindFileWhenException() throws DbxException {
        FolderMetadata parent = mock(FolderMetadata.class);

        when(service.findFile(anyString(), any())).thenCallRealMethod();
        when(service.getPath(anyString(), any())).thenReturn("/path/to/entry");
        when(files.getMetadata(anyString())).thenThrow(DbxException.class);

        assertThrows(FileHandleException.class, () -> service.findFile("filename", parent));

        verify(service).getDbxClient();
        verify(service).findFile("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).files();
        verify(files).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
    }
}