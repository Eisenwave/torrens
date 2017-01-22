package net.grian.torrens.io;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
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
        ImageIO.write(image, "png", stream);
    }

}
