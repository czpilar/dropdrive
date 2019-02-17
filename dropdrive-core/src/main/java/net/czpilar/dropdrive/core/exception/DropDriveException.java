package net.czpilar.dropdrive.core.exception;

/**
 * Base exception for all dropDrive exceptions.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DropDriveException extends RuntimeException {

    public DropDriveException(String message) {
        super(message);
    }

    public DropDriveException(String message, Throwable cause) {
        super(message, cause);
    }

    public DropDriveException(Throwable cause) {
        super(cause);
    }
}
