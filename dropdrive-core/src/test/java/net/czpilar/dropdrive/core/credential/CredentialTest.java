package net.czpilar.dropdrive.core.credential;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class CredentialTest {

    @Test
    public void testGetAccessToken() {
        String accessToken = "access-token";
        Credential credential = new Credential(accessToken);

        assertEquals(accessToken, credential.accessToken());
    }
}