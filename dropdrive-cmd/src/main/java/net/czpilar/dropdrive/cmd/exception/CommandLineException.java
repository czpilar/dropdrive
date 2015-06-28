package net.czpilar.dropdrive.cmd.exception;

import net.czpilar.dropdrive.core.exception.DropDriveException;

/**
 * Exception used for error during work with command line.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class CommandLineException extends DropDriveException {

    public CommandLineException(String message) {
        super(message);
    }

    public CommandLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandLineException(Throwable cause) {
        super(cause);
    }

}
