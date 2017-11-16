package net.grian.torrens.img;

import net.grian.torrens.object.Vertex2f;
import net.grian.torrens.util.ColorMath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Textures {
    
    private final static int
        TYPE_IDENTITY = 0,
        TYPE_UPSCALE = 1,
        TYPE_DOWNSCALE = 2,
        TYPE_MIXED = 3;
    
    @Contract(pure = true)
    private static int type(int w0, int h0, int w1, int h1) {
        if (w1 > w0) {
            if (h1 > h0) return TYPE_UPSCALE;
            else return TYPE_MIXED;
        }
        else if (w1 < w0) {
            if (h1 < h0) return TYPE_DOWNSCALE;
            else return TYPE_MIXED;
        }
        else {
            if (h1 == h0) return TYPE_IDENTITY;
            else return TYPE_MIXED;
        }
    }
    
    /**
     * <p>
     * Returns the light source location of the texture.
     * <p>
     * The result is a pair of float coordinates (x,y) in range(-1,1) specifying the light source of an image.
     * <p>
     * The light source location of an image is defined as:
     * <blockquote>
     * let <b>location</b> be the deviation from the center <code>in range(-1,-1) to (1,1)</code>
     * <br>let <b>brightness</b> be the deviation from gray with -1 being pitch black, 1 being pure white
     * <code>(for all pixels: sum of (location * brightness)) / amount of pixels</code>
     * </blockquote>
     *
     * @param texture the texture
     */
    @NotNull
    public static Vertex2f lightSource(Texture texture) {
        final int
            width = texture.getWidth(),
            height = texture.getHeight(),
            area = width * height;
        final float
            cenX = (width - 1) / 2F,
            cenY = (height - 1) / 2F;
        
        float lightX = 0, lightY = 0;
        
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                final float
                    locX = (x - cenX) / cenX,
                    locY = (y - cenY) / cenX,
                    bright = (ColorMath.brightness(texture.get(x, y)) - 0.5F) * 2F;
                
                lightX += locX * bright;
                lightY += locY * bright;
            }
        
        return new Vertex2f(lightX / area, lightY / area);
    }
    
}
