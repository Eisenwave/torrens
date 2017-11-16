package net.grian.torrens.img;

import net.grian.spatium.array.IntArray2;
import net.grian.spatium.enums.CardinalDirection;
import net.grian.spatium.function.Int2Consumer;
import net.grian.spatium.function.Int2IntFunction;
import net.grian.spatium.function.Int2Predicate;

/**
 * Object dedicated to drawing in {@link Texture} objects.
 */
public class PixelCanvas {
    
    private final BaseTexture data;
    private final int width, height;
    
    public PixelCanvas(BaseTexture data) {
        this.data = data;
        this.width = data.getWidth();
        this.height = data.getHeight();
    }
    
    public PixelCanvas(int width, int height) {
        this(Texture.alloc(width, height));
    }
    
    public BaseTexture getContent() {
        return data;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public int getRGB(int x, int y) {
        return data.get(x, y);
    }
    
    // TRANSFORM
    
    /**
     * Flips the image horizontally.
     */
    public void flipX() {
        final int
            lim = width / 2,
            max = width - 1;
        
        for (int x = 0; x < lim; x++)
            for (int y = 0; y < height; y++)
                data.swap(x, y, max - x, y);
    }
    
    /**
     * Flips the image vertically.
     */
    public void flipY() {
        final int
            lim = height / 2,
            max = height - 1;
        
        for (int x = 0; x < width; x++)
            for (int y = 0; y < lim; y++)
                data.swap(x, y, x, max - y);
    }
    
    /**
     * Flips the image both horizontally and vertically.
     */
    public void flipXY() {
        final int
            limW = width / 2, limH = height / 2,
            maxX = width - 1, maxY = height - 1;
        
        for (int x = 0; x < limW; x++)
            for (int y = 0; y < limH; y++)
                data.swap(x, y, maxX - x, maxY - y);
    }
    
    // DRAW
    
    /**
     * <p>
     * Gives the given pixel a given rgb value (ARGB integer).
     * </p>
     * <p>
     * Should the pixel coordinates be outside the texture boundaries, nothing is drawn and the input is ignored.
     * </p>
     *
     * @param x the pixel x-coordinate
     * @param y the pixel y-coordinate
     * @param rgb the ARGB value
     */
    public void draw(int x, int y, int rgb) {
        if (x >= 0 && y >= 0 && x < width && y < height)
            data.set(x, y, rgb);
    }
    
    /**
     * <p>
     * Gives the given pixel a given rgb value (ARGB integer).
     * </p>
     * <p>
     * Should the pixel coordinates be outside the texture boundaries, nothing is drawn and the input is ignored.
     * </p>
     *
     * @param x the pixel x-coordinate
     * @param y the pixel y-coordinate
     * @param rgb the ARGB value
     */
    public void draw(float x, float y, int rgb) {
        draw((int) x, (int) y, rgb);
    }
    
    /**
     * Fills the texture with a single color.
     *
     * @param rgb the color
     */
    public void drawAll(int rgb) {
        drawRaw((x, y) -> rgb);
    }
    
    public void drawIf(Int2Predicate condition, int rgb) {
        forEachPixel((x, y) -> {if (condition.test(x, y)) draw(x, y, rgb);});
    }
    
    public void drawIfElse(Int2Predicate condition, int rgbTrue, int rgbFalse) {
        drawRaw((x, y) -> condition.test(x, y)? rgbTrue : rgbFalse);
    }
    
    /**
     * Directly draws a function into the texture for every pixel.
     *
     * @param function the function
     */
    public void drawRaw(Int2IntFunction function) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                draw(x, y, function.apply(x, y));
    }
    
    /**
     * Draws a shape with given rgb value into the texture.
     *
     * @param shape the shape
     * @param rgb the rgb value
     */
    public void drawShape(Int2Predicate shape, int rgb) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                if (shape.test(x, y)) draw(x, y, rgb);
    }
    
    /**
     * Draws a two-colored raster into the texture.
     *
     * @param rgb0 the first rgb value
     * @param rgb1 the second rgb value
     */
    public void drawRaster(int rgb0, int rgb1) {
        drawRaw(((x, y) -> (x % 2 == y % 2)? rgb0 : rgb1));
    }
    
    /**
     * Draws a two-colored raster into the texture.
     *
     * @param rgb0 the first rgb value
     * @param rgb1 the second rgb value
     */
    public void drawRaster(int rgb0, int rgb1, int tileSize) {
        if (tileSize < 1) return;
        drawRaw(((x, y) -> x / tileSize % 2 == y / tileSize % 2? rgb0 : rgb1));
    }
    
    public void drawRectangle(int rgb, int x0, int y0, int x1, int y1) {
        final int
            minX = Math.min(x0, x1), maxX = Math.max(x0, x1),
            minY = Math.min(y0, y1), maxY = Math.max(y0, y1);
        
        for (int x = minX; x < maxX; x++)
            for (int y = minY; y < maxY; y++)
                data.set(x, y, rgb);
    }
    
    public void drawLine(int x0, int y0, int x1, int y1, int rgb) {
        if (x0 == x1 && y0 == y1) {
            draw(x0, y0, rgb);
            return;
        }
        
        internalDrawLine(
            Math.min(x0, x1), Math.min(y0, y1),
            Math.max(x0, x1), Math.max(y0, y1), rgb);
    }
    
    private void internalDrawLine(int minX, int minY, int maxX, int maxY, int rgb) {
        final int dx = maxX - minX, dy = maxY - minY;
        int error = Math.max(dx, dy) / 2;
        
        if (dx >= dy) for (int x = minX, y = minY; x <= maxX; x++) {
            draw(x, y, rgb);
            
            error -= dy;
            if (error < 0) {
                error += dx;
                y++;
            }
        }
        else for (int x = minX, y = minY; y <= maxY; y++) {
            draw(x, y, rgb);
            
            error -= dx;
            if (error < 0) {
                error += dy;
                x++;
            }
        }
    }
    
    /**
     * Draws a texture at a given point in the canvas.
     *
     * @param img the texture to draw
     * @param x the x-position
     * @param y the y-position
     * @param w the width of the rectangle to draw
     * @param h the height of the rectangle to draw
     */
    public void drawTexture(BaseTexture img, int x, int y, int w, int h) {
        for (int i = 0; i < w; i++)
            for (int j = 0; j < h; j++)
                img.set(x + i, y + j, img.get(i, j));
    }
    
    /**
     * Draws a texture at a given point in the canvas.
     *
     * @param img the texture to draw
     * @param x the x-position
     * @param y the y-position
     */
    public void drawTexture(BaseTexture img, int x, int y) {
        drawTexture(img, x, y, img.getWidth(), img.getHeight());
    }
    
    // FILL
    
    private final static int
        N = 1 << CardinalDirection.NORTH.ordinal(),
        E = 1 << CardinalDirection.EAST.ordinal(),
        S = 1 << CardinalDirection.SOUTH.ordinal(),
        W = 1 << CardinalDirection.WEST.ordinal(),
        NESW = N | E | S | W,
        F = 1 << 5;
    
    public void floodFill(int ox, int oy, Int2Predicate fillCond, int rgb) {
        IntArray2 mask = new IntArray2(width, height);
        forEachPixel((x, y) -> { //predefine all unfillable pixels
            if (!fillCond.test(x, y))
                mask.set(x, y, F);
        });
        
        FloodFillConfig config = new FloodFillConfig(0, 0, width - 1, height - 1, rgb);
        
        internalFloodFill(mask, config, 0, ox, oy);
    }
    
    private static class FloodFillConfig {
        
        private final int minX, minY, maxX, maxY, rgb;
        
        public FloodFillConfig(int minX, int minY, int maxX, int maxY, int rgb) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
            this.rgb = rgb;
        }
        
    }
    
    private void internalFloodFill(IntArray2 mask, FloodFillConfig config, int org, int x, int y) {
        int here = mask.get(x, y);
        //pixel is either empty or already processed
        if (here >= 0b1111) return;
        
        //write into the mask that the pixel is processed
        mask.set(x, y, here | NESW);
        
        //draw the pixel
        draw(x, y, config.rgb);
        
        //not coming from north && north is unoccupied
        if (y != config.maxY && org != N) internalFloodFill(mask, config, S, x, y + 1);
        if (x != config.maxX && org != E) internalFloodFill(mask, config, W, x + 1, y);
        if (y != config.minY && org != S) internalFloodFill(mask, config, N, x, y - 1);
        if (x != config.minX && org != W) internalFloodFill(mask, config, E, x - 1, y);
    }
    
    private void ffFinalize(IntArray2 mask, int x, int y) {
        int val = mask.get(x, y);
        if ((val & F) != 0)
            mask.set(x, y, val | NESW);
    }
    
    public void edgeFloodFill2(Int2Predicate fillCond, int rgb) {
        IntArray2 mask = new IntArray2(width, height);
        forEachPixel((x, y) -> { //predefine all barriers
            if (!fillCond.test(x, y)) mask.set(x, y, -1);
        });
        
        /*
        forEachEdgePixel((x,y) -> {
            internalFloodFill(mask, 0, x, y);
        });
        */
    }
    
    public void edgeFloodFill(Int2Predicate fillCond, int rgb) {
        final int xmax = width - 1, ymax = height - 1;
        IntArray2 mask = new IntArray2(width, height);
        
        forEachEdgePixel((x, y) -> {
            int here = mask.get(x, y);
            //"if pos is not occupied in north yet and north of pos is fillable, expand north"
            
            //"if pos is not occupied in north yet and north of pos is fillable, expand north"
            if (y != ymax && (here & N) == 0 && fillCond.test(x, y + 1))
                spreadMaskSafely(mask, x, y + 1, xmax, ymax);
            
            if (x != xmax && (here & E) == 0 && fillCond.test(x + 1, y))
                spreadMaskSafely(mask, x + 1, y, xmax, ymax);
            
            if (y != 0 && (here & S) == 0 && fillCond.test(x, y - 1))
                spreadMaskSafely(mask, x, y - 1, xmax, ymax);
            
            if (x != 0 && (here & W) == 0 && fillCond.test(x - 1, y))
                spreadMaskSafely(mask, x - 1, y, xmax, ymax);
        });
        
        for (int x = 1; x < xmax; x++) {
            for (int y = 1; y < ymax; y++) {
                int here = mask.get(x, y);
                if (here == 0) continue;
                
                //"if pos is not occupied in north yet and north of pos is fillable, expand north"
                if ((here & N) == 0 && fillCond.test(x, y + 1))
                    spreadMask(mask, x, y + 1);
                if ((here & E) == 0 && fillCond.test(x + 1, y))
                    spreadMask(mask, x + 1, y);
                if ((here & S) == 0 && fillCond.test(x, y - 1))
                    spreadMask(mask, x, y - 1);
                if ((here & W) == 0 && fillCond.test(x - 1, y))
                    spreadMask(mask, x - 1, y);
            }
        }
        
        drawIf((x, y) -> mask.get(x, y) != 0, rgb);
    }
    
    public void spreadMaskSafely(IntArray2 mask, int x, int y, int xmax, int ymax) {
        //signalize to surrounding 4 pixels that dir is blocked
        if (y < ymax) mask.set(x, y + 1, mask.get(x, y + 1) | S);
        if (x < xmax) mask.set(x + 1, y, mask.get(x + 1, y) | W);
        if (y > 0) mask.set(x, y - 1, mask.get(x, y - 1) | N);
        if (x > 0) mask.set(x - 1, y, mask.get(x - 1, y) | E);
    }
    
    public void spreadMask(IntArray2 mask, int x, int y) {
        //signalize to surrounding 4 pixels that dir is blocked
        mask.set(x, y + 1, mask.get(x, y + 1) | S);
        mask.set(x + 1, y, mask.get(x + 1, y) | W);
        mask.set(x, y - 1, mask.get(x, y - 1) | N);
        mask.set(x - 1, y, mask.get(x - 1, y) | E);
    }
    
    // ITERATION
    
    /**
     * Performs an action for every pixel in this canvas.
     *
     * @param action the action
     */
    public void forEachPixel(Int2Consumer action) {
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                action.accept(x, y);
    }
    
    /**
     * <p>
     * Performs an action for every pixel that is on the edge of this canvas.
     * </p>
     * <p>
     * This is guaranteed to perform the action exactly once for every edge pixel.
     * </p>
     *
     * @param action the action
     */
    public void forEachEdgePixel(Int2Consumer action) {
        final int xmax = width - 1, ymax = height - 1;
        
        for (int x = 0; x < width; x++) {
            action.accept(x, 0);
            action.accept(x, ymax);
        }
        for (int y = 1; y < ymax; y++) {
            action.accept(0, y);
            action.accept(xmax, y);
        }
    }
    
}
