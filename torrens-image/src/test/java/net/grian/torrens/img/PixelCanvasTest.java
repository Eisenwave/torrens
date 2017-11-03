package net.grian.torrens.util.img;

import net.grian.torrens.img.PixelCanvas;
import net.grian.torrens.img.Texture;
import net.grian.torrens.util.ANSI;
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
                System.out.println(ANSI.bold("ERROR AT: x="+x+", y="+x));
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