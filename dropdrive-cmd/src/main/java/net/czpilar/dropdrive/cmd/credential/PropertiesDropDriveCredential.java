package net.czpilar.dropdrive.cmd.credential;

import net.czpilar.dropdrive.cmd.exception.PropertiesFileException;
import net.czpilar.dropdrive.core.credential.impl.AbstractDropDriveCredential;
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

    private final String uploadDirPropertyKey;
    private final String accessTokenPropertyKey;
    private final String defaultUploadDir;

    private String propertyFile;
    private Properties properties;

    public PropertiesDropDriveCredential(String uploadDirPropertyKey, String accessTokenPropertyKey, String defaultUploadDir) {
        this.uploadDirPropertyKey = uploadDirPropertyKey;
        this.accessTokenPropertyKey = accessTokenPropertyKey;
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
            try (FileOutputStream out = new FileOutputStream(propertyFile)) {
                getProperties().store(out, "dropdrive properties file");
            }
        } catch (IOException e) {
            throw new PropertiesFileException("Cannot save properties file.", e);
        }
    }

    private void loadProperties() {
        try {
            try (FileInputStream in = new FileInputStream(propertyFile)) {
                getProperties().load(in);
            }
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
