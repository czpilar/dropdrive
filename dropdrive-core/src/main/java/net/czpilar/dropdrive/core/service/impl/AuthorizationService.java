package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuth;
import net.czpilar.dropdrive.core.credential.Credential;
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
        return dbxWebAuth.authorize(DbxWebAuth.newRequestBuilder().withNoRedirect().build());
    }

    @Override
    public Credential authorize(String authorizationCode) {
        try {
            Credential credential = new Credential(dbxWebAuth.finishFromCode(authorizationCode).getAccessToken());
            getDropDriveCredential().saveCredential(credential);
            return credential;
        } catch (DbxException e) {
            throw new AuthorizationFailedException("Error occurs during authorization process.", e);
        }
    }
}
