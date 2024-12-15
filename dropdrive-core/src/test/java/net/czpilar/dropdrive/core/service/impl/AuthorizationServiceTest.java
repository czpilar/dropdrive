package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import net.czpilar.dropdrive.core.exception.AuthorizationFailedException;
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
public class AuthorizationServiceTest {

    private final AuthorizationService service = new AuthorizationService();

    @Mock
    private DbxWebAuth dbxWebAuth;
    @Mock
    private IDropDriveCredential credential;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        service.setDbxWebAuth(dbxWebAuth);
        service.setDropDriveCredential(credential);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testGetAuthorizationURL() {
        when(dbxWebAuth.authorize(any())).thenReturn("authorization-request-url");

        String result = service.getAuthorizationURL();

        assertEquals("authorization-request-url", result);

        verify(dbxWebAuth).authorize(any());

        verifyNoMoreInteractions(dbxWebAuth);
    }

    @Test
    public void testAuthorize() throws DbxException {
        String authorizationCode = "test-authorization-code";
        String accessToken = "test-access-token";

        DbxAuthFinish dbxAuthFinish = new DbxAuthFinish(accessToken, "user-id", "url-state", "team-id", "url-state");

        when(dbxWebAuth.finishFromCode(authorizationCode)).thenReturn(dbxAuthFinish);

        Credential result = service.authorize(authorizationCode);

        assertNotNull(result);
        assertEquals(accessToken, result.accessToken());

        verify(dbxWebAuth).finishFromCode(authorizationCode);
        verify(credential).saveCredential(result);

        verifyNoMoreInteractions(dbxWebAuth);
        verifyNoMoreInteractions(credential);
    }

    @Test
    public void testAuthorizeWithExceptionDuringStoringCredential() throws DbxException {
        String authorizationCode = "test-authorization-code";

        when(dbxWebAuth.finishFromCode(authorizationCode)).thenThrow(DbxException.class);

        assertThrows(AuthorizationFailedException.class, () -> service.authorize(authorizationCode));

        verify(dbxWebAuth).finishFromCode(authorizationCode);

        verifyNoMoreInteractions(dbxWebAuth);
        verifyNoMoreInteractions(credential);
    }
}
