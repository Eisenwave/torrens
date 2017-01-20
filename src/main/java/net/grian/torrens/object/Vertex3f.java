package net.grian.torrens.object;

/**
 * An immutable triplet of float coordinates.
 */
public class Vertex3f {

    private final float x, y, z;

    public Vertex3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the vertex x-coordinate.
     *
     * @return vertex x-coordinate
     */
    public float getX() {
        return x;
    }

    /**
     * Returns the vertex y-coordinate.
     *
     * @return vertex y-coordinate
     */
    public float getY() {
        return y;
    }

    /**
     * Returns the vertex z-coordinate.
     *
     * @return vertex z-coordinate
     */
    public float getZ() {
        return z;
    }

}
