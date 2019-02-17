package net.czpilar.dropdrive.core.credential.impl;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class SimpleDropDriveCredentialTest {

    private SimpleDropDriveCredential credential;

    @Before
    public void before() {
        credential = new SimpleDropDriveCredential();
    }

    @Test
    public void testSetAndGetAccessToken() {
        assertNull(credential.getAccessToken());

        String accessToken = "test-access-token";
        credential.setAccessToken(accessToken);

        assertEquals(accessToken, credential.getAccessToken());
    }

    @Test
    public void testSaveTokens() {
        assertNull(credential.getAccessToken());

        String accessToken = "test-access-token";

        credential.saveTokens(accessToken);

        assertEquals(accessToken, credential.getAccessToken());
    }

    @Test
    public void testSetAndGetUploadDir() {
        assertNull(credential.getUploadDir());

        String uploadDir = "test-upload-dir";
        credential.setUploadDir(uploadDir);

        assertEquals(uploadDir, credential.getUploadDir());
    }

}
