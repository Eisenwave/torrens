package eisenwave.torrens.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DeserializerByteArray implements Deserializer<byte[]> {
    
    @NotNull
    @Override
    public byte[] fromStream(InputStream stream) throws IOException {
        ByteBuffer result = ByteBuffer.allocate(4096);
        
        byte[] buffer = new byte[1024];
        for (int read; (read = stream.read(buffer)) != -1;)
            result.put(buffer, 0, read);
        
        return Arrays.copyOfRange(result.array(), 0, result.position());
    }
    
    @NotNull
    @Override
    public byte[] fromBytes(byte[] bytes) throws IOException {
        return bytes;
    }
    
}
