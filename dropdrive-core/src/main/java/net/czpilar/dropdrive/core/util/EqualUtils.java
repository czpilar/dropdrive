package net.czpilar.dropdrive.core.util;

import com.dropbox.core.v2.files.FileMetadata;

import java.io.File;
import java.nio.file.Path;

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
    public static boolean equals(FileMetadata file, Path pathToFile) {
        boolean result = false;
        if (file != null && pathToFile != null) {
            File localFile = pathToFile.toFile();
            if (localFile.exists()) {
                result = file.getSize() == localFile.length()
                        && toSeconds(file.getClientModified().getTime()) >= toSeconds(localFile.lastModified());
            }
        }
        return result;
    }

    /**
     * This method strips milliseconds and returns seconds.
     *
     * @param milliseconds
     * @return
     */
    private static long toSeconds(long milliseconds) {
        return milliseconds / 1000;
    }

    /**
     * Returns true if lengths are not equal or remote last modified time is lower than local file, otherwise returns false.
     *
     * @param file
     * @param pathToFile
     * @return
     */
    public static boolean notEquals(FileMetadata file, Path pathToFile) {
        return !equals(file, pathToFile);
    }

}
