package net.grian.torrens.util.object;

import net.grian.spatium.geo2.Vector2;

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
    
    public Vertex2f(Vector2 v) {
        this((float) v.getX(), (float) v.getY());
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
    
    // CHECKERS
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex2f && equals((Vertex2f) obj);
    }
    
    public boolean equals(Vertex2f vertex) {
        return this.x == vertex.x && this.y == vertex.y;
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
