package net.czpilar.dropdrive.core.credential;

/**
 * dropDrive credential interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IDropDriveCredential {

    /**
     * Returns refresh token.
     *
     * @return refresh token
     */
    String getRefreshToken();

    /**
     * Saves refresh token.
     *
     * @param refreshToken refresh token
     */
    void saveRefreshToken(String refreshToken);

    /**
     * Returns upload dir.
     *
     * @return upload dir
     */
    String getUploadDir();

}
