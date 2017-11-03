package net.grian.torrens.img.div;

import net.grian.torrens.object.Rectangle4i;

public interface TextureDivider {
    
    abstract Rectangle4i[] apply(BooleanTexture texture);
    
    
}
