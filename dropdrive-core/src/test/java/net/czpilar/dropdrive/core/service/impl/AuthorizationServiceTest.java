package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxWebAuth;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import net.czpilar.dropdrive.core.exception.AuthorizationFailedException;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

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
    @Mock
    private DropDriveSetting dropDriveSetting;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        service.setDbxWebAuth(dbxWebAuth);
        service.setDropDriveCredential(credential);
        service.setDropDriveSetting(dropDriveSetting);
        when(dropDriveSetting.getRedirectUri()).thenReturn("http://127.0.0.1:8784/dropdrive");
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
        verify(dropDriveSetting).getRedirectUri();

        verifyNoMoreInteractions(dbxWebAuth);
    }

    @Test
    public void testAuthorize() throws Exception {
        String authorizationCode = "test-authorization-code";
        String accessToken = "test-access-token";
        String refreshToken = "test-refresh-token";

        DbxAuthFinish dbxAuthFinish = new DbxAuthFinish(accessToken, null, refreshToken, "user-id", "team-id", "account-id", "url-state");

        when(dbxWebAuth.finishFromRedirect(anyString(), any(DbxSessionStore.class), any(Map.class))).thenReturn(dbxAuthFinish);

        Credential result = service.authorize(authorizationCode);

        assertNotNull(result);
        assertEquals(accessToken, result.accessToken());
        assertEquals(refreshToken, result.refreshToken());

        verify(dbxWebAuth).finishFromRedirect(anyString(), any(DbxSessionStore.class), any(Map.class));
        verify(credential).saveRefreshToken(refreshToken);
        verify(dropDriveSetting).getRedirectUri();

        verifyNoMoreInteractions(dbxWebAuth);
        verifyNoMoreInteractions(credential);
    }

    @Test
    public void testAuthorizeWithExceptionDuringStoringCredential() throws Exception {
        String authorizationCode = "test-authorization-code";

        when(dbxWebAuth.finishFromRedirect(anyString(), any(DbxSessionStore.class), any(Map.class))).thenThrow(DbxException.class);

        assertThrows(AuthorizationFailedException.class, () -> service.authorize(authorizationCode));

        verify(dbxWebAuth).finishFromRedirect(anyString(), any(DbxSessionStore.class), any(Map.class));
        verify(dropDriveSetting).getRedirectUri();

        verifyNoMoreInteractions(dbxWebAuth);
        verifyNoMoreInteractions(credential);
    }
}
