package net.czpilar.dropdrive.core.credential.impl;

/**
 * Simple implementation of {@link net.czpilar.dropdrive.core.credential.IDropDriveCredential} interface
 * for dropDrive credential.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class SimpleDropDriveCredential extends AbstractDropDriveCredential {

    private String accessToken;
    private String uploadDir;

    @Override
    protected String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    protected void saveTokens(String accessToken) {
        setAccessToken(accessToken);
    }

    @Override
    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
