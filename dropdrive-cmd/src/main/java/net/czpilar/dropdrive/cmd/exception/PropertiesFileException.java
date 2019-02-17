package net.czpilar.dropdrive.cmd.exception;

import net.czpilar.dropdrive.core.exception.DropDriveException;

/**
 * Exception used for error during work with properties file.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class PropertiesFileException extends DropDriveException {

    public PropertiesFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
