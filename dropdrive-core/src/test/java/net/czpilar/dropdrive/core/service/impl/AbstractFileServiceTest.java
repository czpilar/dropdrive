package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
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
@PrepareForTest({ DbxClient.class, DbxEntry.File.class, DbxEntry.Folder.class })
public class AbstractFileServiceTest {

    @Mock
    private AbstractFileService service;

    @Mock
    private DbxClient dbxClient;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        when(service.getDbxClient()).thenReturn(dbxClient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPathWithNullFilename() {
        DbxEntry entry = mock(DbxEntry.class);

        when(service.getPath(anyString(), any(DbxEntry.class))).thenCallRealMethod();

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
        when(service.getPath(anyString(), any(DbxEntry.class))).thenCallRealMethod();

        String result = service.getPath("filename", null);

        assertNotNull(result);
        assertEquals("/filename", result);

        verify(service).getPath("filename", null);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetPathWithFilenameStartingWithSlashAndNullParent() {
        when(service.getPath(anyString(), any(DbxEntry.class))).thenCallRealMethod();

        String result = service.getPath("/filename", null);

        assertNotNull(result);
        assertEquals("/filename", result);

        verify(service).getPath("/filename", null);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testGetPathWithFilenameAndExistingParent() {
        DbxEntry.Folder parent = new DbxEntry.Folder("/path/to/parent", "icon", false);

        when(service.getPath(anyString(), any(DbxEntry.class))).thenCallRealMethod();

        String result = service.getPath("filename", parent);

        assertNotNull(result);
        assertEquals("/path/to/parent/filename", result);

        verify(service).getPath("filename", parent);

        verifyNoMoreInteractions(service);
    }

    @Test
    public void testFindFolderWhenEntryIsNull() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(service.findFolder(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenReturn(null);

        DbxEntry.Folder result = service.findFolder("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFolder("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
    }

    @Test
    public void testFindFolderWhenEntryIsFile() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);
        DbxEntry.File entry = mock(DbxEntry.File.class);

        when(entry.isFolder()).thenReturn(false);
        when(service.findFolder(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenReturn(entry);

        DbxEntry.Folder result = service.findFolder("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFolder("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).getMetadata("/path/to/entry");
        verify(entry).isFolder();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test
    public void testFindFolderWhenEntryIsFolder() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);
        DbxEntry.Folder entry = mock(DbxEntry.Folder.class);

        when(entry.isFolder()).thenReturn(true);
        when(service.findFolder(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenReturn(entry);

        DbxEntry.Folder result = service.findFolder("filename", parent);

        assertNotNull(result);
        assertEquals(entry, result);

        verify(service).getDbxClient();
        verify(service).findFolder("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).getMetadata("/path/to/entry");
        verify(entry).isFolder();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test(expected = FileHandleException.class)
    public void testFindFolderWhenException() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(service.findFolder(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenThrow(DbxException.class);

        try {
            service.findFolder("filename", parent);
        } catch (FileHandleException e) {
            verify(service).getDbxClient();
            verify(service).findFolder("filename", parent);
            verify(service).getPath("filename", parent);
            verify(dbxClient).getMetadata("/path/to/entry");

            verifyNoMoreInteractions(service);
            verifyNoMoreInteractions(dbxClient);

            throw e;
        }
    }

    @Test
    public void testFindFileWhenEntryIsNull() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(service.findFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenReturn(null);

        DbxEntry.File result = service.findFile("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFile("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).getMetadata("/path/to/entry");

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
    }

    @Test
    public void testFindFileWhenEntryIsFolder() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);
        DbxEntry.File entry = mock(DbxEntry.File.class);

        when(entry.isFile()).thenReturn(false);
        when(service.findFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenReturn(entry);

        DbxEntry.File result = service.findFile("filename", parent);

        assertNull(result);

        verify(service).getDbxClient();
        verify(service).findFile("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).getMetadata("/path/to/entry");
        verify(entry).isFile();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test
    public void testFindFileWhenEntryIsFile() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);
        DbxEntry.File entry = mock(DbxEntry.File.class);

        when(entry.isFile()).thenReturn(true);
        when(service.findFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenReturn(entry);

        DbxEntry.File result = service.findFile("filename", parent);

        assertNotNull(result);
        assertEquals(entry, result);

        verify(service).getDbxClient();
        verify(service).findFile("filename", parent);
        verify(service).getPath("filename", parent);
        verify(dbxClient).getMetadata("/path/to/entry");
        verify(entry).isFile();

        verifyNoMoreInteractions(service);
        verifyNoMoreInteractions(dbxClient);
        verifyNoMoreInteractions(entry);
    }

    @Test(expected = FileHandleException.class)
    public void testFindFileWhenException() throws DbxException {
        DbxEntry.Folder parent = mock(DbxEntry.Folder.class);

        when(service.findFile(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(service.getPath(anyString(), any(DbxEntry.class))).thenReturn("/path/to/entry");
        when(dbxClient.getMetadata(anyString())).thenThrow(DbxException.class);

        try {
            service.findFile("filename", parent);
        } catch (FileHandleException e) {
            verify(service).getDbxClient();
            verify(service).findFile("filename", parent);
            verify(service).getPath("filename", parent);
            verify(dbxClient).getMetadata("/path/to/entry");

            verifyNoMoreInteractions(service);
            verifyNoMoreInteractions(dbxClient);

            throw e;
        }
    }
}