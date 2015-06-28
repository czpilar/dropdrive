package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWebAuthNoRedirect;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.exception.AuthorizationFailedException;
import net.czpilar.dropdrive.core.service.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Authorization service with methods for authorization to Dropbox.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class AuthorizationService extends AbstractService implements IAuthorizationService {

    private DbxWebAuthNoRedirect dbxWebAuth;

    @Autowired
    public void setDbxWebAuth(DbxWebAuthNoRedirect dbxWebAuth) {
        this.dbxWebAuth = dbxWebAuth;
    }

    @Override
    public String getAuthorizationURL() {
        return dbxWebAuth.start();
    }

    @Override
    public Credential authorize(String authorizationCode) {
        try {
            Credential credential = new Credential(dbxWebAuth.finish(authorizationCode).accessToken);
            getDropDriveCredential().saveCredential(credential);
            return credential;
        } catch (DbxException e) {
            throw new AuthorizationFailedException("Error occurs during authorization process.", e);
        }
    }
}
