package net.grian.torrens.object;

import net.grian.spatium.function.Int2Consumer;
import net.grian.spatium.function.Int2IntFunction;
import net.grian.spatium.function.Int2Predicate;

/**
 * Object dedicated to drawing in {@link BaseTexture} objects.
 */
public class TextureGraphics {

    private final BaseTexture texture;
    private final int width, height;

    public TextureGraphics(BaseTexture texture) {
        this.texture = texture;
        this.width = texture.getWidth();
        this.height = texture.getHeight();
    }

    public BaseTexture getContent() {
        return texture;
    }

    /**
     * <p>
     *     Gives the given pixel a given rgb value (ARGB integer).
     * </p>
     * <p>
     *     Should the pixel coordinates be outside the texture boundaries, nothing is drawn and the input is ignored.
     * </p>
     *
     * @param x the pixel x-coordinate
     * @param y the pixel y-coordinate
     * @param rgb the ARGB value
     */
    public void draw(int x, int y, int rgb) {
        if (x >= 0 && y >= 0 && x < width && y < height)
            texture.set(x, y, rgb);
    }

    /**
     * <p>
     *     Gives the given pixel a given rgb value (ARGB integer).
     * </p>
     * <p>
     *     Should the pixel coordinates be outside the texture boundaries, nothing is drawn and the input is ignored.
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
        drawRaw((x,y) -> rgb);
    }

    /**
     * Directly draws a function into the texture for every pixel.
     *
     * @param function the function
     */
    public void drawRaw(Int2IntFunction function) {
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                draw(x, y, function.apply(x, y));
    }

    /**
     * Draws a shape with given rgb value into the texture.
     *
     * @param shape the shape
     * @param rgb the rgb value
     */
    public void drawShape(Int2Predicate shape, int rgb) {
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                if (shape.test(x, y)) draw(x, y, rgb);
    }

    /**
     * Draws a two-colored raster into the texture.
     *
     * @param rgb0 the first rgb value
     * @param rgb1 the second rgb value
     */
    public void drawRaster(int rgb0, int rgb1) {
        drawRaw(((x, y) -> (x%2 == y%2)? rgb0 : rgb1));
    }

    /**
     * Draws a two-colored raster into the texture.
     *
     * @param rgb0 the first rgb value
     * @param rgb1 the second rgb value
     */
    public void drawRaster(int rgb0, int rgb1, int tileSize) {
        if (tileSize < 1) return;
        drawRaw(((x, y) -> x/tileSize%2 == y/tileSize%2? rgb0 : rgb1));
    }

    public void drawRectangle(int rgb, int x0, int y0, int x1, int y1) {
        final int
                minX = Math.min(x0, x1), maxX = Math.max(x0, x1),
                minY = Math.min(y0, y1), maxY = Math.max(y0, y1);

        for (int x = minX; x<maxX; x++)
            for (int y = minY; y<maxY; y++)
                texture.set(x, y, rgb);
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
        final int dx = maxX-minX, dy = maxY-minY;
        int error = Math.max(dx, dy) / 2;

        if (dx >= dy) for (int x=minX, y=minY; x<=maxX; x++) {
            draw(x, y, rgb);

            error -= dy;
            if (error < 0) {
                error += dx;
                y++;
            }
        }
        else for (int x=minX, y=minY; y<=maxY; y++) {
            draw(x, y, rgb);

            error -= dx;
            if (error < 0) {
                error += dy;
                x++;
            }
        }
    }

    public void forEachPixel(Int2Consumer action) {
        for (int x = 0; x<width; x++)
            for (int y = 0; y<height; y++)
                action.accept(x, y);
    }

}
