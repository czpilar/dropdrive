package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;
import net.czpilar.dropdrive.core.exception.AuthorizationFailedException;
import net.czpilar.dropdrive.core.service.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Authorization service with methods for authorization to Dropbox.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Service
public class AuthorizationService extends AbstractService implements IAuthorizationService {

    private DbxWebAuth dbxWebAuth;

    @Autowired
    public void setDbxWebAuth(DbxWebAuth dbxWebAuth) {
        this.dbxWebAuth = dbxWebAuth;
    }

    @Override
    public String getAuthorizationURL() {
        return dbxWebAuth.authorize(DbxWebAuth.newRequestBuilder()
                .withNoRedirect()
                .withTokenAccessType(TokenAccessType.OFFLINE)
                .build());
    }

    @Override
    public void authorize(String authorizationCode) {
        try {
            String refreshToken = dbxWebAuth.finishFromCode(authorizationCode).getRefreshToken();
            getDropDriveCredential().saveRefreshToken(refreshToken);
        } catch (DbxException e) {
            throw new AuthorizationFailedException("Error occurs during authorization process.", e);
        }
    }
}
