package net.czpilar.dropdrive.core.credential.impl;

/**
 * Simple implementation of {@link net.czpilar.dropdrive.core.credential.IDropDriveCredential} interface
 * for dropDrive credential.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class SimpleDropDriveCredential extends AbstractDropDriveCredential {

    private String refreshToken;
    private String uploadDir;

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public void saveRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
