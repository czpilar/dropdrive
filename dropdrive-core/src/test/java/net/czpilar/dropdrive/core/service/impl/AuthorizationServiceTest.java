package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuthNoRedirect;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import net.czpilar.dropdrive.core.exception.AuthorizationFailedException;
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
public class AuthorizationServiceTest {

    private AuthorizationService service = new AuthorizationService();

    @Mock
    private DbxWebAuthNoRedirect dbxWebAuth;
    @Mock
    private IDropDriveCredential credential;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        service.setDbxWebAuth(dbxWebAuth);
        service.setDropDriveCredential(credential);
    }

    @Test
    public void testGetAuthorizationURL() {
        when(dbxWebAuth.start()).thenReturn("authorization-request-url");

        String result = service.getAuthorizationURL();

        assertEquals("authorization-request-url", result);

        verify(dbxWebAuth).start();

        verifyNoMoreInteractions(dbxWebAuth);
    }

    @Test
    public void testAuthorize() throws DbxException {
        String authorizationCode = "test-authorization-code";
        String accessToken = "test-access-token";

        DbxAuthFinish dbxAuthFinish = new DbxAuthFinish(accessToken, "user-id", "url-state");

        when(dbxWebAuth.finish(authorizationCode)).thenReturn(dbxAuthFinish);

        Credential result = service.authorize(authorizationCode);

        assertNotNull(result);
        assertEquals(accessToken, result.getAccessToken());

        verify(dbxWebAuth).finish(authorizationCode);
        verify(credential).saveCredential(result);

        verifyNoMoreInteractions(dbxWebAuth);
        verifyNoMoreInteractions(credential);
    }

    @Test(expected = AuthorizationFailedException.class)
    public void testAuthorizeWithExceptionDuringStoringCredential() throws DbxException {
        String authorizationCode = "test-authorization-code";

        when(dbxWebAuth.finish(authorizationCode)).thenThrow(DbxException.class);

        try {
            service.authorize(authorizationCode);
        } catch (AuthorizationFailedException e) {
            verify(dbxWebAuth).finish(authorizationCode);

            verifyNoMoreInteractions(dbxWebAuth);
            verifyNoMoreInteractions(credential);

            throw e;
        }
    }
}
