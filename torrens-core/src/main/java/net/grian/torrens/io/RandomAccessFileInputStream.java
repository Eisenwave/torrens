package net.grian.torrens.util.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

public class RandomAccessFileInputStream extends InputStream {
    
    private final RandomAccessFile file;
    
    public RandomAccessFileInputStream(RandomAccessFile file) {
        this.file = Objects.requireNonNull(file);
    }
    
    @Override
    public int read() throws IOException {
        return file.read();
    }
    
    @Override
    public int read(@NotNull byte[] b) throws IOException {
        return file.read(b);
    }
    
    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        return file.read(b, off, len);
    }
    
    @Override
    public long skip(long n) throws IOException {
        return file.skipBytes((int) n);
    }
    
    @Override
    public void close() throws IOException {
        file.close();
    }
    
}
