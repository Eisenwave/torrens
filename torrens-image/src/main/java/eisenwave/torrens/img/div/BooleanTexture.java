package eisenwave.torrens.img.div;

public interface BooleanTexture {
    
    abstract int getWidth();
    
    abstract int getHeight();
    
    abstract boolean contains(int x, int y);
    
}
