package eisenwave.torrens;

import eisenwave.torrens.util.ColorMath;
import org.junit.Test;

import java.awt.*;

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
    
    @Test
    public void stack_opaque() throws Exception {
        final int stacked = ColorMath.stack(ColorMath.SOLID_BLACK, ColorMath.SOLID_WHITE);
    
        assertTrue(ColorMath.isSolid(stacked));
        assertEquals(stacked, ColorMath.SOLID_WHITE);
    }
    
    @Test
    public void stack_invisible() throws Exception {
        final int stacked = ColorMath.stack(ColorMath.SOLID_RED, ColorMath.INVISIBLE_WHITE);
    
        assertTrue(ColorMath.isSolid(stacked));
        assertEquals(stacked, ColorMath.SOLID_RED);
    }
    
    @Test
    public void stack_transparent_onOpaque() throws Exception {
        // half-transparent blue on red
        final int stacked = ColorMath.stack(ColorMath.SOLID_RED, ColorMath.fromRGB(0, 0, 255, 127));
        // expect purple
        assertTrue(ColorMath.isSolid(stacked));
        assertEquals(new Color(127, 0, 127), new Color(stacked, true));
    }
    
    @Test
    public void stack_transparent_onTransparent() throws Exception {
        // half-transparent green on half-transparent red
        final int stacked = ColorMath.stack(ColorMath.fromRGB(255, 0, 0, 127), ColorMath.fromRGB(0, 255, 0, 127));
        // expect slightly transparent lime
        Color expected = new Color(85,  169, 0, 190); // expected values as seen in layer stacking  in GIMP 3.0
        //System.out.println(expected.getAlpha()+" ?= "+ColorMath.alpha(stacked));
        
        assertFalse(ColorMath.isSolid(expected.getRGB()));
        assertEquals(expected, new Color(stacked, true));
    }

}
