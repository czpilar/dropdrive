package net.czpilar.dropdrive.core.setting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveSettingTest {

    @Test
    public void testDropDriveSetting() {
        String applicationVersion = "test-application-version";
        String clientKey = "test-client-key";
        String clientSecret = "test-client-secret";
        int redirectUriPort = 9999;
        String redirectUriContext = "/test-context";
        DropDriveSetting setting = new DropDriveSetting(applicationVersion, clientKey, clientSecret, redirectUriPort, redirectUriContext);

        assertEquals(DropDriveSetting.APPLICATION_NAME, setting.getApplicationName());
        assertEquals(applicationVersion, setting.getApplicationVersion());
        assertEquals(clientKey, setting.getClientKey());
        assertEquals(clientSecret, setting.getClientSecret());
        assertEquals(redirectUriPort, setting.getRedirectUriPort());
        assertEquals(redirectUriContext, setting.getRedirectUriContext());
        assertEquals("http://127.0.0.1:9999/test-context", setting.getRedirectUri());
    }
}
