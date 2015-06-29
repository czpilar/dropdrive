package net.czpilar.dropdrive.cmd.runner.impl;

import java.util.Arrays;
import java.util.List;

import com.dropbox.core.DbxEntry;
import net.czpilar.dropdrive.cmd.credential.PropertiesDropDriveCredential;
import net.czpilar.dropdrive.core.credential.Credential;
import net.czpilar.dropdrive.core.service.IAuthorizationService;
import net.czpilar.dropdrive.core.service.IFileService;
import net.czpilar.dropdrive.core.setting.DropDriveSetting;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

/**
 * @author David Pilar (david@czpilar.net)
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ DbxEntry.File.class })
public class DropDriveCmdRunnerTest {

    private DropDriveCmdRunner runner = new DropDriveCmdRunner();
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

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
        runner.setCommandLineParser(commandLineParser);
        runner.setOptions(options);
        runner.setHelpFormatter(helpFormatter);
        runner.setAuthorizationService(authorizationService);
        runner.setFileService(fileService);
        runner.setDropDriveSetting(dropDriveSetting);
        runner.setPropertiesDropDriveCredential(propertiesDropDriveCredential);
    }

    @Test
    public void testRunWhereCommandLineParsingFails() throws ParseException {
        String appName = "application-name";
        String[] args = { "arg1", "arg2" };
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenThrow(ParseException.class);
        when(dropDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, options, true);
        verify(dropDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
        verifyZeroInteractions(propertiesDropDriveCredential);
        verifyZeroInteractions(commandLine);
    }

    @Test
    public void testRunWhereCommandLineHasEmptyOptions() throws ParseException {
        String appName = "application-name";
        String[] args = { "arg1", "arg2" };
        Option[] optionList = {};
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(dropDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, options, true);
        verify(commandLine).getOptions();
        verify(dropDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
        verifyZeroInteractions(propertiesDropDriveCredential);
    }

    @Test
    public void testRunWhereCommandLineHasOnlyPropertiesOption() throws ParseException {
        String appName = "application-name";
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class) };
        when(commandLineParser.parse(any(Options.class), any(String[].class))).thenReturn(commandLine);
        when(commandLine.getOptions()).thenReturn(optionList);
        when(commandLine.hasOption(DropDriveCmdRunner.OPTION_PROPERTIES)).thenReturn(true);
        when(dropDriveSetting.getApplicationName()).thenReturn(appName);

        runner.run(args);

        verify(commandLineParser).parse(options, args);
        verify(helpFormatter).printHelp(appName, options, true);
        verify(commandLine).getOptions();
        verify(commandLine).hasOption(DropDriveCmdRunner.OPTION_PROPERTIES);
        verify(dropDriveSetting).getApplicationName();

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
        verifyZeroInteractions(propertiesDropDriveCredential);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndVersionOptions() throws ParseException {
        String appVersion = "application-version";
        String propertiesValue = "test-properties-value";
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class), mock(Option.class) };
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

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndHelpOptions() throws ParseException {
        String appName = "application-name";
        String propertiesValue = "test-properties-value";
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class), mock(Option.class) };
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
        verify(helpFormatter).printHelp(appName, options, true);
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

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndLinkOptions() throws ParseException {
        String propertiesValue = "test-properties-value";
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class), mock(Option.class) };
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

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnNullCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-athorization-value";
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class), mock(Option.class) };
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

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndAuthorizationOptionsAndReturnCredential() throws ParseException {
        String propertiesValue = "test-properties-value";
        String authorizationValue = "test-athorization-value";
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class), mock(Option.class) };
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

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndNoDirectory() throws ParseException {
        String propertiesValue = "test-properties-value";
        String optionFile = "test-file-value";
        List<String> optionFiles = Arrays.asList(optionFile);
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class), mock(Option.class) };
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
        when(commandLine.getOptionValues(DropDriveCmdRunner.OPTION_FILE)).thenReturn(new String[] { optionFile });

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

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
        verifyZeroInteractions(authorizationService);
    }

    @Test
    public void testRunWhereCommandLineHasPropertiesAndFileOptionsAndDirectory() throws ParseException {
        String propertiesValue = "test-properties-value";
        String optionFile = "test-file-value";
        String optionDirectory = "test-directory";
        List<String> optionFiles = Arrays.asList(optionFile);
        String[] args = { "arg1", "arg2" };
        Option[] optionList = { mock(Option.class), mock(Option.class) };
        DbxEntry.File file1 = mock(DbxEntry.File.class);
        DbxEntry.File file2 = mock(DbxEntry.File.class);
        List<DbxEntry.File> files = Arrays.asList(file1, file2);
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
        when(commandLine.getOptionValues(DropDriveCmdRunner.OPTION_FILE)).thenReturn(new String[] { optionFile });
        when(commandLine.getOptionValue(DropDriveCmdRunner.OPTION_DIRECTORY)).thenReturn(optionDirectory);
        when(fileService.uploadFiles(anyListOf(String.class), anyString())).thenReturn(files);

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

        verifyNoMoreInteractions(commandLineParser);
        verifyNoMoreInteractions(helpFormatter);
        verifyNoMoreInteractions(dropDriveSetting);
        verifyNoMoreInteractions(commandLine);
        verifyNoMoreInteractions(propertiesDropDriveCredential);
        verifyNoMoreInteractions(file1, file2);

        verifyZeroInteractions(options);
        verifyZeroInteractions(authorizationService);
        verifyZeroInteractions(fileService);
        verifyZeroInteractions(authorizationService);
    }

}
