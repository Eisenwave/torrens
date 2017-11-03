package net.grian.torrens.error;

import java.io.IOException;

/**
 * Thrown when a deserialized file has an unsupported version.
 */
public class FileVersionException extends IOException {

    public FileVersionException() {
        super();
    }

    public FileVersionException(String message) {
        super(message);
    }

    public FileVersionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileVersionException(Throwable cause) {
        super(cause);
    }

}
