package net.czpilar.dropdrive.core.credential.impl;

import net.czpilar.dropdrive.core.credential.Credential;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class AbstractDropDriveCredentialTest {

    @Mock
    private AbstractDropDriveCredential dropDriveCredential;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetCredential() {
        when(dropDriveCredential.getCredential()).thenCallRealMethod();
        String accessToken = "access-token";
        when(dropDriveCredential.getAccessToken()).thenReturn(accessToken);

        Credential result = dropDriveCredential.getCredential();

        assertNotNull(result);
        assertEquals(accessToken, result.getAccessToken());

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
