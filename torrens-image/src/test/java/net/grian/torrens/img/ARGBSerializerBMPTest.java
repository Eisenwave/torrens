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

public class ARGBSerializerBMPTest {
    
    private final static File DEBUG_FILE = new File("F:/Porn/SerializerBMP32Test.bmp");
    
    @Test
    public void toStream() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "bilinear.png");
        Texture texture = Texture.wrapOrCopy(img);
        
        long now = System.currentTimeMillis();
        byte[] bytes = new ARGBSerializerBMP(false).toBytes(texture);
        long time = System.currentTimeMillis() - now;
        System.out.println("serialized "+texture+" in "+time+"ms");
        
        assertTrue(isBMP(bytes));
        readBMP(bytes);
        
        if (DEBUG_FILE.exists()) {
            long now2 = System.currentTimeMillis();
            new SerializerByteArray().toFile(bytes, DEBUG_FILE);
            long time2 = System.currentTimeMillis() - now2;
            System.out.println("wrote "+bytes.length+" bytes to file in "+time2+"ms");
        }
    }
    
    @Test
    public void dataOf() throws Exception {
        assertEquals(4, ARGBSerializerBMP.lineLengthOf(1, 4));
        assertEquals(4, ARGBSerializerBMP.lineLengthOf(1, 3));
        
        for (int x = 0; x < 128; x++)
            assertTrue(ARGBSerializerBMP.lineLengthOf(x, 3) % 4 == 0);
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