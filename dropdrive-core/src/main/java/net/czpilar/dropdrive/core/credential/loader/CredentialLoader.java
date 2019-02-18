package net.czpilar.dropdrive.core.credential.loader;

import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import net.czpilar.dropdrive.core.exception.NoCredentialFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Auto loader of dropDrive credential from Spring context.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Component
public class CredentialLoader {

    private IDropDriveCredential dropDriveCredential;

    @Autowired
    public void setDropDriveCredential(IDropDriveCredential dropDriveCredential) {
        this.dropDriveCredential = dropDriveCredential;
    }

    public Credential getCredential() {
        if (dropDriveCredential == null) {
            throw new NoCredentialFoundException("No credential found.");
        }
        return dropDriveCredential.getCredential();
    }
}
