package net.czpilar.dropdrive.core.setting;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveSettingTest {

    @Test
    public void testGDriveSetting() {
        String applicationName = "test-application-name";
        String applicationVersion = "test-application-version";
        String clientKey = "test-client-key";
        String clientSecret = "test-client-secret";
        DropDriveSetting setting = new DropDriveSetting(applicationName, applicationVersion, clientKey, clientSecret);

        assertEquals(applicationName, setting.getApplicationName());
        assertEquals(applicationVersion, setting.getApplicationVersion());
        assertEquals(clientKey, setting.getClientKey());
        assertEquals(clientSecret, setting.getClientSecret());
    }
}
