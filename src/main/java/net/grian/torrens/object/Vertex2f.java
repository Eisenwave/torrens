package net.grian.torrens.object;

import java.util.Arrays;

/**
 * An immutable pair of float coordinates.
 */
public class Vertex2f {

    @SuppressWarnings("unused")
    public final static Vertex2f ZERO = new Vertex2f(0, 0);

    private final float x, y;

    public Vertex2f(float x, float y) {
        this.x = x;
        this.y = y;
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

    public float[] toArray() {
        return new float[] {x, y};
    }

    @Override
    public String toString() {
        return Vertex2f.class.getSimpleName()+"["+x+","+y+"]";
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex2f && equals((Vertex2f) obj);
    }

    public boolean equals(Vertex2f vertex) {
        return this.x == vertex.x && this.y == vertex.y;
    }

}
