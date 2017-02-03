package net.grian.torrens.img;

import net.grian.spatium.array.Incrementer2;
import net.grian.spatium.function.Int2Consumer;
import net.grian.spatium.util.RGBValue;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.Serializable;
import java.util.Iterator;
import java.util.function.Consumer;

/**
 * <p>
 *     A basic image format using a two-dimensional array of ARGB integers to represent an image.
 * </p>
 */
public class Texture implements Serializable, BaseTexture, Iterable<Texture.Pixel> {

    private final int[][] content;

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

        this.content = new int[width][height];
        this.width = width;
        this.height = height;
    }
    
    /**
     * Constructs a texture from another texture.
     *
     * @param copyOf the copied texture
     */
    public Texture(Texture copyOf) {
        this.width = copyOf.width;
        this.height = copyOf.height;
        this.content = new int[width][height];
        
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
        this.content = new int[width][height];
        
        Raster raster = image.getData();
        ColorModel colorModel = image.getColorModel();
        
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                content[x][y] = colorModel.getRGB(raster.getDataElements(x, y, null));
    }

    /**
     * Returns new graphics for this texture.
     *
     * @return the texture graphics
     */
    public TextureCanvas getGraphics() {
        return new TextureCanvas(this);
    }

    public void paste(BaseTexture texture, final int u, final int v) {
        final int w = texture.getWidth(), h = texture.getHeight();
        if (w + u > this.getWidth())
            throw new IllegalArgumentException("texture width out of bounds");
        if (h + v > this.getHeight())
            throw new IllegalArgumentException("texture height out of bounds");

        if (texture instanceof Texture)
            internalPaste(((Texture) texture).content, w, h, u, v);
        else
            internalPaste(texture, w, h, u, v);
    }

    public void paste(Texture texture) {
        paste(texture, 0, 0);
    }

    private void internalPaste(BaseTexture content, int w, int h, int u, int v) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                this.content[i + u][j + v] = content.get(i, j);
    }

    @SuppressWarnings("ManualArrayCopy")
    private void internalPaste(int[][] content, int w, int h, int u, int v) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                this.content[i + u][j + v] = content[i][j];
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
        return content[x][y];
    }
    
    public int get(float u, float v) {
        int x = u==1? (width-1) : (int) (u * width);
        int y = v==1? (width-1) : (int) (v * height);
        
        return content[x][y];
    }
    
    public Pixel getPixel(int x, int y) {
        return new Pixel(x, y);
    }
    
    public Pixel getPixel(float u, float v) {
        int x = u==1? (width-1) : (int) (u * width);
        int y = v==1? (width-1) : (int) (v * height);
        
        return getPixel(x, y);
    }

    @Override
    public void set(int u, int v, int rgb) {
        content[u][v] = rgb;
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
                rgbArray[x + y*width] = content[x][y];
        
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
            return Texture.this.content[x][y];
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
