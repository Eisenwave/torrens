package net.grian.torrens.util.stl;

import net.grian.torrens.object.Vertex3f;

import java.util.Objects;

public class STLTriangle {
    
    private final Vertex3f normal, a, b, c;
    private final short attribute;
    
    public STLTriangle(Vertex3f normal, Vertex3f a, Vertex3f b, Vertex3f c, short attribute) {
        Objects.requireNonNull(normal);
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        Objects.requireNonNull(c);
        
        this.normal = normal;
        this.a = a;
        this.b = b;
        this.c = c;
        this.attribute = attribute;
    }
    
    public STLTriangle(Vertex3f normal, Vertex3f a, Vertex3f b, Vertex3f c) {
        this(normal, a, b, c, (short) 0);
    }
    
    /**
     * Returns the first vertex of the triangle.
     *
     * @return the first vertex of the triangle
     */
    public Vertex3f getA() {
        return a;
    }
    
    /**
     * Returns the second vertex of the triangle.
     *
     * @return the second vertex of the triangle
     */
    public Vertex3f getB() {
        return b;
    }
    
    /**
     * Returns the third vertex of the triangle.
     *
     * @return the third vertex of the triangle
     */
    public Vertex3f getC() {
        return c;
    }
    
    /**
     * Returns the normal of the triangle.
     *
     * @return the normal of the triangle
     */
    public Vertex3f getNormal() {
        return normal;
    }
    
    /**
     * <p>
     *     Returns the triangle attribute.
     * </p>
     * <p>
     *     This is an optional value which is being set to <code>0</code> by most applications.
     * </p>
     *
     * @return the triangle attribute
     */
    public short getAttribute() {
        return attribute;
    }
    
}
