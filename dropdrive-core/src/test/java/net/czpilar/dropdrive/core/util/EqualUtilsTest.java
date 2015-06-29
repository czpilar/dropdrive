package net.czpilar.dropdrive.core.util;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import com.dropbox.core.DbxEntry;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ EqualUtils.class, DbxEntry.File.class })
public class EqualUtilsTest {

    private java.io.File testFile;

    @Before
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        testFile = new java.io.File(tempDir + "some-test-file-for-md5-" + System.currentTimeMillis() + ".properties");
        FileUtils.writeStringToFile(testFile, "Some test file data to store.");
    }

    @After
    public void after() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testEqualsWhereBothParametersAreNull() {
        DbxEntry.File file = null;
        Path path = null;
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileIsNull() {
        DbxEntry.File file = null;
        Path path = mock(Path.class);
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWherePathIsNull() {
        DbxEntry.File file = mock(DbxEntry.File.class);
        Path path = null;
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileNotExists() {
        DbxEntry.File file = mock(DbxEntry.File.class);
        Path path = mock(Path.class);
        java.io.File ioFile = new java.io.File("invalid-file-to-test.qwerty");
        when(path.toFile()).thenReturn(ioFile);

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsNotEqualWhereLengthIsEqualAndRemoteLastModifiedIsLower() {
        Path path = Paths.get(testFile.getPath());
        DbxEntry.File file = new DbxEntry.File("/path", "icon", false, path.toFile().length(), "20 MB",
                new Date(path.toFile().lastModified() - 1000), new Date(path.toFile().lastModified() - 1000), "rev");

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsNotEqualWhereLengthIsNotEqual() {
        Path path = Paths.get(testFile.getPath());
        DbxEntry.File file = new DbxEntry.File("/path", "icon", false, path.toFile().length() + 1, "20 MB",
                new Date(path.toFile().lastModified()), new Date(path.toFile().lastModified()), "rev");

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndLastModifiedIsEqual() {
        Path path = Paths.get(testFile.getPath());
        DbxEntry.File file = new DbxEntry.File("/path", "icon", false, path.toFile().length(), "20 MB",
                new Date(path.toFile().lastModified()), new Date(path.toFile().lastModified()), "rev");

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndRemoteLastModifiedIsGreater() {
        Path path = Paths.get(testFile.getPath());
        DbxEntry.File file = new DbxEntry.File("/path", "icon", false, path.toFile().length(), "20 MB",
                new Date(path.toFile().lastModified() + 1000), new Date(path.toFile().lastModified() + 1000), "rev");

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testNotEquals1() {
        PowerMockito.mockStatic(EqualUtils.class);
        when(EqualUtils.notEquals(any(DbxEntry.File.class), any(Path.class))).thenCallRealMethod();
        when(EqualUtils.equals(any(DbxEntry.File.class), any(Path.class))).thenReturn(true);

        boolean result = EqualUtils.notEquals(mock(DbxEntry.File.class), mock(Path.class));

        assertFalse(result);
    }

    @Test
    public void testNotEquals2() {
        PowerMockito.mockStatic(EqualUtils.class);
        when(EqualUtils.notEquals(any(DbxEntry.File.class), any(Path.class))).thenCallRealMethod();
        when(EqualUtils.equals(any(DbxEntry.File.class), any(Path.class))).thenReturn(false);

        boolean result = EqualUtils.notEquals(mock(DbxEntry.File.class), mock(Path.class));

        assertTrue(result);
    }
}