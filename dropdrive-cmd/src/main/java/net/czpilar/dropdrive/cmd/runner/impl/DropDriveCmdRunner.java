package net.czpilar.dropdrive.cmd.runner.impl;

import java.util.Arrays;
import java.util.List;

import com.dropbox.core.DbxEntry;
import net.czpilar.dropdrive.cmd.credential.PropertiesDropDriveCredential;
import net.czpilar.dropdrive.cmd.exception.CommandLineException;
import net.czpilar.dropdrive.cmd.runner.IDropDriveCmdRunner;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.service.IAuthorizationService;
import net.czpilar.dropdrive.core.service.impl.FileService;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Command line runner implementation.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveCmdRunner implements IDropDriveCmdRunner {

    public static final String OPTION_FILE = "f";
    public static final String OPTION_LINK = "l";
    public static final String OPTION_AUTHORIZATION = "a";
    public static final String OPTION_DIRECTORY = "d";
    public static final String OPTION_PROPERTIES = "p";
    public static final String OPTION_HELP = "h";
    public static final String OPTION_VERSION = "v";

    private CommandLineParser commandLineParser;

    private Options options;

    private HelpFormatter helpFormatter;

    private IAuthorizationService authorizationService;

    @Autowired
    private FileService fileService;
//    private IFileService fileService;

    private DropDriveSetting dropDriveSetting;

    private PropertiesDropDriveCredential propertiesDropDriveCredential;

    @Autowired
    public void setCommandLineParser(CommandLineParser commandLineParser) {
        this.commandLineParser = commandLineParser;
    }

    @Autowired
    public void setOptions(Options options) {
        this.options = options;
    }

    @Autowired
    public void setHelpFormatter(HelpFormatter helpFormatter) {
        this.helpFormatter = helpFormatter;
    }

    @Autowired
    public void setAuthorizationService(IAuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

//    @Autowired
//    public void setFileService(IFileService fileService) {
//        this.fileService = fileService;
//    }

    @Autowired
    public void setDropDriveSetting(DropDriveSetting dropDriveSetting) {
        this.dropDriveSetting = dropDriveSetting;
    }

    @Autowired
    public void setPropertiesDropDriveCredential(PropertiesDropDriveCredential propertiesDropDriveCredential) {
        this.propertiesDropDriveCredential = propertiesDropDriveCredential;
    }

    private void doPropertiesOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_PROPERTIES)) {
            propertiesDropDriveCredential.setPropertyFile(cmd.getOptionValue(OPTION_PROPERTIES));
        }
    }

    private void doVersionOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_VERSION)) {
            System.out.println("dropDrive version: " + dropDriveSetting.getApplicationVersion());
        }
    }

    private void doHelpOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_HELP)) {
            printCommandLine();
        }
    }

    private void doLinkOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_LINK)) {
            System.out.println("Please authorize dropDrive application with following link:\n");
            System.out.println(authorizationService.getAuthorizationURL());
        }
    }

    private void doAuthrizationOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_AUTHORIZATION)) {
            Credential credential = authorizationService.authorize(cmd.getOptionValue(OPTION_AUTHORIZATION));
            if (credential != null) {
                System.out.println("Authorization was successful.");
            }
        }
    }

    private void doFileOption(CommandLine cmd) {
        if (cmd.hasOption(OPTION_FILE)) {
            String dir = cmd.hasOption(OPTION_DIRECTORY) ? cmd.getOptionValue(OPTION_DIRECTORY) : null;
            List<DbxEntry.File> files = fileService.uploadFiles(Arrays.asList(cmd.getOptionValues(OPTION_FILE)), dir);
            System.out.println("Uploaded " + files.size() + " file(s)...");
            for (DbxEntry.File file : files) {
                System.out.println("- " + file.name + " (remote revision: " + file.rev + ")");
            }
        }
    }

    private CommandLine parseCommandLine(String[] args) {
        try {
            CommandLine cmd = commandLineParser.parse(options, args);
            int length = cmd.getOptions().length;
            if (length == 0) {
                throw new CommandLineException("No arguments passed!");
            } else if (length == 1 && cmd.hasOption(OPTION_PROPERTIES)) {
                throw new CommandLineException("Provide at least one more argument!");
            }
            return cmd;
        } catch (ParseException e) {
            throw new CommandLineException("Invalid arguments passed!", e);
        }
    }

    private void printCommandLine() {
        helpFormatter.printHelp(dropDriveSetting.getApplicationName(), options, true);
    }

    @Override
    public void run(String[] args) {
        try {
            CommandLine cmd = parseCommandLine(args);
            doPropertiesOption(cmd);
            doVersionOption(cmd);
            doHelpOption(cmd);
            doLinkOption(cmd);
            doAuthrizationOption(cmd);
            doFileOption(cmd);
        } catch (CommandLineException e) {
            System.out.println(e.getMessage() + "\n");
            printCommandLine();
        }
    }

}
