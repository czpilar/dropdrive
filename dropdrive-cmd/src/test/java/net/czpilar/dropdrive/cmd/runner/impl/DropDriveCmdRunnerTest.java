package net.czpilar.dropdrive.cmd.runner.impl;

import com.dropbox.core.v2.files.FileMetadata;
import net.czpilar.dropdrive.cmd.credential.PropertiesDropDriveCredential;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.service.IAuthorizationService;
import net.czpilar.dropdrive.core.service.IFileService;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.apache.commons.cli.*;
import org.apache.commons.cli.help.HelpFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveCmdRunnerTest {

    private final DropDriveCmdRunner runner = new DropDriveCmdRunner();
    @Mock
    private CommandLineParser commandLineParser;
    @Mock
    private Options options;
    @Mock
    private HelpFormatter helpFormatter;
    @Mock
    private IAuthorizationService authorizationService;
    @Mock
    private IFileService fileService;
    @Mock
    private DropDriveSetting dropDriveSetting;
    @Mock
    private PropertiesDropDriveCredential propertiesDropDriveCredential;
    @Mock
    private CommandLine commandLine;

    private AutoCloseable autoCloseable;

    @BeforeEach
    public void before() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        runner.setCommandLineParser(commandLineParser);
        runner.setOptions(options);
        runner.setHelpFormatter(helpFormatter);
        runner.setAuthorizationService(authorizationService);
        runner.setFileService(fileService);
        runner.setDropDriveSetting(dropDriveSetting);
        runner.setPropertiesDropDriveCredential(propertiesDropDriveCredential);
    }

    @AfterEach
    public void after() throws Exception {
        autoCloseable.close();
    }

    @Test
    public void testRunWhereCommandLineParsingFails() throws ParseException, IOException {
        String appName = "application-name";
        String[] args = {"arg1", "arg2"};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenThrow(ParseException.class);
        when(dropDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, null, options, null, true);
        verify(dropDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(propertiesDropDriveCredential);
        verifyNoInteractions(commandLine);
    }

    @Test
    public void testRunWhereCommandLineHasEmptyOptions() throws ParseException, IOException {
        String appName = "application-name";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(dropDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, null, options, null, true);
        verify(commandLine).getOptions();
        verify(dropDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(propertiesDropDriveCredential);
    }

    @Test
    public void testRunWhereCommandLineHasOnlyPropertiesOption() throws ParseException, IOException {
        String appName = "application-name";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(dropDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, null, options, null, true);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(dropDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
        verifyNoInteractions(propertiesDropDriveCredential);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndVersionOptions() throws ParseException {
        String appVersion = "application-version";
        String propertiesValue = "test-properties-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_VERSION)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(dropDriveSetting.getApplicationVersion()).thenReturn(appVersion);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(dropDriveSetting).getApplicationVersion();
        verify(propertiesDropDriveCredential).setPropertyFile(propertiesValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndHelpOptions() throws ParseException, IOException {
        String appName = "application-name";
        String propertiesValue = "test-properties-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_HELP)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(dropDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, null, options, null, true);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(dropDriveSetting).getApplicationName();
        verify(propertiesDropDriveCredential).setPropertyFile(propertiesValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndLinkOptions() throws ParseException {
        String propertiesValue = "test-properties-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_LINK)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(authorizationService.getAuthorizationURL()).thenReturn("test-authorization-url");

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(propertiesDropDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).getAuthorizationURL();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnNullCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-authorization-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(authorizationValue);
        when(authorizationService.authorize(authorizationValue)).thenReturn(null);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(propertiesDropDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).authorize(authorizationValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-authorization-value";
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_FILE)).thenReturn(false);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(authorizationValue);
        when(authorizationService.authorize(authorizationValue)).thenReturn(mock(Credential.class));

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(propertiesDropDriveCredential).setPropertyFile(propertiesValue);
        verify(authorizationService).authorize(authorizationValue);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);
        verifyNoMoreInteractions(authorizationService);

        verifyNoInteractions(options);
        verifyNoInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndNoDirectory() throws ParseException {
        String propertiesValue = "test-properties-value";
        String optionFile = "test-file-value";
        List<String> optionFiles = List.of(optionFile);
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_FILE)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(false);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValues(DropDriveCmdRunner.OPTION_FILE)).thenReturn(new String[]{optionFile});

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_DIRECTORY);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValues(DropDriveCmdRunner.OPTION_FILE);
        verify(propertiesDropDriveCredential).setPropertyFile(propertiesValue);
        verify(fileService).uploadFiles(optionFiles, (String) null);

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);
        verifyNoMoreInteractions(fileService);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(authorizationService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndDirectory() throws ParseException {
        String propertiesValue = "test-properties-value";
        String optionFile = "test-file-value";
        String optionDirectory = "test-directory";
        List<String> optionFiles = List.of(optionFile);
        String[] args = {"arg1", "arg2"};
        Option[] optionList = {mock(Option.class), mock(Option.class)};
        FileMetadata file1 = mock(FileMetadata.class);
        FileMetadata file2 = mock(FileMetadata.class);
        List<FileMetadata> files = Arrays.asList(file1, file2);
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_VERSION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_HELP)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_LINK)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION)).thenReturn(false);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_FILE)).thenReturn(true);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(true);
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(propertiesValue);
        when(commandLine.getOptionValues(DropDriveCmdRunner.OPTION_FILE)).thenReturn(new String[]{optionFile});
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(optionDirectory);
        when(fileService.uploadFiles(anyList(), anyString())).thenReturn(files);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_VERSION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_HELP);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_LINK);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_AUTHORIZATION);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_DIRECTORY);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(commandLine).getOptionValues(DropDriveCmdRunner.OPTION_FILE);
        verify(commandLine).getOptionValue(DropDriveCmdRunner.OPTION_DIRECTORY);
        verify(propertiesDropDriveCredential).setPropertyFile(propertiesValue);
        verify(fileService).uploadFiles(optionFiles, optionDirectory);
        verify(file1).getName();
        verify(file1).getRev();
        verify(file2).getName();
        verify(file2).getRev();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);
        verifyNoMoreInteractions(fileService);
        verifyNoMoreInteractions(file1, file2);

        verifyNoInteractions(options);
        verifyNoInteractions(authorizationService);
        verifyNoInteractions(authorizationService);
    }

}
