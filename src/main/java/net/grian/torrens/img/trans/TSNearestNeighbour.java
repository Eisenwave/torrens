package net.grian.torrens.img.trans;

import net.grian.torrens.img.Texture;

public class TSNearestNeighbour implements TextureScale {
    
    @Override
    public Texture apply(Texture texture, int w1, int h1) {
        final int
            w0 = texture.getWidth(),
            h0 = texture.getHeight();
        if (w0 == w1 && h0 == h1)
            return texture.clone();
        
        Texture result = Texture.alloc(w1, h1);
        
        for (int x1 = 0; x1 < w1; x1++) for (int y1 = 0; y1 < h1; y1++) {
            final int rgb = texture.get(
                x1 * w0 / w1,
                y1 * h0 / h1);
            result.set(x1, y1, rgb);
        }
        
        return result;
    }
    
    @SuppressWarnings("Duplicates")
    @Override
    public Texture applyX(Texture texture, int w1) {
        final int
            w0 = texture.getWidth(),
            h = texture.getHeight();
        if (w0 == w1)
            return texture.clone();
        
        Texture result = Texture.alloc(w1, h);
    
        for (int x1 = 0; x1 < w1; x1++) for (int y = 0; y < h; y++) {
            final int rgb = texture.get(x1 * w0 / w1, y);
            result.set(x1, y, rgb);
        }
    
        return result;
    }
    
    @SuppressWarnings("Duplicates")
    @Override
    public Texture applyY(Texture texture, int h1) {
        final int
            w = texture.getWidth(),
            h0 = texture.getHeight();
        if (h0 == h1)
            return texture.clone();
        
        Texture result = Texture.alloc(w, h1);
    
        for (int x = 0; x < w; x++) for (int y1 = 0; y1 < h1; y1++) {
            final int rgb = texture.get(x, y1 * h0 / h1);
            result.set(x, y1, rgb);
        }
    
        return result;
    }
}
