package net.grian.torrens.img;

import net.grian.torrens.io.Serializer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public interface ARGBSerializer extends Serializer<BaseTexture> {
    
    @Override
    abstract void toStream(BaseTexture texture, OutputStream stream) throws IOException;
    
    default void toStream(BufferedImage img, OutputStream stream)throws IOException {
        toStream(Texture.wrapOrCopy(img), stream);
    }
    
    default void toFile(BufferedImage img, File file)throws IOException {
        toFile(Texture.wrapOrCopy(img), file);
    }
    
}
