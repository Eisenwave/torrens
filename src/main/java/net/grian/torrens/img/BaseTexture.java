package net.grian.torrens.img;

/**
 * A rectangular texture.
 */
public interface BaseTexture extends BaseRectangle {

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
     * Sets the rgb value at given u, v coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws IndexOutOfBoundsException if the index is out of bounds
     */
    abstract void set(int x, int y, int rgb);
    

}
