package net.czpilar.dropdrive.core.exception;

/**
 * Exception used when authorization fails.
 *
 * @author David Pilar (david@czpilar.net)
 */
public class AuthorizationFailedException extends DropDriveException {

    public AuthorizationFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
