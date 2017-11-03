package net.grian.torrens.object;

import java.io.Serializable;
import java.util.Arrays;

/**
 * An immutable pair of floating point coordinates.
 */
public class Vertex2f implements Serializable {

    @SuppressWarnings("unused")
    public final static Vertex2f ZERO = new Vertex2f(0, 0);

    private final float x, y;

    public Vertex2f(float x, float y) {
        this.x = x;
        this.y = y;
    }
    
    // GETTERS

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
    
    // PREDICATES
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex2f && equals((Vertex2f) obj);
    }
    
    public boolean equals(Vertex2f vertex) {
        return this.x == vertex.x && this.y == vertex.y;
    }
    
    // OPERATIONS
    
    public Vertex2f midPoint(Vertex2f v) {
        return new Vertex2f(
            (this.x + v.x) * 0.5F,
            (this.y + v.y) * 0.5F);
    }
    
    // MISC
    
    /**
     * Returns the coordinates of this vertex in an array of length 3.
     *
     * @return the coordinates of this vertex
     */
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

}
