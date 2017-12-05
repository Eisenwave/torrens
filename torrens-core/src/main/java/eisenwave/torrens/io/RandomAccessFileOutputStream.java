package eisenwave.torrens.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

public class RandomAccessFileOutputStream extends OutputStream {
    
    private final RandomAccessFile file;
    
    public RandomAccessFileOutputStream(RandomAccessFile file) {
        this.file = Objects.requireNonNull(file);
    }
    
    @Override
    public void write(int b) throws IOException {
        file.write(b);
    }
    
    @Override
    public void write(@NotNull byte[] b) throws IOException {
        file.write(b);
    }
    
    @Override
    public void write(@NotNull byte[] b, int off, int len) throws IOException {
        file.write(b, off, len);
    }
    
    @Override
    public void close() throws IOException {
        file.close();
    }
    
}
