package net.grian.torrens.img;

import net.grian.torrens.error.FileFormatException;
import net.grian.torrens.io.Serializer;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 *     A serializer for <b>Portable Network Graphics (.png)</b> files.
 * </p>
 * <p>
 *     No version restrictions exist.
 * </p>
 */
public class SerializerPNG implements Serializer<RenderedImage> {

    @Override
    public void toStream(RenderedImage image, OutputStream stream) throws IOException {
        if (!ImageIO.write(image, "png", stream))
            throw new FileFormatException("can not write png's");
    }
    
    @Override
    public void toFile(RenderedImage image, File file) throws IOException {
        if (!ImageIO.write(image, "png", file))
            throw new FileFormatException("can not write png's");
    }
    
}
