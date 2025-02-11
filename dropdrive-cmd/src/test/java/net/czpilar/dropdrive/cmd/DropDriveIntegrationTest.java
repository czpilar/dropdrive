package net.czpilar.dropdrive.cmd;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveIntegrationTest {

    private static final String PROPERTIES = "dropdrive.properties";

    @Test
    public void testNoArgs() {
        DropDrive.main(new String[]{});
    }

    @Test
    public void testHelp() {
        DropDrive.main(new String[]{"-h"});
    }

    @Test
    public void testVersion() {
        DropDrive.main(new String[]{"-v"});
    }

    @Test
    public void testProperties() {
        DropDrive.main(new String[]{"-p", PROPERTIES});
    }

    @Test
    public void testShowAuthLink() {
        DropDrive.main(new String[]{"-l", "-p", PROPERTIES});
    }

    @Test
    public void testAuthorize() {
        DropDrive.main(new String[]{"-a", "auth_code", "-p", PROPERTIES});
    }

    private void createFileIfNotExist(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            FileUtils.writeStringToFile(file, "This is a testing file with filename: " + filename, Charset.defaultCharset());
        }
    }

    @Test
    public void testUploadFiles() throws IOException {
        String filename1 = "target/test1.txt";
        String filename2 = "target/test2.txt";
        String filename3 = "target/test3.txt";
        createFileIfNotExist(filename1);
        createFileIfNotExist(filename2);
        createFileIfNotExist(filename3);
        DropDrive.main(new String[]{"-f", filename1, filename2, filename3, "-d", "dropdrive-test-backup", "-p", PROPERTIES});
    }

    @Test
    public void testUploadFileToSubdirectory() throws IOException {
        String filename = "target/test1.txt";
        createFileIfNotExist(filename);
        DropDrive.main(new String[]{"-f", filename, "-d", "dropdrive-test-backup/dropdrive-subdir/dropdrive-last-dir", "-p", PROPERTIES});
    }
}
