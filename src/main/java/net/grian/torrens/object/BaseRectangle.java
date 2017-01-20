package net.grian.torrens.object;

public interface BaseRectangle {

    /**
     * Returns the rectangle width.
     *
     * @return the rectangle width
     */
    public abstract int getWidth();

    /**
     * Returns the rectangle height.
     *
     * @return the rectangle height
     */
    public abstract int getHeight();

    /**
     * Returns the area <b>A</b> of the rectangle: <code>A = width * height</code>
     *
     * @return the area
     */
    public default int getArea() {
        return getWidth() * getHeight();
    }

}