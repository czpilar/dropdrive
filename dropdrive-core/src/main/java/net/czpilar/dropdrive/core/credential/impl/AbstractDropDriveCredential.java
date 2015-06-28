package net.czpilar.dropdrive.core.credential.impl;

import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;

/**
 * Template implementation of {@link net.czpilar.dropdrive.core.credential.IDropDriveCredential} interface
 * for dropDrive credential.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractDropDriveCredential implements IDropDriveCredential {

    /**
     * Returns access token.
     *
     * @return
     */
    protected abstract String getAccessToken();

    /**
     * Saves access token.
     *
     * @param accessToken
     */
    protected abstract void saveTokens(String accessToken);

    @Override
    public Credential getCredential() {
        return new Credential(getAccessToken());
    }

    @Override
    public void saveCredential(Credential credential) {
        saveTokens(credential.getAccessToken());
    }
}
