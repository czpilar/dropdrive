package net.czpilar.dropdrive.cmd.credential;

import net.czpilar.dropdrive.cmd.context.DropDriveCmdContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesDropDriveCredentialTest {

    private PropertiesDropDriveCredential dropDrivePropertiesNotExist;
    private PropertiesDropDriveCredential dropDrivePropertiesExist;

    private File propertiesNotExist;
    private File propertiesExist;
    private Properties properties;

    @BeforeEach
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        propertiesNotExist = new File(tempDir + "test-properties-not-exist-file-" + System.currentTimeMillis() + ".properties");
        propertiesExist = new File(tempDir + "test-properties-exist-file-" + System.currentTimeMillis() + ".properties");
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);

        properties = new Properties();
        properties.setProperty(DropDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY, "test-upload-dir");
        properties.setProperty(DropDriveCmdContext.ACCESS_TOKEN_PROPERTY_KEY, "test-access-token");
        properties.store(new FileOutputStream(propertiesExist), "properties created in test");

        dropDrivePropertiesNotExist = createDropDriveCredential(propertiesNotExist.getPath());
        dropDrivePropertiesExist = createDropDriveCredential(propertiesExist.getPath());
    }

    @AfterEach
    public void after() {
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);
    }

    private PropertiesDropDriveCredential createDropDriveCredential(String propertyFile) {
        PropertiesDropDriveCredential dropDriveCredential = new PropertiesDropDriveCredential(
                DropDriveCmdContext.UPLOAD_DIR_PROPERTY_KEY, DropDriveCmdContext.ACCESS_TOKEN_PROPERTY_KEY, DropDriveCmdContext.DEFAULT_UPLOAD_DIR);
        dropDriveCredential.setPropertyFile(propertyFile);
        return dropDriveCredential;
    }

    private void deleteIfExist(File file) {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testGetAccessTokenWherePropertiesExist() {
        assertEquals("test-access-token", dropDrivePropertiesExist.getAccessToken());
    }

    @Test
    public void testSetAccessToken() {
        dropDrivePropertiesNotExist.setAccessToken("new-access-token");
        assertEquals("new-access-token", dropDrivePropertiesNotExist.getAccessToken());
    }

    @Test
    public void testSaveTokens() {
        dropDrivePropertiesNotExist.saveTokens("new-access-token-to-save");

        PropertiesDropDriveCredential dropDrivePropertiesInTest = createDropDriveCredential(propertiesNotExist.getPath());

        assertEquals("new-access-token-to-save", dropDrivePropertiesInTest.getAccessToken());
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
