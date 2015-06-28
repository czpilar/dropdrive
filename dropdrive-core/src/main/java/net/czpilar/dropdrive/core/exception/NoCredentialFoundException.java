package net.czpilar.dropdrive.core.exception;

/**
 * Exception used when no credential found.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class NoCredentialFoundException extends DropDriveException {

    public NoCredentialFoundException(String message) {
        super(message);
    }

    public NoCredentialFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCredentialFoundException(Throwable cause) {
        super(cause);
    }

}
