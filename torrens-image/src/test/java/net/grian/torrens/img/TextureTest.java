package net.grian.torrens.util.img;

import net.grian.torrens.img.DeserializerImage;
import net.grian.torrens.img.SerializerPNG;
import net.grian.torrens.img.Texture;
import net.grian.torrens.util.ColorMath;
import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

public class TextureTest {
    
    private final static File DEBUG_FILE = new File("F:/Porn/TextureTest.png");
    
    public static Texture noiseSquare(int size) {
        Texture texture = Texture.alloc(size, size);
        texture.forEachPosition((x,y) -> texture.set(x, y, ColorMath.random(true)));
        return texture;
    }
    
    @Test
    public void match() throws Exception {
        Texture t = noiseSquare(128);
        assertEquals(1F, t.match(t, true), 0);
    }
    
    @Test
    public void wrap() throws Exception {
        final int width = 2, height = 2;
        
        final int[] back = new int[width * height];
        Texture a = Texture.wrap(back, width, height);
        Texture b = Texture.wrap(back, width, height);
        
        Assert.assertTrue(a.equals(b));
        
        back[3] = ColorMath.fromRGB(16, 16, 16);
        assertEquals(back[3], a.get(1, 1));
        assertEquals(back[3], b.get(1, 1));
    
        Assert.assertTrue(a.equals(b));
    }
    
    @Test
    public void getImageWrapper() throws Exception {
        final int width = 32, height = 32;
        
        Texture texture = Texture.alloc(width, height);
        BufferedImage wrapper = texture.getImageWrapper();
    
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
            final int argb = ColorMath.random(true);
            
            assertNotEquals(argb, texture.get(x, y));
            Assert.assertNotEquals(argb, wrapper.getRGB(x, y));
    
            wrapper.setRGB(x, y, argb);
            
            assertEquals(argb, texture.get(x, y));
            Assert.assertEquals(argb, wrapper.getRGB(x, y));
        }
    }
    
    @Test
    public void average_RGB_HSB() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture t = Texture.wrapOrCopy(img);
        
        final int rgb = t.averageRGB(false);
        final float[] hsb = t.averageHSB();
    
        final int rgb2 = ColorMath.fromHSB(hsb[0], hsb[1], hsb[2]);
        final float[] hsb2 = ColorMath.hsb(rgb);
    
        System.out.println(new Color(rgb));
        System.out.println(new Color(rgb2));
        System.out.println(Arrays.toString(hsb));
        System.out.println(Arrays.toString(hsb2));
    }
    
    @Test
    public void averageRGB_ofNoiseIsGray() throws Exception {
        Texture t = noiseSquare(64);
        int avg = t.averageRGB(false);
        int gray = ColorMath.fromRGB(127, 127, 127);
        Assert.assertTrue(ColorMath.componentDiff(avg, gray, false) < 32);
    }
    
    @Test
    public void forEdge() throws Exception {
        Texture t = Texture.alloc(32, 32);
        t.forEdge((x,y) -> t.set(x, y, ColorMath.DEBUG1));
        
        if (DEBUG_FILE.canWrite())
            new SerializerPNG().toFile(t.getImageWrapper(), DEBUG_FILE);
    }
    
}