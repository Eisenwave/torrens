package net.grian.torrens.util.object;

import java.io.Serializable;
import java.util.Arrays;

/**
 * An immutable pair of integer coordinates.
 */
public class Vertex2i implements Serializable {

    @SuppressWarnings("unused")
    public final static Vertex2i ZERO = new Vertex2i(0, 0);

    private final int x, y;

    public Vertex2i(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // GETTERS

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
    
    // CHECKERS
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex2i && equals((Vertex2i) obj);
    }
    
    public boolean equals(Vertex2i vertex) {
        return this.x == vertex.x && this.y == vertex.y;
    }
    
    // MISC
    
    /**
     * Returns the coordinates of this vertex in an array of length 3.
     *
     * @return the coordinates of this vertex
     */
    public int[] toArray() {
        return new int[] {x, y};
    }

    @Override
    public String toString() {
        return Vertex2i.class.getSimpleName()+"["+x+","+y+"]";
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(toArray());
    }

}
