package net.czpilar.dropdrive.core.exception;

/**
 * Exception used when file handling fails.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class FileHandleException extends DropDriveException {

    public FileHandleException(String message, Throwable cause) {
        super(message, cause);
    }
}
