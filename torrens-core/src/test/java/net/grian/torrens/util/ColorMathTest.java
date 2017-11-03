package net.grian.torrens.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorMathTest {
    
    /*
    @Test
    public void xyz() throws Exception {
        float[] magenta = ColorMath.xyz(ColorMath.SOLID_MAGENTA);
        assertArrayEquals(new float[] {0.5792F, 0.2831F, 0.7281F}, magenta, 1E-5F);
    }
    */
    
    @Test
    public void componentDifference_constants() throws Exception {
        assertEquals(0, ColorMath.componentDiff(ColorMath.DEBUG1, ColorMath.DEBUG1, true));
        assertEquals(255, ColorMath.componentDiff(ColorMath.SOLID_RED, ColorMath.SOLID_YELLOW, true));
        assertEquals(510, ColorMath.componentDiff(ColorMath.SOLID_RED, ColorMath.SOLID_BLUE, true));
        assertEquals(765, ColorMath.componentDiff(ColorMath.INVISIBLE_BLACK, ColorMath.SOLID_WHITE, false));
        assertEquals(1020, ColorMath.componentDiff(ColorMath.INVISIBLE_BLACK, ColorMath.SOLID_WHITE, true));
        
    }
    
    @Test
    public void componentDifference_random() throws Exception {
        for (int i = 0; i < 100; i++) {
            final int random = ColorMath.random(true);
            assertEquals(0, ColorMath.componentDiff(random, random, true));
            assertEquals(0, ColorMath.componentDiff(random, random, false));
        }
    }
    
    @Test
    public void rgb() throws Exception {
        for (int i = 0; i<10000; i++) {
            int rgb0 = ColorMath.random(true);
            int rgb1 = ColorMath.fromRGB(
                ColorMath.red(rgb0),
                ColorMath.green(rgb0),
                ColorMath.blue(rgb0),
                ColorMath.alpha(rgb0));
            
            assertEquals(rgb0, rgb1);
        }
    }

    private final static int
    SOLID = ColorMath.fromRGB(1F, 1F, 1F, 1F),
    TRANSPARENT = ColorMath.fromRGB(1F, 1F, 1F, 0.5F),
    INVISIBLE = ColorMath.fromRGB(1F, 1F, 1F, 0F);

    @Test
    public void isTransparent() throws Exception {
        assertFalse(ColorMath.isTransparent(SOLID));
        assertTrue(ColorMath.isTransparent(TRANSPARENT));
        assertTrue(ColorMath.isTransparent(INVISIBLE));
    }

    @Test
    public void isSolid() throws Exception {
        assertTrue(ColorMath.isSolid(SOLID));
        assertFalse(ColorMath.isSolid(TRANSPARENT));
        assertFalse(ColorMath.isSolid(INVISIBLE));
    }

    @Test
    public void isVisible() throws Exception {
        assertTrue(ColorMath.isVisible(SOLID));
        assertTrue(ColorMath.isVisible(TRANSPARENT));
        assertFalse(ColorMath.isVisible(INVISIBLE));
    }

    @Test
    public void isInvisible() throws Exception {
        assertFalse(ColorMath.isInvisible(SOLID));
        assertFalse(ColorMath.isInvisible(TRANSPARENT));
        assertTrue(ColorMath.isInvisible(INVISIBLE));
    }

}