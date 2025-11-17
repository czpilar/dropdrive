package net.czpilar.dropdrive.cmd.context;

import net.czpilar.dropdrive.cmd.credential.PropertiesDropDriveCredential;
import net.czpilar.dropdrive.core.context.DropDriveCoreContext;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.help.HelpFormatter;
import org.springframework.context.annotation.*;

import static net.czpilar.dropdrive.cmd.runner.impl.DropDriveCmdRunner.*;

@Configuration
@ComponentScan(basePackages = "net.czpilar.dropdrive.cmd")
@Import(DropDriveCoreContext.class)
@PropertySource("classpath:dropdrive-core.properties")
public class DropDriveCmdContext {

    public static final String UPLOAD_DIR_PROPERTY_KEY = "dropdrive.uploadDir";
    public static final String ACCESS_TOKEN_PROPERTY_KEY = "dropdrive.accessToken";
    public static final String DEFAULT_UPLOAD_DIR = "dropdrive-uploads";

    @Bean
    public PropertiesDropDriveCredential propertiesDropDriveCredential() {
        return new PropertiesDropDriveCredential(UPLOAD_DIR_PROPERTY_KEY, ACCESS_TOKEN_PROPERTY_KEY, DEFAULT_UPLOAD_DIR);
    }

    @Bean
    public HelpFormatter helpFormatter() {
        return HelpFormatter.builder().setShowSince(false).get();
    }

    @Bean
    public DefaultParser defaultParser() {
        return new DefaultParser();
    }

    @Bean
    public Options options() {
        return new Options()
                .addOption(toOption(OPTION_VERSION, "show dropDrive version"))
                .addOption(toOption(OPTION_HELP, "show this help"))
                .addOption(toOption(OPTION_LINK, "display authorization link"))
                .addOption(toOption(OPTION_AUTHORIZATION, "process authorization", "code"))
                .addOption(toUnlimitedOption(toOption(OPTION_FILE, "upload file(s)", "file")))
                .addOption(toOption(OPTION_DIRECTORY, "directory for upload; creates new one if no directory exists; default is dropdrive-uploads", "dir"))
                .addOption(toOption(OPTION_PROPERTIES, "path to dropDrive properties file", "props"));
    }

    private Option toOption(String opt, String description) {
        return new Option(opt, description);
    }

    private Option toOption(String opt, String description, String argName) {
        Option option = new Option(opt, true, description);
        option.setArgName(argName);
        return option;
    }

    private Option toUnlimitedOption(Option option) {
        option.setArgs(Option.UNLIMITED_VALUES);
        return option;
    }
}
