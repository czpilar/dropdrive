package net.czpilar.dropdrive.cmd.credential;

import net.czpilar.dropdrive.cmd.exception.PropertiesFileException;
import net.czpilar.dropdrive.core.credential.impl.AbstractDropDriveCredential;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Implementation of dropDrive credential using
 * properties file to store tokens and upload dir name.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesDropDriveCredential extends AbstractDropDriveCredential {

    private String uploadDirPropertyKey;
    private String accessTokenPropertyKey;
    private String defaultUploadDir;

    private String propertyFile;
    private Properties properties;

    @Required
    public void setUploadDirPropertyKey(String uploadDirPropertyKey) {
        this.uploadDirPropertyKey = uploadDirPropertyKey;
    }

    @Required
    public void setAccessTokenPropertyKey(String accessTokenPropertyKey) {
        this.accessTokenPropertyKey = accessTokenPropertyKey;
    }

    @Required
    public void setDefaultUploadDir(String defaultUploadDir) {
        this.defaultUploadDir = defaultUploadDir;
    }

    public void setPropertyFile(String propertyFile) {
        this.propertyFile = propertyFile;
    }

    protected Properties getProperties() {
        Assert.notNull(propertyFile, "Missing property file name.");
        if (properties == null) {
            properties = new Properties();
            try {
                loadProperties();
            } catch (PropertiesFileException e) {
                saveProperties();
            }

            if (getUploadDir() == null) {
                setUploadDir(defaultUploadDir);
                saveProperties();
            }
        }
        return properties;
    }

    private void saveProperties() {
        try {
            getProperties().store(new FileOutputStream(propertyFile), "dropdrive properties file");
        } catch (IOException e) {
            throw new PropertiesFileException("Cannot save properties file.", e);
        }
    }

    private void loadProperties() {
        try {
            getProperties().load(new FileInputStream(propertyFile));
        } catch (IOException e) {
            throw new PropertiesFileException("Cannot load properties file.", e);
        }
    }

    @Override
    public String getAccessToken() {
        return getProperties().getProperty(accessTokenPropertyKey);
    }

    public void setAccessToken(String accessToken) {
        getProperties().setProperty(accessTokenPropertyKey, accessToken);
    }

    @Override
    public void saveTokens(String accessToken) {
        setAccessToken(accessToken);
        saveProperties();
    }

    @Override
    public String getUploadDir() {
        return getProperties().getProperty(uploadDirPropertyKey);
    }

    public void setUploadDir(String uploadDir) {
        getProperties().setProperty(uploadDirPropertyKey, uploadDir);
    }
}
