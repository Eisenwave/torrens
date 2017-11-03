package net.grian.torrens.util.img.gif;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class GIFHeader implements Serializable, Cloneable {
    
    private int width, height;
    private boolean loop;
    private GIFColorTable table;
    
    public GIFHeader(int width, int height, @Nullable GIFColorTable table) {
        setWidth(width);
        setHeight(height);
        setColorTable(table);
    }
    
    public GIFHeader(int width, int height) {
        this(width, height, null);
    }
    
    public GIFHeader(GIFHeader copyOf, int width, int height) {
        setWidth(width);
        setHeight(height);
        setColorTable(copyOf.table);
    }
    
    public GIFHeader(GIFHeader copyOf) {
        this(copyOf, copyOf.width, copyOf.height);
    }
    
    // GETTERS
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public boolean isLoop() {
        return loop;
    }
    
    @Nullable
    public GIFColorTable getColorTable() {
        return table;
    }
    
    // SETTERS
    
    public void setWidth(int width) {
        if (width < 0) throw new IllegalArgumentException("width must be positive");
        this.width = width;
    }
    
    public void setHeight(int height) {
        if (height < 0) throw new IllegalArgumentException("height must be positive");
        this.height = height;
    }
    
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    public void setColorTable(@Nullable GIFColorTable table) {
        this.table = table;
    }
    
    // MISC
    
    @Override
    public GIFHeader clone() {
        return new GIFHeader(this);
    }
    
    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder
            .append("{width=")
            .append(width)
            .append(" ,height=")
            .append(height)
            .append(", table=")
            .append(table);
        
        return builder.toString();
    }
    
}
