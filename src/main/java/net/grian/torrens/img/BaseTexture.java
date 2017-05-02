package net.grian.torrens.img;

import net.grian.spatium.function.Int2Consumer;
import net.grian.spatium.util.ColorMath;

import java.awt.*;

/**
 * A rectangular texture.
 */
public interface BaseTexture extends BaseRectangle {

    // GETTERS
    
    /**
     * Returns the rgb value at given u, v coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the rgb value at the coordinates
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    abstract int get(int x, int y);
    
    /**
     * Writes an ARGB top-to-bottom array of a given region into a given array.
     *
     * @param x the x-start in the texture
     * @param y the y-start in the texture
     * @param width the width of the region
     * @param height the height of the region
     * @param arr the array to be written into
     * @param offset the offset in the written array
     * @throws IllegalArgumentException if width, height or offset are negative
     */
    default void get(int x, int y, int width, int height, int[] arr, int offset) {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                arr[offset + i + j*width] = get(x+i, y+j);
    }
    
    /**
     * Returns ARGB top-to-bottom array of a given region.
     *
     * @param x the x-start in the texture
     * @param y the y-start in the texture
     * @param width the width of the region
     * @param height the height of the region
     * @throws IllegalArgumentException if width or height are negative
     */
    default int[] get(int x, int y, int width, int height) {
        int[] arr = new int[width * height];
        get(x, y, width, height, arr, 0);
        return arr;
    }

    default int getTransparency() {
        final int limX = getWidth(), limY = getHeight();
        
        boolean opaque = true;
        for (int x = 0; x < limX; x++) for (int y = 0; y < limY; y++) {
            final int trans = ColorMath.getTransparency(get(x,y));
            if (trans == Transparency.TRANSLUCENT)
                return trans;
            
            else if (trans == Transparency.BITMASK && opaque)
                opaque = false;
        }
        
        return opaque? Transparency.OPAQUE : Transparency.BITMASK;
    }
    
    // SETTERS
    
    /**
     * Sets the rgb value at given u, v coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    abstract void set(int x, int y, int rgb);
    
    /**
     * Swaps the rgb values of two pixels.
     */
    default void swap(int x0, int y0, int x1, int y1) {
        int swap = get(x0, y0);
        set(x0, y0, get(x1, y1));
        set(x1, y1, swap);
    }
    
    // ITERATION
    
    default void forEachPosition(Int2Consumer action) {
        final int limX = getWidth(), limY = getHeight();
        
        for (int x = 0; x<limX; x++)
            for (int y = 0; y<limY; y++)
                action.accept(x, y);
    }
    

}
