package net.grian.torrens.error;

import java.io.IOException;

/**
 * Thrown when a deserialized file has an invalid format.
 */
public class FileFormatException extends IOException {

    public FileFormatException() {
        super();
    }

    public FileFormatException(String message) {
        super(message);
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileFormatException(Throwable cause) {
        super(cause);
    }

}
