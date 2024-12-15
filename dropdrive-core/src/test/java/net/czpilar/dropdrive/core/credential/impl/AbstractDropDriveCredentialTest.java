package net.czpilar.dropdrive.core.credential.impl;

import net.czpilar.dropdrive.core.credential.Credential;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class AbstractDropDriveCredentialTest {

    @Mock
    private AbstractDropDriveCredential dropDriveCredential;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testGetCredential() {
        when(dropDriveCredential.getCredential()).thenCallRealMethod();
        String accessToken = "access-token";
        when(dropDriveCredential.getAccessToken()).thenReturn(accessToken);

        Credential result = dropDriveCredential.getCredential();

        assertNotNull(result);
        assertEquals(accessToken, result.accessToken());

        verify(dropDriveCredential).getCredential();
        verify(dropDriveCredential).getAccessToken();

        verifyNoMoreInteractions(dropDriveCredential);
    }

    @Test
    public void testSaveCredential() {
        doCallRealMethod().when(dropDriveCredential).saveCredential(any(Credential.class));
        doNothing().when(dropDriveCredential).saveTokens(anyString());

        Credential credential = new Credential("access-token");

        dropDriveCredential.saveCredential(credential);

        verify(dropDriveCredential).saveCredential(any(Credential.class));
        verify(dropDriveCredential).saveTokens("access-token");

        verifyNoMoreInteractions(dropDriveCredential);
    }
}
