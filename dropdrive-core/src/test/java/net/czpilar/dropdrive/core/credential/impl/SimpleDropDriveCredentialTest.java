package net.czpilar.dropdrive.core.credential.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author David Pilar (david@czpilar.net)
 */
class SimpleDropDriveCredentialTest {

    private SimpleDropDriveCredential credential;

    @BeforeEach
    void before() {
        credential = new SimpleDropDriveCredential();
    }

    @Test
    void testGetRefreshToken() {
        assertNull(credential.getRefreshToken());
    }

    @Test
    void testSaveRefreshToken() {
        assertNull(credential.getRefreshToken());

        String refreshToken = "test-refresh-token";

        credential.saveRefreshToken(refreshToken);

        assertEquals(refreshToken, credential.getRefreshToken());
    }

    @Test
    void testSetAndGetUploadDir() {
        assertNull(credential.getUploadDir());

        String uploadDir = "test-upload-dir";
        credential.setUploadDir(uploadDir);

        assertEquals(uploadDir, credential.getUploadDir());
    }

}
