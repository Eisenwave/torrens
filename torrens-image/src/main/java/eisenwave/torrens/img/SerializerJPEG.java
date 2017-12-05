package eisenwave.torrens.img;

import eisenwave.torrens.error.FileFormatException;
import eisenwave.torrens.io.Serializer;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * A serializer for <b>Joint Photographic Experts Group (.jpeg / .jpg)</b> files.
 * <p>
 * No version restrictions exist.
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
