package net.grian.torrens.img;

import net.grian.spatium.array.Incrementer2;
import net.grian.spatium.util.ColorMath;
import net.grian.spatium.util.RGBValue;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.*;
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
    
    public final static ColorModel COLOR_MODEL = ColorModel.getRGBdefault();
    
    private final static int[] BAND_MASKS = {
        0x00FF0000, // R
        0x0000FF00, // G
        0x000000FF, // B
        0xFF000000  // A
    };
    
    /**
     * Constructs a new Texture by allocating an ARGB array.
     *
     * @param width the texture width
     * @param height the texture height
     * @return a new Texture
     */
    @NotNull
    public static Texture alloc(int width, int height) {
        if (width < 1) throw new IllegalArgumentException("width must be >= 1");
        if (height < 1) throw new IllegalArgumentException("height must be >= 1");
        
        return new Texture(new int[width*height], width, height, false);
    }
    
    /**
     * Constructs a new Texture by copying an ARGB array.
     *
     * @param argb the array
     * @param width the texture width
     * @param height the texture height
     * @return a new Texture
     */
    @NotNull
    public static Texture copy(int[] argb, int width, int height) {
        validate(argb, width, height);
        return new Texture(Arrays.copyOfRange(argb, 0, width*height), width, height, false);
    }
    
    /**
     * Constructs a new Texture by copying the RGB data of a buffered image.
     *
     * @param image the image
     * @return a new Texture
     */
    @NotNull
    public static Texture copy(BufferedImage image) {
        final int
            width = image.getWidth(),
            height = image.getHeight();
        final int[] argb = image.getRGB(0, 0, width, height, new int[width * height], 0, width);
        
        return new Texture(argb, width, height, false);
    }
    
    /**
     * Constructs a new Texture by wrapping an ARGB array.
     *
     * @param argb the array
     * @param width the texture width
     * @param height the texture height
     * @return a new Texture
     */
    @NotNull
    public static Texture wrap(int[] argb, int width, int height) {
        validate(argb, width, height);
        return new Texture(argb, width, height, true);
    }
    
    /**
     * Constructs a new Texture by wrapping the ARGB data buffer of a buffered image. Note that an error is thrown if
     * the image raster does not have an appropriate buffer.
     *
     * @param image the image
     * @return a new Texture
     * @throws IllegalArgumentException if the image is not of type {@link BufferedImage#TYPE_INT_ARGB}
     * @see DataBufferInt#getData()
     */
    @NotNull
    public static Texture wrap(BufferedImage image) {
        final int
            width = image.getWidth(),
            height = image.getHeight();
        
        if (image.getType() != BufferedImage.TYPE_INT_ARGB)
            throw new IllegalArgumentException("image must be of type INT_ARGB");
        
        DataBuffer buffer = image.getRaster().getDataBuffer();
        if (buffer.getDataType() != DataBuffer.TYPE_INT)
            throw new IllegalArgumentException("image must have an int-buffer");
        
        int[] argb = ((DataBufferInt) buffer).getData();
        return new Texture(argb, width, height, true);
    }
    
    /**
     * Wraps an image of its format allows it, otherwise copies its data.
     *
     * @param image the image
     * @return a new Texture
     * @see #wrap(BufferedImage)
     * @see #copy(BufferedImage)
     */
    public static Texture wrapOrCopy(BufferedImage image) {
        return image.getType() == BufferedImage.TYPE_INT_ARGB? wrap(image) : copy(image);
    }
    
    private static void validate(int[] arr, int width, int height) {
        if (width < 1)
            throw new IllegalArgumentException("width must be >= 1");
        if (height < 1)
            throw new IllegalArgumentException("height must be >= 1");
        if (arr.length % width != 0)
            throw new IllegalArgumentException("rgb array can not represent img of width "+width);
        if (arr.length < width * height)
            throw new IllegalArgumentException("rgb array has insufficient length ("+arr.length+" < "+width*height+")");
    }
    
    // OBJECT

    private final int[] content;

    private final boolean wrapper;
    final int width, height;
    
    private Texture(int[] rgb, int width, int height, boolean wrapper) {
        this.wrapper = wrapper;
        this.content = rgb;
        this.width = width;
        this.height = height;
    }
    
    private Texture(Texture copyOf) {
        this.wrapper = false;
        this.width = copyOf.width;
        this.height = copyOf.height;
        this.content = new int[width * height];
        
        System.arraycopy(copyOf.content, 0, this.content, 0, this.content.length);
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
    public float getAspectRatio() {
        return width / (float) height;
    }

    @Override
    public int get(int x, int y) {
        return content[x + y*width];
    }
    
    @Override
    public void get(int x, int y, int width, int height, int[] arr, int offset) {
        if (width < 0 || height < 0)
            throw new IllegalArgumentException("width & height must be positive");
        
        // special case: copying entire rows
        if (x == 0 && width == this.width) {
            int srcPos = y*this.width;
            System.arraycopy(this.content, srcPos, arr, offset, width*height);
        }
        
        for (int j = 0; j < height; j++) {
            final int
                offArr = j*width,
                offCon = (y+j)*this.width;
            
            for (int i = 0; i < width; i++)
                arr[offset + offArr + i] = content[offCon + x + i];
        }
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
     *     For instance, a completely white and completely black image would have 0 match (with no alpha).
     * </p>
     * <p>
     *     This only takes raw pixel differences into account, visual deviation is not being respected.
     *     For instance, a completely transparent white and completely transparent black image still differ massively.
     * </p>
     *
     * @param texture the texture to compare to
     * @param alpha whether the alpha channel is to be respected
     * @return the match amount in range(0,1)
     */
    public float match(BaseTexture texture, boolean alpha) {
        final long
            maxDiff = width*height * (alpha? 1020 : 765),
            result = diff(texture, alpha);
        
        return (maxDiff - result) / (float) maxDiff;
    }
    
    private long diff(BaseTexture texture, boolean alpha) {
        if (width != texture.getWidth() || height != texture.getHeight())
            throw new IllegalArgumentException("can not get difference to texture with different resolution");
        
        long diff = 0;
        for (int x = 0; x < width; x++) for (int y = 0; y < height; y++) {
            diff += ColorMath.componentDifference(
                this.get(x, y),
                texture.get(x, y),
                alpha
            );
        }
        
        return diff;
    }
    
    static int count = 0;
    private static int compDiffDebug(int a, int b, boolean alpha) {
        final int
            red = Math.abs(ColorMath.red(a)-ColorMath.red(b)),
            grn = Math.abs(ColorMath.green(a)-ColorMath.green(b)),
            blue = Math.abs(ColorMath.blue(a)-ColorMath.blue(b)),
            alp = (alpha? Math.abs(ColorMath.alpha(a)-ColorMath.alpha(b)) : 0);
        
        if (count++ < 64) System.out.println(red+", "+grn+", "+blue+", "+alp);
        return red+grn+blue+alp;
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
        final int pixels = (maxX-minX+1) * (maxY-minY+1);
        
        if (minX == 0 && minY == 0 && maxX == width-1 && maxY == height-1) for (int i = 0; i < pixels; i++) {
            final int rgb = content[i];
            r += ColorMath.red(rgb);
            g += ColorMath.green(rgb);
            b += ColorMath.blue(rgb);
            if (transparency)
                a += ColorMath.alpha(rgb);
        }
        
        else for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) {
            final int rgb = get(x, y);
            r += ColorMath.red(rgb);
            g += ColorMath.green(rgb);
            b += ColorMath.blue(rgb);
            if (transparency)
                a += ColorMath.alpha(rgb);
        }
        
        r /= pixels;
        g /= pixels;
        b /= pixels;
        a = transparency? a/pixels : 0xFF;
        
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
    
    /**
     * Returns the average hue, saturation and brightness of this image.
     *
     * @return the average HSB
     */
    public float[] averageHSB() {
        double[] sum = {0, 0, 0};
        double sumSat = 0;
        final int pixels = width * height;
        
        for (int i = 0; i < pixels; i++) {
            final int rgb = content[i];
            float[] hsb = Color.RGBtoHSB(
                ColorMath.red(rgb),
                ColorMath.green(rgb),
                ColorMath.blue(rgb),
                null);
            double sat = hsb[1];
            sumSat += sat;
            
            sum[0] += hsb[0] * sat;
            sum[1] += hsb[1];
            sum[2] += hsb[2];
        }
        
        sum[0] /= sumSat;
        sum[1] /= pixels;
        sum[2] /= pixels;
        
        return new float[] {
            (float) sum[0],
            (float) sum[1],
            (float) sum[2]};
    }
    
    // CHECKERS
    
    /**
     * Returns whether this texture is a wrapper of any content, such as an ARGB int-array.
     *
     * @return whether this texture is a wrapper
     */
    public boolean isWrapper() {
        return wrapper;
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
    
    public void paste(BaseTexture texture) {
        paste(texture, 0, 0);
    }
    
    private void internalPaste(BaseTexture content, int w, int h, int u, int v) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                set(u+i, v+j, content.get(i, j));
    }
    
    //MISC
    
    
    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Texture clone() {
        return new Texture(this);
    }
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || obj instanceof Texture && equals((Texture) obj);
    }
    
    /**
     * Returns whether this texture is exactly equal to another texture, pixel by pixel.
     *
     * @param texture the other texture
     * @return whether this texture is exactly equal to the other texture
     */
    public boolean equals(Texture texture) {
        if (this.width != texture.width || this.height != texture.height)
            return false;
        
        final int lim = width * height;
        for (int i = 0; i < lim; i++)
            if (this.content[i] != texture.content[i])
                return false;
        
        return true;
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
        
        result.setRGB(0, 0, width, height, get(0, 0, width, height), 0, width);
        return result;
    }
    
    public WritableRaster getRaster() {
        DataBuffer buffer = new DataBufferInt(content, content.length);
        return Raster.createPackedRaster(buffer, width, height, width, BAND_MASKS, null);
    }
    
    /**
     * Converts this texture to a {@link BufferedImage} with the default color model
     * ({@link BufferedImage#TYPE_INT_ARGB}).
     *
     * @return a new image
     */
    public BufferedImage getImageWrapper() {
        return new BufferedImage(COLOR_MODEL, getRaster(), false, null);
    }
    
    //ITERATION
    
    @Override
    public void forEach(Consumer<? super Pixel> action) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                action.accept(getPixel(x, y));
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
