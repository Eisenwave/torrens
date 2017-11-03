package net.grian.torrens.util.img;

import net.grian.torrens.error.FileFormatException;
import net.grian.torrens.io.Serializer;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 *     A serializer for <b>Joint Photographic Experts Group (.jpeg / .jpg)</b> files.
 * </p>
 * <p>
 *     No version restrictions exist.
 * </p>
 */
public class SerializerJPEG implements Serializer<RenderedImage> {

    @Override
    public void toStream(RenderedImage image, OutputStream stream) throws IOException {
        if (!ImageIO.write(image, "jpg", stream))
            throw new FileFormatException("can not write jpg's");
    }
    
    @Override
    public void toFile(RenderedImage image, File file) throws IOException {
        if (!ImageIO.write(image, "jpg", file))
            throw new FileFormatException("can not write jpg's");
    }

}
