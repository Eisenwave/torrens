package net.grian.torrens.io;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
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
        ImageIO.write(image, "jpeg", stream);
    }

}
