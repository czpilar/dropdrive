package net.czpilar.dropdrive.cmd.runner.impl;

import com.dropbox.core.v2.files.FileMetadata;
import net.czpilar.dropdrive.cmd.credential.PropertiesDropDriveCredential;
import net.czpilar.dropdrive.cmd.exception.CommandLineException;
import net.czpilar.dropdrive.cmd.runner.IDropDriveCmdRunner;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.service.IAuthorizationService;
import net.czpilar.dropdrive.core.service.IFileService;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Command line runner implementation.
 *
 * @author David Pilar (david@czpilar.net)
 */
@Component
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

    private IFileService fileService;

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

    @Autowired
    public void setFileService(IFileService fileService) {
        this.fileService = fileService;
    }

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

    private void doAuthorizationOption(CommandLine cmd) {
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
            List<FileMetadata> files = fileService.uploadFiles(Arrays.asList(cmd.getOptionValues(OPTION_FILE)), dir);
            System.out.println("Uploaded " + files.size() + " file(s)...");
            for (FileMetadata file : files) {
                System.out.println("- " + file.getName() + " (remote revision: " + file.getRev() + ")");
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
        try {
            helpFormatter.printHelp(dropDriveSetting.getApplicationName(), null, options, null, true);
        } catch (IOException e) {
            throw new CommandLineException("Unable to print help!", e);
        }
    }

    @Override
    public void run(String[] args) {
        try {
            CommandLine cmd = parseCommandLine(args);
            doPropertiesOption(cmd);
            doVersionOption(cmd);
            doHelpOption(cmd);
            doLinkOption(cmd);
            doAuthorizationOption(cmd);
            doFileOption(cmd);
        } catch (CommandLineException e) {
            System.out.println(e.getMessage() + "\n");
            printCommandLine();
        }
    }

}
