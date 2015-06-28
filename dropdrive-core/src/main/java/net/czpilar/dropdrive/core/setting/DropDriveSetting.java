package net.czpilar.dropdrive.core.setting;

/**
 * Holder for DropDrive secrets, scopes and other info.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveSetting {

    private final String applicationName;
    private final String applicationVersion;
    private final String clientKey;
    private final String clientSecret;

    public DropDriveSetting(String applicationName, String applicationVersion, String clientKey, String clientSecret) {
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getClientKey() {
        return clientKey;
    }

    public String getClientSecret() {
        return clientSecret;
    }
}
