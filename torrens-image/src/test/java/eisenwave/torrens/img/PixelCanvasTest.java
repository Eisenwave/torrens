package eisenwave.torrens.img;

import eisenwave.torrens.util.ColorMath;
import org.junit.Test;

import static org.junit.Assert.*;

public class PixelCanvasTest {
    
    @SuppressWarnings("SuspiciousNameCombination")
    @Test
    public void drawLine_fillsDiagonal() {
        PixelCanvas graphics = Texture.alloc(64, 64).getGraphics();
        graphics.drawLine(0, 0, 63, 63, ColorMath.DEBUG1);
        
        for (int x = 0; x < 64; x++) {
            try {
                assertEquals(ColorMath.DEBUG1, graphics.getContent().get(x, x));
            } catch (AssertionError error) {
                System.err.println("ERROR AT: x=" + x + ", y=" + x);
                throw error;
            }
        }
    }
    
    @Test
    public void drawLine_Print() {
        PixelCanvas canvas = Texture.alloc(256, 256).getCanvasWrapper();
        canvas.drawLine(0, 0, 127, 888, ColorMath.DEBUG1);
    }
    
    @Test
    public void drawCircle() {
        final int size = 5, halfSize = size / 2;
        
        Texture texture = Texture.alloc(size, size);
        PixelCanvas canvas = texture.getCanvasWrapper();
        canvas.drawRaster(ColorMath.SOLID_WHITE, ColorMath.fromRGB(0.5f, 0.5f, 0.5f), 1);
        for (int r = 0; r <= halfSize; r++) {
            int rgb = ColorMath.fromRGB((int) ((255 * 2f * r) / size), 0, 255);
            canvas.drawCircle(halfSize, halfSize, r, rgb);
        }
        //canvas.drawCircle(halfSize, halfSize, 0, ColorMath.SOLID_BLUE);
        //new SerializerPNG().toFile(texture.getImageWrapper(), "/home/user/Files/PixelCanvasTest.png");
    }
    
}
