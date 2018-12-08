package eisenwave.torrens.stl;

import eisenwave.torrens.object.Vertex3f;
import eisenwave.torrens.object.Vertex3i;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A triangle in an {@link STLModel}.
 */
public class STLTriangle {
    
    private final Vertex3f normal, a, b, c;
    private final short attribute;
    
    /**
     * Constructs a new triangle.
     *
     * @param normal the triangle normal.
     * @param a the first vertex
     * @param b the second vertex
     * @param c the third vertex
     * @param attribute the attribute, a number which can have special meaning depending on software
     */
    public STLTriangle(@NotNull Vertex3f normal, @NotNull Vertex3f a, @NotNull Vertex3f b, @NotNull Vertex3f c,
                       short attribute) {
        this.normal = normal;
        this.a = a;
        this.b = b;
        this.c = c;
        this.attribute = attribute;
    }
    
    /**
     * Constructs a new triangle with the default attribute of {@code 0}.
     *
     * @param normal the triangle normal
     * @param a the first vertex
     * @param b the second vertex
     * @param c the third vertex
     */
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
     * Returns the triangle attribute.
     * <p>
     * This is an optional value which is being set to <code>0</code> by most applications.
     *
     * @return the triangle attribute
     */
    public short getAttribute() {
        return attribute;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() +
            "{a=" + getA() +
            ", b=" + getB() +
            ", c=" + getC() +
            ", n=" + getNormal() +
            "attr=" + Integer.toHexString(getAttribute()) + "}";
    }
    
}
