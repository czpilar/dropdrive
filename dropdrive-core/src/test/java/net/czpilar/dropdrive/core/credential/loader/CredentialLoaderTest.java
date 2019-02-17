package net.czpilar.dropdrive.core.credential.loader;

import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import net.czpilar.dropdrive.core.exception.NoCredentialFoundException;
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
public class CredentialLoaderTest {

    private CredentialLoader loader;

    @Mock
    private IDropDriveCredential dropDriveCredential;

    @Mock
    private Credential credential;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        loader = new CredentialLoader();
        loader.setDropDriveCredential(dropDriveCredential);
    }

    @Test(expected = NoCredentialFoundException.class)
    public void testGetCredentialWhereNoCredentialLoaded() {
        loader.setDropDriveCredential(null);
        loader.getCredential();
    }

    @Test
    public void testGetCredential() {
        when(dropDriveCredential.getCredential()).thenReturn(credential);

        Credential result = loader.getCredential();

        assertNotNull(result);
        assertEquals(credential, result);

        verify(dropDriveCredential).getCredential();

        verifyNoMoreInteractions(dropDriveCredential);
        verifyZeroInteractions(credential);
    }
}
