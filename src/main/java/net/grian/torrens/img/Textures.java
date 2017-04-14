package net.grian.torrens.img;

import net.grian.spatium.util.ColorMath;
import net.grian.torrens.object.Vertex2f;

public class Textures {
    
    public final static int
        SCALE_AREA_AVG = 0,
        SCALE_NEAREST_NB = 1;
    
    /**
     * Scales a texture to a given width and height.
     *
     * @param texture the texture
     * @param width the new width
     * @param height the new height
     */
    public static Texture scale(Texture texture, int width, int height, int mode) {
        switch (mode) {
            case SCALE_NEAREST_NB:
                return scaleNearestNb(texture, width, height);
            case SCALE_AREA_AVG:
                return scaleAreaAvg(texture, width, height);
            default:
                throw new IllegalArgumentException("unknown downscale mode: " + mode);
        }
    }
    
    private static Texture scaleNearestNb(Texture texture, int w1, int h1) {
        final int
            w0 = texture.getWidth(),
            h0 = texture.getHeight();
        Texture result = new Texture(w1, h1);
    
        for (int x1 = 0; x1 < w1; x1++) for (int y1 = 0; y1 < h1; y1++) {
            final int rgb = texture.get(
                x1 * w0 / w1,
                y1 * h0 / h1);
            result.set(x1, y1, rgb);
        }
        
        return result;
    }
    
    private static Texture scaleAreaAvg(Texture texture, int w1, int h1) {
        final int
            w0 = texture.getWidth(),
            h0 = texture.getHeight();
        Texture result = new Texture(w1, h1);
        
        for (int x1 = 0; x1 < w1; x1++) for (int y1 = 0; y1 < h1; y1++) {
            
            final int
                minX = x1 * w0 / w1,
                minY = y1 * h0 / h1,
                limX = (x1+1) * w0 / w1,
                limY = (y1+1) * h0 / h1,
                //if the new width or height are smaller, max(X|Y) < min(X|Y)
                //in this scenario the algorithm essentially uses nearest nb on one or multiple axes
                maxX = Math.max(minX, limX-1),
                maxY = Math.max(minY, limY-1);
            
            result.set(x1, y1, texture.averageRGB(minX, minY, maxX, maxY, true));
        }
        
        return result;
    }
    
    /**
     * <p>
     *     Returns the light source location of the texture.
     * </p>
     * <p>
     *     The result is a pair of float coordinates (x,y) in range(-1,1) specifying the light source of an image.
     * </p>
     * <p>
     *     The light source location of an image is defined as:
     *     <blockquote>
     *             let <b>location</b> be the deviation from the center <code>in range(-1,-1) to (1,1)</code>
     *         <br>let <b>brightness</b> be the deviation from gray with -1 being pitch black, 1 being pure white
     *         <code>(for all pixels: sum of (location * brightness)) / amount of pixels</code>
     *     </blockquote>
     * </p>
     *
     * @param texture the texture
     */
    public static Vertex2f lightSource(Texture texture) {
        final int
            width = texture.getWidth(),
            height = texture.getHeight(),
            area = width * height;
        final float
            cenX = (width-1) / 2F,
            cenY = (height-1) / 2F;
        
        float lightX = 0, lightY = 0;
        
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
            final float
                locX = (x - cenX) / cenX,
                locY = (y - cenY) / cenX,
                bright = (ColorMath.brightness(texture.get(x,y)) - 0.5F) * 2F;
            
            lightX += locX*bright;
            lightY += locY*bright;
        }
        
        return new Vertex2f(lightX/area, lightY/area);
    }
    
}
