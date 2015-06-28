package net.czpilar.dropdrive.core.credential;

/**
 * Credential holder.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class Credential {

    private final String accessToken;

    public Credential(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
