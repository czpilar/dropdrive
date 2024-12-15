package net.czpilar.dropdrive.core.context;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.v2.DbxClientV2;
import net.czpilar.dropdrive.core.credential.loader.CredentialLoader;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackages = "net.czpilar.dropdrive.core")
@PropertySource("classpath:dropdrive.properties")
public class DropDriveCoreContext {

    private final DbxRequestConfig dbxRequestConfig;
    private final DbxAppInfo dbxAppInfo;

    @Autowired
    private CredentialLoader credentialLoader;

    public DropDriveCoreContext(DropDriveSetting dropDriveSetting) {
        dbxRequestConfig = new DbxRequestConfig(dropDriveSetting.getApplicationName());
        dbxAppInfo = new DbxAppInfo(dropDriveSetting.getClientKey(), dropDriveSetting.getClientSecret());
    }

    @Bean
    @Scope("prototype")
    public DbxClientV2 dbxClient() {
        return new DbxClientV2(dbxRequestConfig, credentialLoader.getCredential().accessToken());
    }

    @Bean
    public DbxWebAuth dbxWebAuth() {
        return new DbxWebAuth(dbxRequestConfig, dbxAppInfo);
    }
}
