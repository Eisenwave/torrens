package net.grian.torrens.util.io;

import org.jetbrains.annotations.NotNull;

import java.io.OutputStream;

public class VoidOutputStream extends OutputStream {
    
    @Override
    public void write(int b) {}
    
    @Override
    public void write(@NotNull byte[] b) {}
    
    @Override
    public void write(@NotNull byte[] b, int off, int len) {}
    
    @Override
    public void flush() {}
    
    @Override
    public void close() {}
    
}
