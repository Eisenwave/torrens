package eisenwave.torrens.object;

import java.io.Serializable;
import java.util.Arrays;

/**
 * An immutable triplet of floating point coordinates.
 */
public class Vertex3f implements Serializable {

    @SuppressWarnings("unused")
    public final static Vertex3f ZERO = new Vertex3f(0, 0, 0);

    private final float x, y, z;

    public Vertex3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex3f(Vertex3f vertex) {
        this(vertex.getX(), vertex.getY(), vertex.getZ());
    }

    public Vertex3f(Vertex3i vertex) {
        this(vertex.getX(), vertex.getY(), vertex.getZ());
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

    /**
     * Returns the vertex z-coordinate.
     *
     * @return vertex z-coordinate
     */
    public float getZ() {
        return z;
    }

    public Vertex3f plus(float x, float y, float z) {
        return new Vertex3f(this.x+x, this.y+y, this.z+z);
    }
    
    public Vertex3f plus(Vertex3f v) {
        return plus(v.x, v.y, v.z);
    }

    public Vertex3f minus(float x, float y, float z) {
        return new Vertex3f(this.x-x, this.y-y, this.z-z);
    }
    
    public Vertex3f minus(Vertex3f v) {
        return minus(v.x, v.y, v.z);
    }

    public Vertex3f divided(float x, float y, float z) {
        return new Vertex3f(this.x/x, this.y/y, this.z/z);
    }

    public Vertex3f divided(float divisor) {
        return divided(divisor, divisor, divisor);
    }

    public Vertex3f multiplied(float x, float y, float z) {
        return new Vertex3f(this.x*x, this.y*y, this.z*z);
    }

    public Vertex3f multiplied(float factor) {
        return multiplied(factor, factor, factor);
    }

    public Vertex3f normalized() {
        return withLength(1);
    }
    
    public Vertex3f negative() {
        return new Vertex3f(-x, -y, -z);
    }
    
    public Vertex3f midPoint(Vertex3f v) {
        return new Vertex3f(
            (this.x + v.x) * 0.5F,
            (this.y + v.y) * 0.5F,
            (this.z + v.z) * 0.5F);
    }
    
    public float getLengthSquared() {
        return x*x + y*y + z*z;
    }
    
    public float getLength() {
        return (float) Math.sqrt(x*x + y*y + z*z);
    }

    public Vertex3f withLength(float length) {
        double factor = length / Math.sqrt(x*x + y*y + z*z);
        return new Vertex3f((float) (x*factor), (float) (y*factor), (float) (z*factor));
    }
    
    public Vertex3f cross(float x, float y, float z) {
        return new Vertex3f(
            this.y * z - this.z * y,
            this.z * x - this.x * z,
            this.x * y - this.y * x);
    }
    
    public Vertex3f cross(Vertex3f v) {
        return cross(v.getX(), v.getY(), v.getZ());
    }
    
    // MISC
    
    /**
     * Returns the coordinates of this vertex in an array of length 3.
     *
     * @return the coordinates of this vertex
     */
    public float[] toArray() {
        return new float[] {x, y, z};
    }

    @Override
    public String toString() {
        return String.format("[%.4f, %.4f, %.4f]", x, y, z);
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
