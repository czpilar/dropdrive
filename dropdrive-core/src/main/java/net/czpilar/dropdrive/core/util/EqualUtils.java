package net.czpilar.dropdrive.core.util;

import java.io.File;
import java.nio.file.Path;

import com.dropbox.core.DbxEntry;

/**
 * Equal utility class.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class EqualUtils {

    /**
     * Returns true if lengths are equal and remote last modified time is greater or equal to local file, otherwise returns false.
     *
     * @param file
     * @param pathToFile
     * @return
     */
    public static boolean equals(DbxEntry.File file, Path pathToFile) {
        boolean result = false;
        if (file != null && pathToFile != null) {
            File localFile = pathToFile.toFile();
            result = file.numBytes == localFile.length() && file.lastModified.getTime() >= localFile.lastModified();
        }
        return result;
    }

    /**
     * Returns true if lengths are not equal or remote last modified time is lower than local file, otherwise returns false.
     *
     * @param file
     * @param pathToFile
     * @return
     */
    public static boolean notEquals(DbxEntry.File file, Path pathToFile) {
        return !equals(file, pathToFile);
    }

}
