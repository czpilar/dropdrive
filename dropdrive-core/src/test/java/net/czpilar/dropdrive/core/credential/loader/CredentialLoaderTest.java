package net.czpilar.dropdrive.core.credential.loader;

import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import net.czpilar.dropdrive.core.exception.NoCredentialFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class CredentialLoaderTest {

    private CredentialLoader loader;

    @Mock
    private IDropDriveCredential dropDriveCredential;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        loader = new CredentialLoader(dropDriveCredential);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testConstructorWhereNoCredentialLoaded() {
        assertThrows(NoCredentialFoundException.class, () -> new CredentialLoader(null));
    }

    @Test
    public void testGetRefreshToken() {
        when(dropDriveCredential.getRefreshToken()).thenReturn("test-refresh-token");

        String result = loader.getRefreshToken();

        assertNotNull(result);
        assertEquals("test-refresh-token", result);

        verify(dropDriveCredential).getRefreshToken();

        verifyNoMoreInteractions(dropDriveCredential);
    }

    @Test
    public void testGetRefreshTokenReturnsNull() {
        when(dropDriveCredential.getRefreshToken()).thenReturn(null);

        String result = loader.getRefreshToken();

        assertNull(result);

        verify(dropDriveCredential).getRefreshToken();

        verifyNoMoreInteractions(dropDriveCredential);
    }
}
