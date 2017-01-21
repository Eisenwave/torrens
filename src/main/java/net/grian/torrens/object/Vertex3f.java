package net.grian.torrens.object;

import java.util.Arrays;

/**
 * An immutable triplet of float coordinates.
 */
public class Vertex3f {

    @SuppressWarnings("unused")
    public final static Vertex3f ZERO = new Vertex3f(0, 0, 0);

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

    public float[] toArray() {
        return new float[] {x, y, z};
    }

    @Override
    public String toString() {
        return Vertex3f.class.getSimpleName()+"["+x+","+y+","+z+"]";
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex3f && equals((Vertex3f) obj);
    }

    public boolean equals(Vertex3f vertex) {
        return this.x == vertex.x && this.y == vertex.y && this.z == vertex.z;
    }

}
