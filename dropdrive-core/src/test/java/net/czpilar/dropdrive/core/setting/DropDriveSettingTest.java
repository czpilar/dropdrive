package net.czpilar.dropdrive.core.setting;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveSettingTest {

    @Test
    public void testGDriveSetting() {
        String applicationVersion = "test-application-version";
        String clientKey = "test-client-key";
        String clientSecret = "test-client-secret";
        DropDriveSetting setting = new DropDriveSetting(applicationVersion, clientKey, clientSecret);

        assertEquals(DropDriveSetting.APPLICATION_NAME, setting.getApplicationName());
        assertEquals(applicationVersion, setting.getApplicationVersion());
        assertEquals(clientKey, setting.getClientKey());
        assertEquals(clientSecret, setting.getClientSecret());
    }
}
