package net.grian.torrens.img;

import net.grian.spatium.array.Incrementer2;
import net.grian.spatium.function.Int2Consumer;
import net.grian.spatium.util.ColorMath;
import net.grian.spatium.util.RGBValue;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * <p>
 *     A basic image format using a two-dimensional array of ARGB integers to represent an image.
 * </p>
 */
public class Texture implements Serializable, BaseTexture, Iterable<Texture.Pixel> {

    private final int[] content;

    final int width, height;

    /**
     * Constructs a texture with a given width and height.
     *
     * @param width the texture width
     * @param height the texture height
     */
    public Texture(int width, int height) {
        if (width < 1) throw new IllegalArgumentException("width < 1");
        if (height < 1) throw new IllegalArgumentException("height < 1");

        this.content = new int[width * height];
        this.width = width;
        this.height = height;
    }
    
    public Texture(int[] rgb, int width) {
        if (rgb.length % width != 0)
            throw new IllegalArgumentException("rgb array can not represent img of width "+width);
        
        this.content = Arrays.copyOf(rgb, rgb.length);
        this.width = width;
        this.height = rgb.length / width;
    }
    
    /**
     * Constructs a texture from another texture.
     *
     * @param copyOf the copied texture
     */
    public Texture(Texture copyOf) {
        this.width = copyOf.width;
        this.height = copyOf.height;
        this.content = new int[width * height];
        
        System.arraycopy(copyOf.content, 0, this.content, 0, 0);
    }
    
    /**
     * Constructs a texture from a {@link RenderedImage} with arbitrary color model.
     *
     * @param image the image
     */
    public Texture(RenderedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.content = new int[width * height];
        
        Raster raster = image.getData();
        ColorModel colorModel = image.getColorModel();
        
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                set(x, y, colorModel.getRGB(raster.getDataElements(x, y, null)) );
    }

    /**
     * Returns new graphics for this texture.
     *
     * @return the texture graphics
     */
    public TextureCanvas getGraphics() {
        return new TextureCanvas(this);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int get(int x, int y) {
        return content[x + y*width];
    }
    
    public int[] get(int minX, int minY, int maxX, int maxY) {
        final int dx = maxX-minX+1, dy = maxY-minY+1;
        
        int[] result = new int[dx * dy];
        for (int x = 0; x < dx; x++) for (int y = 0; y < dy; y++) {
            result[x + y*dx] = get(minX+x, minY+y);
        }
        
        return result;
    }
    
    public int get(float u, float v) {
        return get(
            u==1? (width-1) : (int) (u * width),
            v==1? (width-1) : (int) (v * height));
    }
    
    public Pixel getPixel(int x, int y) {
        return new Pixel(x, y);
    }
    
    public Pixel getPixel(float u, float v) {
        int x = u==1? (width-1) : (int) (u * width);
        int y = v==1? (width-1) : (int) (v * height);
        
        return getPixel(x, y);
    }
    
    /**
     * <p>
     *     Returns the total match amount between this and another texture.
     * </p>
     * <p>
     *     The result is a value in range(0,1), with 0 being absolutely no match and 1 being complete pixel equality.
     *     For instance, a completely white and completely black image would have 0 match (with no transparency).
     * </p>
     * <p>
     *     This only takes raw pixel differences into account, visual deviation is not being respected.
     *     For instance, a completely transparent white and completely transparent black image still differ massively.
     * </p>
     *
     * @param texture the texture to compare to
     * @param transparency whether the alpha channel is to be respected
     * @return the match amount in range(0,1)
     */
    public float match(BaseTexture texture, boolean transparency) {
        final long maxDiff = width*height * (transparency? 1020 : 765);
        
        return (maxDiff - diff(texture, true)) / (float) maxDiff;
    }
    
    private long diff(BaseTexture texture, boolean transparency) {
        if (width != texture.getWidth() || height != texture.getHeight())
            throw new IllegalArgumentException("can not get difference to texture with different resolution");
        
        long diff = 0;
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
            diff += ColorMath.componentDifference(
                this.get(x, y),
                texture.get(x, y),
                transparency
            );
        }
        
        return diff;
    }
    
    /**
     * Returns the average color of this image inside a given section.
     *
     * @param transparency whether pixel alpha is to be respected
     * @return an argb int representing the average color
     */
    public int averageRGB(int minX, int minY, int maxX, int maxY, boolean transparency) {
        if (minX < 0 || minX >= width) throw new IllegalArgumentException("minX out of range ("+minX+")");
        if (minY < 0 || minY >= height) throw new IllegalArgumentException("minY out of range ("+minX+")");
        if (maxX < 0 || maxX >= width) throw new IllegalArgumentException("maxX out of range ("+maxX+")");
        if (maxY < 0 || maxY >= height) throw new IllegalArgumentException("maxY out of range ("+maxY+")");
        
        long r = 0, g = 0, b = 0, a = 0;
        final int div = (maxX-minX+1) * (maxY-minY+1);
        
        for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) {
            final int rgb = get(x, y);
            r += ColorMath.red(rgb);
            g += ColorMath.green(rgb);
            b += ColorMath.blue(rgb);
            a += ColorMath.alpha(rgb);
        }
        
        r /= div;
        g /= div;
        b /= div;
        
        if (transparency) a /= div;
        else a = 255;
        
        return ColorMath.fromRGB((int) r, (int) g, (int) b, (int) a);
    }
    
    /**
     * Returns the average color of this image.
     *
     * @param transparency whether pixel alpha is to be respected
     * @return an argb int representing the average color
     */
    public int averageRGB(boolean transparency) {
        return averageRGB(0, 0, width-1, height-1, transparency);
    }
    
    // SETTERS

    @Override
    public void set(int x, int y, int rgb) {
        content[x + y*width] = rgb;
    }
    
    public void paste(BaseTexture texture, final int u, final int v) {
        final int w = texture.getWidth(), h = texture.getHeight();
        if (w + u > this.getWidth())
            throw new IllegalArgumentException("texture width out of bounds");
        if (h + v > this.getHeight())
            throw new IllegalArgumentException("texture height out of bounds");
        
        internalPaste(texture, w, h, u, v);
    }
    
    public void paste(Texture texture) {
        paste(texture, 0, 0);
    }
    
    private void internalPaste(BaseTexture content, int w, int h, int u, int v) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                set(u+i, v+j, content.get(i, j));
    }
    
    //MISC
    
    @Override
    public String toString() {
        return Texture.class.getSimpleName()+"{width="+width+",height="+height+"}";
    }
    
    /**
     * Converts this texture to a {@link BufferedImage} with the default color model
     * ({@link BufferedImage#TYPE_INT_ARGB}).
     *
     * @return a new image
     */
    public BufferedImage toImage(boolean alpha) {
        int type = alpha? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
        BufferedImage result = new BufferedImage(width, height, type);
        
        int[] rgbArray = new int[width * height];
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                rgbArray[x + y*width] = content[x + y*width];
        
        result.setRGB(0, 0, width, height, rgbArray, 0, width);
        return result;
    }
    
    /**
     * Converts this texture to a {@link BufferedImage} with the default color model
     * ({@link BufferedImage#TYPE_INT_ARGB}).
     *
     * @return a new image
     */
    public BufferedImage toImage() {
        return toImage(true);
    }
    
    //ITERATION
    
    @Override
    public void forEach(Consumer<? super Pixel> action) {
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                action.accept(getPixel(x, y));
    }
    
    public void forEachPosition(Int2Consumer action) {
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                action.accept(x, y);
    }
    
    @Override
    public PixelIterator iterator() {
        return new PixelIterator();
    }
    
    public class Pixel implements RGBValue {
        
        private final int x, y;
        
        private Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        public int getX() {
            return x;
        }
        
        public int getY() {
            return y;
        }
        
        @Override
        public int getRGB() {
            return Texture.this.get(x, y);
        }
        
    }
    
    public class PixelIterator implements Iterator<Pixel> {
        
        private final Incrementer2 increment = new Incrementer2(width, height);
    
        @Override
        public boolean hasNext() {
            return increment.canIncrement();
        }
    
        @Override
        public Pixel next() {
            int[] xy = increment.current();
            Pixel pixel = new Pixel(xy[0], xy[1]);
            increment.increment();
            return pixel;
        }
        
        public int nextRGB() {
            int[] xy = increment.current();
            int rgb = get(xy[0], xy[1]);
            increment.increment();
            return rgb;
        }
        
    }
    
}
