package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.v2.DbxClientV2;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Template service.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractService {

    private DbxClientV2 dbxClient;
    private IDropDriveCredential dropDriveCredential;

    protected DbxClientV2 getDbxClient() {
        return dbxClient;
    }

    @Autowired
    public void setDbxClient(DbxClientV2 dbxClient) {
        this.dbxClient = dbxClient;
    }

    public IDropDriveCredential getDropDriveCredential() {
        return dropDriveCredential;
    }

    @Autowired
    public void setDropDriveCredential(IDropDriveCredential dropDriveCredential) {
        this.dropDriveCredential = dropDriveCredential;
    }

}
