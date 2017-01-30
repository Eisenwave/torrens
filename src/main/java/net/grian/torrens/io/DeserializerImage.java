package net.grian.torrens.io;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *     A deserializer for images of type:<ul>
 *         <li><b>Joint Photographic Experts Group (.jpeg / .jpg)</b></li>
 *         <li><b>Portable Network Graphics (.png)</b></li>
 *         <li><b>Bitmap (.bmp)</b></li>
 *         <li><b>Wireless Application Protocol Bitmap Format (.wbmp)</b></li>
 *         <li><b>Graphics Interchange Format (.gif)</b></li>
 *     </ul>
 * </p>
 * <p>
 *     No version restrictions exist.
 * </p>
 */
public class DeserializerImage implements Deserializer<BufferedImage> {

    @Override
    public BufferedImage fromStream(InputStream stream) throws IOException {
        return ImageIO.read(stream);
    }

}
