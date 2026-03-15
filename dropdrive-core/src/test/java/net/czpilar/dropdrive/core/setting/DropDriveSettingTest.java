package net.czpilar.dropdrive.core.setting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
class DropDriveSettingTest {

    @Test
    void testDropDriveSetting() {
        String applicationVersion = "test-application-version";
        String clientKey = "test-client-key";
        String clientSecret = "test-client-secret";
        String redirectUri = "http://127.0.0.1:9999/test-context";
        int redirectUriPort = 9999;
        String redirectUriContext = "/test-context";
        DropDriveSetting setting = new DropDriveSetting(applicationVersion, clientKey, clientSecret, redirectUri, redirectUriPort, redirectUriContext);

        assertEquals(DropDriveSetting.APPLICATION_NAME, setting.getApplicationName());
        assertEquals(applicationVersion, setting.getApplicationVersion());
        assertEquals(clientKey, setting.getClientKey());
        assertEquals(clientSecret, setting.getClientSecret());
        assertEquals(redirectUriPort, setting.getRedirectUriPort());
        assertEquals(redirectUriContext, setting.getRedirectUriContext());
        assertEquals(redirectUri, setting.getRedirectUri());
    }
}
