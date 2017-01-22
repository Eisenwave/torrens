package net.grian.torrens.object;

import java.util.Arrays;

/**
 * An immutable pair of integer coordinates.
 */
public class Vertex2i {

    @SuppressWarnings("unused")
    public final static Vertex2i ZERO = new Vertex2i(0, 0);

    private final int x, y;

    public Vertex2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Vertex2i && equals((Vertex2i) obj);
    }

    public boolean equals(Vertex2i vertex) {
        return this.x == vertex.x && this.y == vertex.y;
    }

}
