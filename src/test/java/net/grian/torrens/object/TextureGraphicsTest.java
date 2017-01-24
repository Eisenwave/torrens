package net.grian.torrens.object;

import net.grian.spatium.util.ColorMath;
import net.grian.torrens.util.ANSI;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

public class TextureGraphicsTest {

    @Test
    public void drawLine_fillsDiagonal() throws Exception {
        TextureGraphics graphics = new Texture(64, 64).getGraphics();
        graphics.drawLine(0, 0, 63, 63, ColorMath.DEBUG1);

        for (int x = 0; x<64; x++) {
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
        TextureGraphics graphics = new Texture(256, 256).getGraphics();
        graphics.drawLine(0, 0, 127, 888, ColorMath.DEBUG1);
    }

}