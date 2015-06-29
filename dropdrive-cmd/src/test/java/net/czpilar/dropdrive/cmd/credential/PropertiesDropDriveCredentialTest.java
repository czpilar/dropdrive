package net.czpilar.dropdrive.cmd.credential;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesDropDriveCredentialTest {

    private static final String UPLOAD_DIR_PROPERTY_KEY = "dropdrive.uploadDir";
    private static final String ACCESS_TOKEN_PROPERTY_KEY = "dropdrive.accessToken";
    private static final String DEFAULT_UPLOAD_DIR = "dropdrive-uploads";

    private PropertiesDropDriveCredential dropDrivePropertiesNotExist;
    private PropertiesDropDriveCredential dropDrivePropertiesExist;

    private File propertiesNotExist;
    private File propertiesExist;
    private Properties properties;

    @Before
    public void before() throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        propertiesNotExist = new File(tempDir + "test-properties-not-exist-file-" + System.currentTimeMillis() + ".properties");
        propertiesExist = new File(tempDir + "test-properties-exist-file-" + System.currentTimeMillis() + ".properties");
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);

        properties = new Properties();
        properties.setProperty(UPLOAD_DIR_PROPERTY_KEY, "test-upload-dir");
        properties.setProperty(ACCESS_TOKEN_PROPERTY_KEY, "test-access-token");
        properties.store(new FileOutputStream(propertiesExist), "properties created in test");

        dropDrivePropertiesNotExist = createDropDriveCredential(propertiesNotExist.getPath());
        dropDrivePropertiesExist = createDropDriveCredential(propertiesExist.getPath());
    }

    @After
    public void after() throws IOException {
        deleteIfExist(propertiesNotExist);
        deleteIfExist(propertiesExist);
    }

    private PropertiesDropDriveCredential createDropDriveCredential(String propertyFile) {
        PropertiesDropDriveCredential dropDriveCredential = new PropertiesDropDriveCredential();
        dropDriveCredential.setUploadDirPropertyKey(UPLOAD_DIR_PROPERTY_KEY);
        dropDriveCredential.setAccessTokenPropertyKey(ACCESS_TOKEN_PROPERTY_KEY);
        dropDriveCredential.setDefaultUploadDir(DEFAULT_UPLOAD_DIR);
        dropDriveCredential.setPropertyFile(propertyFile);
        return dropDriveCredential;
    }

    private void deleteIfExist(File file) throws IOException {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testGetAccessTokenWherePropertiesNotExist() {
        assertNull(dropDrivePropertiesNotExist.getAccessToken());
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
        assertEquals(DEFAULT_UPLOAD_DIR, dropDrivePropertiesNotExist.getUploadDir());
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
