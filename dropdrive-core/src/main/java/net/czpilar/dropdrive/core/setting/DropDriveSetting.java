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
    private final int redirectUriPort;
    private final String redirectUriContext;

    public DropDriveSetting(@Value("${dropdrive.version}") String applicationVersion,
                            @Value("${dropdrive.core.drive.clientKey}") String clientKey,
                            @Value("${dropdrive.core.drive.clientSecret}") String clientSecret,
                            @Value("${dropdrive.core.drive.redirectUri.port:8784}") int redirectUriPort,
                            @Value("${dropdrive.core.drive.redirectUri.context:/dropdrive}") String redirectUriContext) {
        this.applicationVersion = applicationVersion;
        this.clientKey = clientKey;
        this.clientSecret = clientSecret;
        this.redirectUriPort = redirectUriPort;
        this.redirectUriContext = redirectUriContext;
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

    public int getRedirectUriPort() {
        return redirectUriPort;
    }

    public String getRedirectUriContext() {
        return redirectUriContext;
    }

    public String getRedirectUri() {
        return "http://127.0.0.1:" + redirectUriPort + redirectUriContext;
    }
}
