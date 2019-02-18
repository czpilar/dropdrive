package net.czpilar.dropdrive.core.setting;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Holder for DropDrive secrets, scopes and other info.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Component
public class DropDriveSetting {

    public static final String APPLICATION_NAME = "dropdrive";

    private final String applicationVersion;
    private final String clientKey;
    private final String clientSecret;

    public DropDriveSetting(@Value("${dropdrive.version}") String applicationVersion,
                            @Value("${dropdrive.core.drive.clientKey}") String clientKey,
                            @Value("${dropdrive.core.drive.clientSecret}") String clientSecret) {
        this.applicationVersion = applicationVersion;
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
    }

    public String getApplicationName() {
        return APPLICATION_NAME;
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
