package net.grian.torrens.io;

import java.io.*;

/**
 * Throwaway object only meant to perform one deserialization of a reader.
 *<p>
 *     The parser interface is designed as a utility interface which is an extension of the {@link Deserializer}
 *     interface specifically for readable files.
 * </p>
 * <p>
 *     The only method to be implemented is {@link #deserialize(Reader)} which reads an object from a generic reader.
 * </p>
 *
 * @param <T> the type of object which is to be parsed
 */
public interface Parser<T> extends Deserializer<T> {

    /**
     * Deserializes an object from a {@link Reader}.
     *
     * @param reader the reader
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    abstract T deserialize(Reader reader) throws IOException;

    /**
     * Deserializes an object from a {@link String} using a {@link StringReader}.
     *
     * @param str the string
     * @return the deserialized object
     * @throws IOException if the deserialization fails
     */
    default T deserialize(String str) throws IOException {
        Reader reader = new StringReader(str);
        T result = deserialize(reader);
        reader.close();
        return result;
    }

    @Override
    default T deserialize(InputStream stream) throws IOException {
        return deserialize(new InputStreamReader(stream));
    }

    @Override
    default T deserialize(File file) throws IOException {
        Reader reader = new FileReader(file);
        T result = deserialize(reader);
        reader.close();
        return result;
    }


}
