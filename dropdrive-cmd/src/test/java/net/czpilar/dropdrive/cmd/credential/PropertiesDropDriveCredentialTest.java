package net.czpilar.dropdrive.cmd.credential;

import net.czpilar.dropdrive.cmd.context.DropDriveCmdContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesDropDriveCredentialTest {

    private PropertiesDropDriveCredential dropDrivePropertiesNotExist;
    private PropertiesDropDriveCredential dropDrivePropertiesExist;

    private File propertiesNotExist;
    private File propertiesExist;

    @BeforeEach
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        propertiesNotExist = new File(tempDir + "test-properties-not-exist-file-" + System.currentTimeMillis() + ".properties");
        propertiesExist = new File(tempDir + "test-properties-exist-file-" + System.currentTimeMillis() + ".properties");
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);

        Properties properties = new Properties();
        properties.setProperty(DropDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY, "test-upload-dir");
        properties.setProperty(DropDriveCmdContext.REFRESH_TOKEN_PROPERTY_KEY, "test-refresh-token");
        try (FileOutputStream out = new FileOutputStream(propertiesExist)) {
            properties.store(out, "properties created in test");
        }

        dropDrivePropertiesNotExist = createDropDriveCredential(propertiesNotExist.getPath());
        dropDrivePropertiesExist = createDropDriveCredential(propertiesExist.getPath());
    }

    @AfterEach
    public void after() throws IOException {
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);
    }

    private PropertiesDropDriveCredential createDropDriveCredential(String propertyFile) {
        PropertiesDropDriveCredential dropDriveCredential = new PropertiesDropDriveCredential(
                DropDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY, DropDriveCmdContext.REFRESH_TOKEN_PROPERTY_KEY, DropDriveCmdContext.DEFAULT_UPLOAD_DIR);
        dropDriveCredential.setPropertyFile(propertyFile);
        return dropDriveCredential;
    }

    private void deleteIfExist(File file) throws IOException {
        Files.deleteIfExists(file.toPath());
    }

    @Test
    public void testGetRefreshTokenWherePropertiesExist() {
        assertEquals("test-refresh-token", dropDrivePropertiesExist.getRefreshToken());
    }

    @Test
    public void testGetRefreshTokenWherePropertiesNotExist() {
        assertNull(dropDrivePropertiesNotExist.getRefreshToken());
    }

    @Test
    public void testSaveRefreshToken() {
        dropDrivePropertiesNotExist.saveRefreshToken("new-refresh-token-to-save");

        PropertiesDropDriveCredential dropDrivePropertiesInTest = createDropDriveCredential(propertiesNotExist.getPath());

        assertEquals("new-refresh-token-to-save", dropDrivePropertiesInTest.getRefreshToken());
    }

    @Test
    public void testSaveRefreshTokenWithNull() {
        dropDrivePropertiesNotExist.saveRefreshToken(null);

        PropertiesDropDriveCredential dropDrivePropertiesInTest = createDropDriveCredential(propertiesNotExist.getPath());

        assertNull(dropDrivePropertiesInTest.getRefreshToken());
    }

    @Test
    public void testGetUploadDirWherePropertiesNotExist() {
        assertEquals(DropDriveCmdContext.DEFAULT_UPLOAD_DIR, dropDrivePropertiesNotExist.getUploadDir());
    }

    @Test
    public void testGetUploadDirWherePropertiesExist() {
        assertEquals("test-upload-dir", dropDrivePropertiesExist.getUploadDir());
    }

    @Test
    public void testSetUploadDir() {
        dropDrivePropertiesNotExist.setUploadDir("new-upload-dir");
        assertEquals("new-upload-dir", dropDrivePropertiesNotExist.getUploadDir());
    }
}
