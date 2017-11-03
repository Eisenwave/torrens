package net.grian.torrens.util.img.div;

public interface BooleanTexture {
    
    abstract int getWidth();
    
    abstract int getHeight();
    
    abstract boolean contains(int x, int y);
    
}
