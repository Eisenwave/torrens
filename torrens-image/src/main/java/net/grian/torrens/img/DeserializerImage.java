package net.grian.torrens.img;

import net.grian.torrens.error.FileFormatException;
import net.grian.torrens.io.Deserializer;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * <p>
 * A deserializer for images of type:<ul>
 * <li><b>Joint Photographic Experts Group (.jpeg / .jpg)</b></li>
 * <li><b>Portable Network Graphics (.png)</b></li>
 * <li><b>Bitmap (.bmp)</b></li>
 * <li><b>Wireless Application Protocol Bitmap Format (.wbmp)</b></li>
 * <li><b>Graphics Interchange Format (.gif)</b></li>
 * </ul>
 * <p>
 * No version restrictions exist.
 */
public class DeserializerImage implements Deserializer<BufferedImage> {
    
    @NotNull
    @Override
    public BufferedImage fromStream(InputStream stream) throws IOException {
        BufferedImage result = ImageIO.read(stream);
        if (result == null)
            throw new FileFormatException("image could not be read");
        return result;
    }
    
    @NotNull
    @Override
    public BufferedImage fromFile(File file) throws IOException {
        BufferedImage result = ImageIO.read(file);
        if (result == null)
            throw new FileFormatException("image could not be read");
        return result;
    }
    
    @NotNull
    @Override
    public BufferedImage fromURL(URL url) throws IOException {
        BufferedImage result = ImageIO.read(url);
        if (result == null)
            throw new FileFormatException("image could not be read");
        return result;
    }
    
}
