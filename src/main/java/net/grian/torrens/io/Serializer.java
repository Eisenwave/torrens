package net.grian.torrens.io;

import net.grian.torrens.util.Resources;

import java.io.*;

/**
 * Throwaway object for writing objects to files.
 * <p>
 *     The serializer interface is designed as a utility interface, giving each implementation several methods for
 *     serializing to URL's, files, byte arrays and more.
 * </p>
 * <p>
 *     The only method to be implemented is {@link #serialize(Object, OutputStream)} which writes an object into
 *     a generic output stream.
 * </p>
 *
 * @param <T> the type of object which is to be serialized
 */
public interface Serializer<T> {

    /**
     * Writes the object into a {@link OutputStream}.
     *
     * @param object the object
     * @param stream the output stream
     * @throws IOException if an I/O error occurs
     */
    public void serialize(T object, OutputStream stream) throws IOException;

    /**
     * Writes the object into a {@link File} using a {@link FileOutputStream}.
     *
     * @param object the object
     * @param file the file
     * @throws IOException if an I/O error occurs
     */
    public default void serialize(T object, File file) throws IOException {
        FileOutputStream stream = new FileOutputStream(file);
        serialize(object, stream);
        stream.close();
    }

    /**
     * Writes the object into bytes {@code byte[]} using a {@link ByteArrayOutputStream} with set capacity.
     *
     * @param object the object
     * @param capacity the byte buffer capacity used by the stream
     * @return a byte array containing the serialized object
     * @throws IOException if an I/O error occurs
     * @see ByteArrayOutputStream#ByteArrayOutputStream(int)
     */
    public default byte[] serialize(T object, int capacity) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream(capacity);
        serialize(object, stream);
        return stream.toByteArray();
    }

    /**
     * Writes the object into bytes {@code byte[]} using a {@link ByteArrayOutputStream} with unset capacity.
     *
     * @param object the object
     * @return a byte array containing the serialized object
     * @throws IOException if an I/O error occurs
     * @see ByteArrayOutputStream#ByteArrayOutputStream()
     */
    public default byte[] serialize(T object) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        serialize(object, stream);
        return stream.toByteArray();
    }

    /**
     * Writes the object into a resource by opening a stream to it.
     *
     * @param object the object
     * @param clazz the class
     * @param resPath the resource path
     * @throws IOException if an I/O error occurs
     */
    public default void serialize(T object, Class<?> clazz, String resPath) throws IOException {
        serialize(object, Resources.getFile(clazz, resPath));
    }

}
