package net.czpilar.dropdrive.core.util;

import com.dropbox.core.v2.files.FileMetadata;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EqualUtils.class})
public class EqualUtilsTest {

    private java.io.File testFile;

    @Before
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        testFile = new java.io.File(tempDir + "some-test-file-for-md5-" + System.currentTimeMillis() + ".properties");
        FileUtils.writeStringToFile(testFile, "Some test file data to store.", Charset.defaultCharset(), false);
    }

    @After
    public void after() {
        if (testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testEqualsWhereBothParametersAreNull() {
        FileMetadata file = null;
        Path path = null;
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileIsNull() {
        FileMetadata file = null;
        Path path = mock(Path.class);
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWherePathIsNull() {
        FileMetadata file = mock(FileMetadata.class);
        Path path = null;
        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileNotExists() {
        FileMetadata file = mock(FileMetadata.class);
        Path path = mock(Path.class);
        java.io.File ioFile = new java.io.File("invalid-file-to-test.qwerty");
        when(path.toFile()).thenReturn(ioFile);

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsNotEqualWhereLengthIsEqualAndRemoteLastModifiedIsLower() {
        Path path = Paths.get(testFile.getPath());
        FileMetadata file = new FileMetadata("icon", "/path", "/path", "id", new Date(path.toFile().lastModified() - 1000),
                new Date(path.toFile().lastModified() - 1000), "123456789ab", path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsNotEqualWhereLengthIsNotEqual() {
        Path path = Paths.get(testFile.getPath());
        FileMetadata file = new FileMetadata("icon", "/path", "/path", "id", new Date(path.toFile().lastModified()),
                new Date(path.toFile().lastModified()), "123456789ab", path.toFile().length() + 1);

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndLastModifiedIsEqual() {
        Path path = Paths.get(testFile.getPath());
        FileMetadata file = new FileMetadata("icon", "/path", "/path", "id", new Date(path.toFile().lastModified()),
                new Date(path.toFile().lastModified()), "123456789ab", path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndRemoteLastModifiedIsGreater() {
        Path path = Paths.get(testFile.getPath());
        FileMetadata file = new FileMetadata("icon", "/path", "/path", "id", new Date(path.toFile().lastModified() + 1000),
                new Date(path.toFile().lastModified() + 1000), "123456789ab", path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testNotEquals1() {
        PowerMockito.mockStatic(EqualUtils.class);
        when(EqualUtils.notEquals(any(FileMetadata.class), any(Path.class))).thenCallRealMethod();
        when(EqualUtils.equals(any(FileMetadata.class), any(Path.class))).thenReturn(true);

        boolean result = EqualUtils.notEquals(mock(FileMetadata.class), mock(Path.class));

        assertFalse(result);
    }

    @Test
    public void testNotEquals2() {
        PowerMockito.mockStatic(EqualUtils.class);
        when(EqualUtils.notEquals(any(FileMetadata.class), any(Path.class))).thenCallRealMethod();
        when(EqualUtils.equals(any(FileMetadata.class), any(Path.class))).thenReturn(false);

        boolean result = EqualUtils.notEquals(mock(FileMetadata.class), mock(Path.class));

        assertTrue(result);
    }
}