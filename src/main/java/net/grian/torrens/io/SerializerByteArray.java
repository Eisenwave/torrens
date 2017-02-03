package net.grian.torrens.io;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

public class SerializerByteArray implements Serializer<byte[]> {
    
    @Override
    public void toStream(byte[] object, OutputStream stream) throws IOException {
        stream.write(object);
    }
    
    @Override
    public byte[] toBytes(byte[] object) throws IOException {
        return Arrays.copyOf(object, object.length);
    }
    
    @Override
    public byte[] toBytes(byte[] object, int capacity) throws IOException {
        return Arrays.copyOf(object, object.length);
    }
    
}
