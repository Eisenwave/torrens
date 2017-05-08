package net.grian.torrens.img.trans;

import net.grian.torrens.img.Texture;

public class TSNearestNeighbour extends TextureScale {
    
    public TSNearestNeighbour(Texture in, int w1, int h1) {
        super(in, w1, h1);
    }
    
    @Override
    public Texture apply() {
        if (w0 == w1 && h0 == h1) {
            return out = in.clone();
        }
        
        out = Texture.alloc(w1, h1);
        
        for (int x = 0; x < w1; x++) for (int y = 0; y < h1; y++) {
            final int rgb = in.get(
                x * w0 / w1,
                y * h0 / h1);
            out.set(x, y, rgb);
        }
        
        return out;
    }
    
    @Override
    public Texture applyX() {
        if (w0 == w1)
            return out = in.clone();
    
        out = Texture.alloc(w1, h1);
    
        for (int x = 0; x < w1; x++) for (int y = 0; y < h1; y++) {
            final int rgb = in.get(x * w0 / w1, y);
            out.set(x, y, rgb);
        }
        
        return out;
    }
    
    @Override
    public Texture applyY() {
        if (h0 == h1)
            return out = in.clone();
        
        out = Texture.alloc(w1, h1);
    
        for (int x = 0; x < w1; x++) for (int y = 0; y < h1; y++) {
            final int rgb = in.get(x, y * h0 / h1);
            out.set(x, y, rgb);
        }
        
        return out;
    }
}
