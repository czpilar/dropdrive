package net.czpilar.dropdrive.core.credential;

/**
 * Credential holder for Dropbox OAuth2 tokens.
 *
 * @author David Pilar (david@czpilar.net)
 */
public record Credential(String accessToken, String refreshToken) {
}
