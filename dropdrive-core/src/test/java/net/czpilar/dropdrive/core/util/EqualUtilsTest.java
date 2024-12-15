package net.czpilar.dropdrive.core.util;

import com.dropbox.core.v2.files.FileMetadata;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EqualUtilsTest {

    private java.io.File testFile;

    @BeforeEach
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        testFile = new java.io.File(tempDir + "some-test-file-for-md5-" + System.currentTimeMillis() + ".properties");
        FileUtils.writeStringToFile(testFile, "Some test file data to store.", Charset.defaultCharset(), false);
    }

    @AfterEach
    public void after() throws IOException {
        Files.deleteIfExists(testFile.toPath());
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
        FileMetadata file = new FileMetadata("icon", "id", new Date(path.toFile().lastModified() - 1000),
                new Date(path.toFile().lastModified() - 1000), "123456789ab", path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsNotEqualWhereLengthIsNotEqual() {
        Path path = Paths.get(testFile.getPath());
        FileMetadata file = new FileMetadata("icon", "id", new Date(path.toFile().lastModified()),
                new Date(path.toFile().lastModified()), "123456789ab", path.toFile().length() + 1);

        boolean result = EqualUtils.equals(file, path);

        assertFalse(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndLastModifiedIsEqual() {
        Path path = Paths.get(testFile.getPath());
        FileMetadata file = new FileMetadata("icon", "id", new Date(path.toFile().lastModified()),
                new Date(path.toFile().lastModified()), "123456789ab", path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testEqualsWhereFileAndPathIsEqualWhereLengthIsEqualAndRemoteLastModifiedIsGreater() {
        Path path = Paths.get(testFile.getPath());
        FileMetadata file = new FileMetadata("icon", "id", new Date(path.toFile().lastModified() + 1000),
                new Date(path.toFile().lastModified() + 1000), "123456789ab", path.toFile().length());

        boolean result = EqualUtils.equals(file, path);

        assertTrue(result);
    }

    @Test
    public void testNotEquals1() {
        try (MockedStatic<EqualUtils> equalUtilsMockedStatic = Mockito.mockStatic(EqualUtils.class)) {
            equalUtilsMockedStatic.when(() -> EqualUtils.notEquals(any(FileMetadata.class), any(Path.class))).thenCallRealMethod();
            equalUtilsMockedStatic.when(() -> EqualUtils.equals(any(FileMetadata.class), any(Path.class))).thenReturn(true);

            boolean result = EqualUtils.notEquals(mock(FileMetadata.class), mock(Path.class));

            assertFalse(result);
        }
    }

    @Test
    public void testNotEquals2() {
        try (MockedStatic<EqualUtils> equalUtilsMockedStatic = Mockito.mockStatic(EqualUtils.class)) {
            equalUtilsMockedStatic.when(() -> EqualUtils.notEquals(any(FileMetadata.class), any(Path.class))).thenCallRealMethod();
            equalUtilsMockedStatic.when(() -> EqualUtils.equals(any(FileMetadata.class), any(Path.class))).thenReturn(false);

            boolean result = EqualUtils.notEquals(mock(FileMetadata.class), mock(Path.class));

            assertTrue(result);
        }
    }
}