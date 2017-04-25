package net.grian.torrens.img;

import com.sun.imageio.plugins.bmp.BMPImageReader;
import net.grian.torrens.io.SerializerByteArray;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class SerializerBMP32Test {
    
    private final static File DEBUG_FILE = new File("F:/Porn/SerializerBMP32Test.bmp");
    
    @Test
    public void toStream() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        byte[] bytes = new SerializerBMP32().toBytes(Texture.wrapOrCopy(img));
        assertTrue(isBMP(bytes));
        readBMP(bytes);
        
        if (DEBUG_FILE.exists())
            new SerializerByteArray().toFile(bytes, DEBUG_FILE);
    }
    
    private static void readBMP(byte[] bytes) throws IOException {
        ImageInputStream input = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes));
        BMPImageReader reader = (BMPImageReader) ImageIO.getImageReadersByFormatName("bmp").next();
        reader.setInput(input);
        
        reader.read(0);
    }
    
    private static boolean isBMP(byte[] bytes) {
        return bytes[0] == 0x42 && bytes[1] == 0x4d;
    }
    
}