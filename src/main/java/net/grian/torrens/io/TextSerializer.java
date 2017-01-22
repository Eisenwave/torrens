package net.grian.torrens.io;

import java.io.*;

public interface TextSerializer<T> extends Serializer<T> {

    /**
     * Writes the object into a {@link Writer}.
     *
     * @param object the object
     * @param writer the writer
     * @throws IOException if an I/O error occurs
     */
    void toWriter(T object, Writer writer) throws IOException;

    /**
     * Writes the object into an {@link OutputStream} using an {@link OutputStreamWriter}.
     *
     * @param object the object
     * @param stream the stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    default void toStream(T object, OutputStream stream) throws IOException {
        try (Writer writer = new OutputStreamWriter(stream)) {
            toWriter(object, writer);
        }
    }

    /**
     * Writes the object into a {@link File} using an {@link FileWriter}.
     *
     * @param object the object
     * @param file the file
     * @throws IOException if an I/O error occurs
     */
    @Override
    default void toFile(T object, File file) throws IOException {
        try (Writer writer = new FileWriter(file)) {
            toWriter(object, writer);
        }
    }

    /**
     * Writes the object into a {@link String} using a {@link StringWriter}.
     *
     * @param object the object
     * @throws IOException if an I/O error occurs
     */
    default String toString(T object) throws IOException {
        Writer writer = new StringWriter();
        toWriter(object, writer);
        return writer.toString();
    }

}
