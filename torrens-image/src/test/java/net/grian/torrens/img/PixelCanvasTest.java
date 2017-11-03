package net.grian.torrens.img;

import net.grian.torrens.util.ColorMath;
import org.junit.Test;

import static org.junit.Assert.*;

public class PixelCanvasTest {

    @SuppressWarnings("SuspiciousNameCombination")
    @Test
    public void drawLine_fillsDiagonal() throws Exception {
        PixelCanvas graphics = Texture.alloc(64, 64).getGraphics();
        graphics.drawLine(0, 0, 63, 63, ColorMath.DEBUG1);

        for (int x = 0; x < 64; x++) {
            try {
                assertEquals(ColorMath.DEBUG1, graphics.getContent().get(x, x));
            } catch (AssertionError error) {
                System.err.println("ERROR AT: x="+x+", y="+x);
                throw error;
            }
        }
    }

    @Test
    public void drawLine_Print() throws Exception {
        PixelCanvas graphics = Texture.alloc(256, 256).getGraphics();
        graphics.drawLine(0, 0, 127, 888, ColorMath.DEBUG1);
    }

}