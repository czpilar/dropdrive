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
     * @return
     */
    Credential getCredential();

    /**
     * Saves credential.
     *
     * @param credential
     */
    void saveCredential(Credential credential);

    /**
     * Returns upload dir.
     *
     * @return
     */
    String getUploadDir();

}
