package net.grian.torrens.util.io;

import net.grian.torrens.util.util.Resources;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;

/**
 * Throwaway object only meant to perform one deserialization of a stream.
 *<p>
 *     The deserializer interface is designed as a utility interface, giving each implementation several methods for
 *     reading from URL's, files, byte arrays and more.
 * </p>
 * <p>
 *     The only method to be implemented is {@link #fromStream(InputStream)} which reads an object from a generic
 *     input stream.
 * </p>
 *
 * @param <T> the type of object which is to be deserialized
 */
public interface Deserializer<T> {

    /**
     * Deserializes an object from an {@link InputStream}.
     *
     * @param stream the stream
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @NotNull
    public abstract T fromStream(InputStream stream) throws IOException;

    /**
     * Deserializes an object from a {@link File} using a {@link FileInputStream}.
     *
     * @param file the file
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @NotNull
    public default T fromFile(File file) throws IOException {
        try (InputStream stream = new FileInputStream(file)) {
            try (BufferedInputStream buffStream = new BufferedInputStream(stream)) {
                return fromStream(buffStream);
            }
        }
    }
    
    /**
     * Deserializes an object from a {@link RandomAccessFile}.
     *
     * @param file the random access file
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @NotNull
    public default T fromFile(RandomAccessFile file) throws IOException {
        RandomAccessFileInputStream stream = new RandomAccessFileInputStream(file);
        return fromStream(stream);
    }

    /**
     * Deserializes an object from a {@code byte[]} using a {@link ByteArrayInputStream}.
     *
     * @param bytes the byte array
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @NotNull
    public default T fromBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return fromStream(stream);
    }

    /**
     * Deserializes an object from a {@link Class} and a resource path by opening a stream to the resource via the
     * {@link ClassLoader}.
     *
     * @param clazz the class which's class loader is to be used
     * @param resPath the path to the resource
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @NotNull
    public default T fromResource(Class<?> clazz, String resPath) throws IOException {
        try (InputStream stream = Resources.getStream(clazz, resPath)) {
            return fromStream(stream);
        }
    }

    /**
     * Deserializes an object from a {@link URL} by opening a stream to it.
     *
     * @param url the url
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    @NotNull
    public default T fromURL(URL url) throws IOException {
        try (InputStream stream = url.openStream()) {
            return fromStream(stream);
        }
    }
    
    @NotNull
    public default T fromURL(String url) throws IOException {
       return fromURL(new URL(url));
    }
    
}