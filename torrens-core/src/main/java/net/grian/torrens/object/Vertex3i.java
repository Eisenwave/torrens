package net.grian.torrens.util.object;

import java.io.Serializable;
import java.util.Arrays;

/**
 * An immutable triplet of integer coordinates.
 */
public class Vertex3i implements Serializable {

    @SuppressWarnings("unused")
    public final static Vertex3i ZERO = new Vertex3i(0, 0, 0);

    private final int x, y, z;

    public Vertex3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex3i(Vertex3i vertex) {
        this(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    // GETTER
    
    /**
     * Returns the vertex x-coordinate.
     *
     * @return vertex x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the vertex y-coordinate.
     *
     * @return vertex y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the vertex z-coordinate.
     *
     * @return vertex z-coordinate
     */
    public int getZ() {
        return z;
    }
    
    // CHECKERS
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex3i && equals((Vertex3i) obj);
    }
    
    public boolean equals(Vertex3i vertex) {
        return this.x == vertex.x && this.y == vertex.y && this.z == vertex.z;
    }
    
    // MISC
    
    /**
     * Returns the coordinates of this vertex in an array of length 3.
     *
     * @return the coordinates of this vertex
     */
    public int[] toArray() {
        return new int[] {x, y, z};
    }

    @Override
    public String toString() {
        return Vertex3i.class.getSimpleName()+"["+x+","+y+","+z+"]";
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }
    
}
