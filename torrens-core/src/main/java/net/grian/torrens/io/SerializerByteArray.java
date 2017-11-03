package net.grian.torrens.util.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class SerializerByteArray implements Serializer<byte[]> {
    
    @Override
    public void toStream(byte[] object, OutputStream stream) throws IOException {
        stream.write(object);
    }
    
    @NotNull
    @Override
    public byte[] toBytes(byte[] object) throws IOException {
        return Arrays.copyOf(object, object.length);
    }
    
    @NotNull
    @Override
    public byte[] toBytes(byte[] object, int capacity) throws IOException {
        return Arrays.copyOf(object, object.length);
    }
    
}
