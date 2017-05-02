package net.grian.torrens.img.trans;

import net.grian.torrens.img.Texture;

public interface TextureScale {
    
    default Texture apply(Texture texture, int width, int height) {
        return applyY(applyX(texture, width), height);
    }
    
    abstract Texture applyX(Texture texture, int width);
    
    abstract Texture applyY(Texture texture, int height);
    
}
