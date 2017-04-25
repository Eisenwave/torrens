package net.grian.torrens.img;

import net.grian.spatium.util.ColorMath;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class TextureTest {
    
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
        
        assertTrue(a.equals(b));
        
        back[3] = ColorMath.fromRGB(16, 16, 16);
        assertEquals(back[3], a.get(1, 1));
        assertEquals(back[3], b.get(1, 1));
    
        assertTrue(a.equals(b));
    }
    
    @Test
    public void getImageWrapper() throws Exception {
        final int width = 32, height = 32;
        
        Texture texture = Texture.alloc(width, height);
        BufferedImage wrapper = texture.getImageWrapper();
    
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
            final int argb = ColorMath.random(true);
            
            assertNotEquals(argb, texture.get(x, y));
            assertNotEquals(argb, wrapper.getRGB(x, y));
    
            wrapper.setRGB(x, y, argb);
            
            assertEquals(argb, texture.get(x, y));
            assertEquals(argb, wrapper.getRGB(x, y));
        }
    }
    
    @Test
    public void averageRGB_ofNoiseIsGray() throws Exception {
        Texture t = noiseSquare(64);
        int avg = t.averageRGB(false);
        int gray = ColorMath.fromRGB(127, 127, 127);
        assertTrue(ColorMath.componentDiff(avg, gray, false) < 32);
    }
    
}