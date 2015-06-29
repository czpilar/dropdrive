package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
import net.czpilar.dropdrive.core.exception.DirectoryHandleException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DbxClient.class, DbxEntry.Folder.class })
public class DirectoryServiceTest {

    private DirectoryService service = new DirectoryService();

    @Mock
    private DirectoryService serviceMock;

    @Mock
    private DbxClient dbxClient;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);

        when(serviceMock.getDbxClient()).thenReturn(dbxClient);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOneDirectoryWhereDirnameIsNullAndParentDirIsNull() {
        service.createOneDirectory(null, null);
    }

    @Test
    public void testCreateOneDirectoryWhereParentDirIsNull() throws DbxException {
        String dirname = "/test-dirname";
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);

        when(serviceMock.createOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getPath(anyString(), any(DbxEntry.Folder.class))).thenReturn(dirname);
        when(dbxClient.createFolder(anyString())).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.createOneDirectory(dirname, null);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).createOneDirectory(dirname, null);
        verify(serviceMock).getPath(dirname, null);
        verify(serviceMock).getDbxClient();
        verify(dbxClient).createFolder(dirname);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyZeroInteractions(directory);
    }

    @Test
    public void testCreateOneDirectoryWhereParentDirExists() throws DbxException {
        String dirname = "/test-dirname";
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);

        when(serviceMock.createOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getPath(anyString(), any(DbxEntry.Folder.class))).thenReturn(dirname);
        when(dbxClient.createFolder(anyString())).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.createOneDirectory(dirname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).createOneDirectory(dirname, parentDir);
        verify(serviceMock).getPath(dirname, parentDir);
        verify(serviceMock).getDbxClient();
        verify(dbxClient).createFolder(dirname);

        verifyNoMoreInteractions(serviceMock);
        verifyNoMoreInteractions(dbxClient);
        verifyZeroInteractions(directory);
    }

    @Test(expected = DirectoryHandleException.class)
    public void testCreateOneDirectoryWhereDbxExceptionWasThrown() throws DbxException {
        String dirname = "/test-dirname";
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);

        when(serviceMock.createOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.getPath(anyString(), any(DbxEntry.Folder.class))).thenReturn(dirname);
        when(dbxClient.createFolder(anyString())).thenThrow(DbxException.class);

        try {
            serviceMock.createOneDirectory(dirname, null);
        } catch (DirectoryHandleException e) {
            verify(serviceMock).createOneDirectory(dirname, null);
            verify(serviceMock).getPath(dirname, null);
            verify(serviceMock).getDbxClient();
            verify(dbxClient).createFolder(dirname);

            verifyNoMoreInteractions(serviceMock);
            verifyNoMoreInteractions(dbxClient);
            verifyZeroInteractions(directory);

            throw e;
        }
    }

    @Test
    public void testFindOrCreateOneDirectoryWhereDirectoryIsFound() {
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);
        String dirname = "/test-dirname";

        when(serviceMock.findOrCreateOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findFolder(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.findOrCreateOneDirectory(dirname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateOneDirectory(dirname, parentDir);
        verify(serviceMock).findFolder(dirname, parentDir);

        verifyNoMoreInteractions(serviceMock);

        verifyZeroInteractions(parentDir);
        verifyZeroInteractions(directory);
    }

    @Test
    public void testFindOrCreateOneDirectoryWhereDirectoryIsCreated() {
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);
        String dirname = "/test-dirname";

        when(serviceMock.findOrCreateOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findFolder(anyString(), any(DbxEntry.Folder.class))).thenReturn(null);
        when(serviceMock.createOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.findOrCreateOneDirectory(dirname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateOneDirectory(dirname, parentDir);
        verify(serviceMock).findFolder(dirname, parentDir);
        verify(serviceMock).createOneDirectory(dirname, parentDir);

        verifyNoMoreInteractions(serviceMock);

        verifyZeroInteractions(parentDir);
        verifyZeroInteractions(directory);
    }

    @Test
    public void testFindDirectoryWithPathname() {
        String pathname = "/test-pathname";
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);

        when(serviceMock.findDirectory(anyString())).thenCallRealMethod();
        when(serviceMock.findDirectory(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.findDirectory(pathname);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findDirectory(pathname);
        verify(serviceMock).findDirectory(pathname, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWhereDirnameIsNullAndNextPathnameIsNull() {
        when(serviceMock.findDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();

        DbxEntry.Folder result = serviceMock.findDirectory(null, null);

        assertNull(result);

        verify(serviceMock).findDirectory(null, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWherePathnameHasMoreDirsButCurrentDirIsNull() {
        String dirname1 = "test-dirname1";
        String dirname2 = "test-dirname2";
        String pathname = dirname1 + "/" + dirname2;
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);

        when(serviceMock.findDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findFolder(anyString(), any(DbxEntry.Folder.class))).thenReturn(null);

        DbxEntry.Folder result = serviceMock.findDirectory(pathname, parentDir);

        assertNull(result);

        verify(serviceMock).findDirectory(pathname, parentDir);
        verify(serviceMock).findFolder(dirname1, parentDir);

        verifyNoMoreInteractions(serviceMock);

        verifyZeroInteractions(parentDir);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWherePathnameHasOneDir() {
        String pathname = "test-dirname";
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);

        when(serviceMock.findDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findFolder(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.findDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findDirectory(pathname, parentDir);
        verify(serviceMock).findFolder(pathname, parentDir);

        verifyNoMoreInteractions(serviceMock);

        verifyZeroInteractions(parentDir);
        verifyZeroInteractions(directory);
    }

    @Test
    public void testFindDirectoryWithPathnameAndParentWherePathnameHasMoreDirs() {
        String dirname1 = "test-dirname1";
        String dirname2 = "test-dirname2";
        String dirname3 = "test-dirname3";
        String pathname = dirname1 + "/" + dirname2 + "/" + dirname3;
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory1 = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory2 = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory3 = mock(DbxEntry.Folder.class);

        when(serviceMock.findDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findFolder(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory1, directory2, directory3);

        DbxEntry.Folder result = serviceMock.findDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory3, result);

        verify(serviceMock).findDirectory(pathname, parentDir);
        verify(serviceMock).findDirectory(dirname2 + "/" + dirname3, directory1);
        verify(serviceMock).findDirectory(dirname3, directory2);
        verify(serviceMock).findFolder(dirname1, parentDir);
        verify(serviceMock).findFolder(dirname2, directory1);
        verify(serviceMock).findFolder(dirname3, directory2);

        verifyNoMoreInteractions(serviceMock);

        verifyZeroInteractions(directory1);
        verifyZeroInteractions(directory2);
        verifyZeroInteractions(directory3);
        verifyZeroInteractions(parentDir);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathname() {
        String pathname = "test-pathname";
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);

        when(serviceMock.findOrCreateDirectory(anyString())).thenCallRealMethod();
        when(serviceMock.findOrCreateDirectory(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.findOrCreateDirectory(pathname);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateDirectory(pathname);
        verify(serviceMock).findOrCreateDirectory(pathname, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathnameAndParentWhereDirnameIsNullAndNextPathnameIsNull() {
        when(serviceMock.findOrCreateDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();

        DbxEntry.Folder result = serviceMock.findOrCreateDirectory(null, null);

        assertNull(result);

        verify(serviceMock).findOrCreateDirectory(null, null);

        verifyNoMoreInteractions(serviceMock);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathnameAndParentWherePathnameHasOneDir() {
        String pathname = "test-dirname";
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory = mock(DbxEntry.Folder.class);

        when(serviceMock.findOrCreateDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findOrCreateOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory);

        DbxEntry.Folder result = serviceMock.findOrCreateDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory, result);

        verify(serviceMock).findOrCreateDirectory(pathname, parentDir);
        verify(serviceMock).findOrCreateOneDirectory(pathname, parentDir);

        verifyNoMoreInteractions(serviceMock);

        verifyZeroInteractions(directory);
        verifyZeroInteractions(parentDir);
    }

    @Test
    public void testFindOrCreateDirectoryWithPathnameAndParentWherePathnameHasMoreDirs() {
        String dirname1 = "test-dirname1";
        String dirname2 = "test-dirname2";
        String dirname3 = "test-dirname3";
        String pathname = dirname1 + "/" + dirname2 + "/" + dirname3;
        DbxEntry.Folder parentDir = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory1 = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory2 = mock(DbxEntry.Folder.class);
        DbxEntry.Folder directory3 = mock(DbxEntry.Folder.class);

        when(serviceMock.findOrCreateDirectory(anyString(), any(DbxEntry.Folder.class))).thenCallRealMethod();
        when(serviceMock.findOrCreateOneDirectory(anyString(), any(DbxEntry.Folder.class))).thenReturn(directory1, directory2, directory3);

        DbxEntry.Folder result = serviceMock.findOrCreateDirectory(pathname, parentDir);

        assertNotNull(result);
        assertEquals(directory3, result);

        verify(serviceMock).findOrCreateDirectory(pathname, parentDir);
        verify(serviceMock).findOrCreateDirectory(dirname2 + "/" + dirname3, directory1);
        verify(serviceMock).findOrCreateDirectory(dirname3, directory2);
        verify(serviceMock).findOrCreateOneDirectory(dirname1, parentDir);
        verify(serviceMock).findOrCreateOneDirectory(dirname2, directory1);
        verify(serviceMock).findOrCreateOneDirectory(dirname3, directory2);

        verifyNoMoreInteractions(serviceMock);

        verifyZeroInteractions(directory1);
        verifyZeroInteractions(directory2);
        verifyZeroInteractions(directory3);
        verifyZeroInteractions(parentDir);
    }
}
