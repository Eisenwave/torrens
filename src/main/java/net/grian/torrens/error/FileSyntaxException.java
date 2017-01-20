package net.grian.torrens.error;

import java.io.IOException;

/**
 * Thrown when a deserialized file does not follow the file syntax of its type.
 */
public class FileSyntaxException extends IOException {

    public FileSyntaxException() {
        super();
    }

    public FileSyntaxException(String message) {
        super(message);
    }

    public FileSyntaxException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileSyntaxException(Throwable cause) {
        super(cause);
    }

}
