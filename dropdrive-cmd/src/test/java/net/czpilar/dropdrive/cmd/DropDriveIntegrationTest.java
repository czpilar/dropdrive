package net.czpilar.dropdrive.cmd;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

/**
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveIntegrationTest {

    private static final String PROPERTIES = "dropdrive.properties";

    @Test
    public void testNoArgs() {
        DropDrive.main(new String[] {});
    }

    @Test
    public void testHelp() {
        DropDrive.main(new String[] { "-h" });
    }

    @Test
    public void testVersion() {
        DropDrive.main(new String[] { "-v" });
    }

    @Test
    public void testProperties() {
        DropDrive.main(new String[] { "-p", PROPERTIES });
    }

    @Test
    public void testShowAuthLink() {
        DropDrive.main(new String[] { "-l", "-p", PROPERTIES });
    }

    @Test
    public void testAuthorize() {
        DropDrive.main(new String[] { "-a", "6Vw_ImSRc-sAAAAAAAIJvlAxVYMDOVJfhJ0qG0bwhhI", "-p", PROPERTIES });
    }

    private void createFileIfNotExist(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            FileUtils.writeStringToFile(file, "This is a testing file with filename: " + filename);
        }
    }

    @Test
    public void testUploadFiles() throws IOException {
        String filename1 = "test1.txt";
        String filename2 = "test2.txt";
        String filename3 = "test3.txt";
        createFileIfNotExist(filename1);
        createFileIfNotExist(filename2);
        createFileIfNotExist(filename3);
        DropDrive.main(new String[] { "-f", filename1, filename2, filename3, "-d", "dropdrive-test-backup", "-p", PROPERTIES });
    }

    @Test
    public void testUploadFileToSubdirectory() throws IOException {
        String filename = "test1.txt";
        createFileIfNotExist(filename);
        DropDrive.main(new String[] { "-f", filename, "-d", "dropdrive-test-backup/dropdrive-subdir/dropdrive-lastdir", "-p", PROPERTIES });
    }
}
