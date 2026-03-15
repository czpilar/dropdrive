package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.exception.AuthorizationFailedException;
import net.czpilar.dropdrive.core.service.IAuthorizationService;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Authorization service with methods for authorization to Dropbox.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Service
public class AuthorizationService extends AbstractService implements IAuthorizationService {

    private DbxWebAuth dbxWebAuth;
    private DropDriveSetting dropDriveSetting;

    private final DbxSessionStore sessionStore = new SimpleSessionStore();

    @Autowired
    public void setDbxWebAuth(DbxWebAuth dbxWebAuth) {
        this.dbxWebAuth = dbxWebAuth;
    }

    @Autowired
    public void setDropDriveSetting(DropDriveSetting dropDriveSetting) {
        this.dropDriveSetting = dropDriveSetting;
    }

    @Override
    public String getAuthorizationURL() {
        return dbxWebAuth.authorize(DbxWebAuth.newRequestBuilder()
                .withRedirectUri(dropDriveSetting.getRedirectUri(), sessionStore)
                .withTokenAccessType(TokenAccessType.OFFLINE)
                .build());
    }

    @Override
    public Credential authorize(String authorizationCode) {
        try {
            Map<String, String[]> queryParams = Map.of("code", new String[]{authorizationCode},
                    "state", new String[]{sessionStore.get()});
            DbxAuthFinish authFinish = dbxWebAuth.finishFromRedirect(
                    dropDriveSetting.getRedirectUri(), sessionStore, queryParams);
            Credential credential = new Credential(authFinish.getAccessToken(), authFinish.getRefreshToken());
            getDropDriveCredential().saveRefreshToken(credential.refreshToken());
            return credential;
        } catch (Exception e) {
            throw new AuthorizationFailedException("Error occurs during authorization process.", e);
        }
    }

    private static class SimpleSessionStore implements DbxSessionStore {

        private String value;

        @Override
        public String get() {
            return value;
        }

        @Override
        public void set(String value) {
            this.value = value;
        }

        @Override
        public void clear() {
            this.value = null;
        }
    }
}
