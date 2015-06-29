package net.czpilar.dropdrive.core.service.impl;

import com.dropbox.core.DbxClient;
import net.czpilar.dropdrive.core.credential.IDropDriveCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * Template service.
 *
 * @author David Pilar (david@czpilar.net)
 */
public abstract class AbstractService {

    private ApplicationContext applicationContext;
    private IDropDriveCredential dropDriveCredential;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public IDropDriveCredential getDropDriveCredential() {
        return dropDriveCredential;
    }

    @Autowired
    public void setDropDriveCredential(IDropDriveCredential dropDriveCredential) {
        this.dropDriveCredential = dropDriveCredential;
    }

    protected DbxClient getDbxClient() {
        return applicationContext.getBean(DbxClient.class);
    }

}
