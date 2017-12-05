package eisenwave.torrens.img.div;

import eisenwave.torrens.object.Rectangle4i;

public interface TextureDivider {
    
    abstract Rectangle4i[] apply(BooleanTexture texture);
    
}
