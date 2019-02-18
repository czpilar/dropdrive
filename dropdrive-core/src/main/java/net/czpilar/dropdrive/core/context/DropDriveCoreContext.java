package net.czpilar.dropdrive.core.context;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import net.czpilar.dropdrive.core.credential.loader.CredentialLoader;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "net.czpilar.dropdrive.core")
@PropertySource("dropdrive.properties")
public class DropDriveCoreContext {

    private DbxRequestConfig dbxRequestConfig;

    @Autowired
    private DropDriveSetting dropDriveSetting;

    @Autowired
    private CredentialLoader credentialLoader;

    @Value("${dropdrive.version}")
    private String applicationVersion;

    @Value("${dropdrive.core.drive.clientKey}")
    private String clientKey;

    @Value("${dropdrive.core.drive.clientSecret}")
    private String clientSecret;


    @Bean
    public DbxAppInfo dbxAppInfo() {
        return new DbxAppInfo(dropDriveSetting.getClientKey(), dropDriveSetting.getClientSecret());
    }

    @Bean
    public DbxRequestConfig dbxRequestConfig() {
        if (dbxRequestConfig == null) {
            dbxRequestConfig = new DbxRequestConfig(dropDriveSetting.getApplicationName());
        }
        return dbxRequestConfig;
    }

    @Bean
    @Scope("prototype")
    public DbxClientV2 dbxClient() {
        return new DbxClientV2(dbxRequestConfig(), credentialLoader.getCredential().getAccessToken());
    }

    @Bean
    public DbxWebAuth dbxWebAuth() {
        return new DbxWebAuth(dbxRequestConfig(), dbxAppInfo());
    }
}
