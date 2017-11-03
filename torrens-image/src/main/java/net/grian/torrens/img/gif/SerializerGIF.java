package net.grian.torrens.util.img.gif;

import net.grian.torrens.error.FileFormatException;
import net.grian.torrens.io.Serializer;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class SerializerGIF implements Serializer<RenderedImage> {
    
    @Override
    public void toStream(RenderedImage image, OutputStream stream) throws IOException {
        if (!ImageIO.write(image, "gif", stream))
            throw new FileFormatException("can not write gif's");
    }
    
    @Override
    public void toFile(RenderedImage image, File file) throws IOException {
        if (!ImageIO.write(image, "gif", file))
            throw new FileFormatException("can not write gif's");
    }
    
}
