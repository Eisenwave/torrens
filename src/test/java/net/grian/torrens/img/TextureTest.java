package net.grian.torrens.img;

import net.grian.spatium.util.ColorMath;
import org.junit.Test;

import static org.junit.Assert.*;

public class TextureTest {
    
    public static Texture noiseSquare(int size) {
        Texture texture = new Texture(size, size);
        texture.forEachPosition((x,y) -> texture.set(x, y, ColorMath.random(true)));
        return texture;
    }
    
    @Test
    public void match() throws Exception {
        Texture t = noiseSquare(128);
        assertEquals(1F, t.match(t, true), 0);
    }
    
    @Test
    public void averageRGB_ofNoiseIsGray() throws Exception {
        Texture t = noiseSquare(64);
        int avg = t.averageRGB(false);
        int gray = ColorMath.fromRGB(127, 127, 127);
        assertTrue(ColorMath.componentDifference(avg, gray, false) < 32);
    }
    
}