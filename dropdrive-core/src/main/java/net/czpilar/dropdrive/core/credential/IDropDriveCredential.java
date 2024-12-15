package net.czpilar.dropdrive.core.credential;

/**
 * dropDrive credential interface.
 *
 * @author David Pilar (david@czpilar.net)
 */
public interface IDropDriveCredential {

    /**
     * Returns credential.
     *
     * @return credential
     */
    Credential getCredential();

    /**
     * Saves credential.
     *
     * @param credential credential
     */
    void saveCredential(Credential credential);

    /**
     * Returns upload dir.
     *
     * @return upload dir
     */
    String getUploadDir();

}
