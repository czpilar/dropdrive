package net.czpilar.dropdrive.core.credential;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CredentialTest {

    @Test
    public void testGetAccessToken() {
        Credential credential = new Credential("test-access-token", "test-refresh-token");
        assertEquals("test-access-token", credential.accessToken());
    }

    @Test
    public void testGetRefreshToken() {
        Credential credential = new Credential("test-access-token", "test-refresh-token");
        assertEquals("test-refresh-token", credential.refreshToken());
    }
}
