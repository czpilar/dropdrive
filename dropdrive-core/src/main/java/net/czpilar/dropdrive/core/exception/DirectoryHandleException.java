package net.czpilar.dropdrive.core.exception;

/**
 * Exception used when directory handling fails.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class DirectoryHandleException extends DropDriveException {

    public DirectoryHandleException(String message) {
        super(message);
    }

    public DirectoryHandleException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirectoryHandleException(Throwable cause) {
        super(cause);
    }
}
