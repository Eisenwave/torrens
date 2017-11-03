package net.grian.torrens.stl;

import net.grian.torrens.object.BoundingBox6f;
import net.grian.torrens.object.Vertex3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Object representation of a <i><b>ST</b>ereo<b>L</b>ithography</i> /
 * <i><b>S</b>tandard <b>T</b>essellation <b>L</b>anguage</i>.
 */
public class STLModel {

    private final List<STLTriangle> triangles = new ArrayList<>();

    private String header;

    public STLModel(String header) {
        setHeader(header);
    }

    public STLModel() {
        this("");
    }

    // GETTERS

    public List<STLTriangle> getTriangles() {
        return triangles;
    }

    /**
     * Returns the amount of triangles in this model.
     *
     * @return the amount of triangles
     */
    public int size() {
        return triangles.size();
    }

    /**
     * Returns the model header.
     *
     * @return the model header
     */
    public String getHeader() {
        return header;
    }
    
    public BoundingBox6f getBoundaries() {
        if (isEmpty()) throw new IllegalStateException("empty models have no boundaries");
        float
            minX =  Float.MAX_VALUE, minY =  Float.MAX_VALUE, minZ =  Float.MAX_VALUE,
            maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE, maxZ = -Float.MAX_VALUE;
        
        for (STLTriangle triangle : triangles) {
            Vertex3f a = triangle.getA(), b = triangle.getB(), c = triangle.getC();
            final float
                ax = a.getX(), ay = a.getY(), az = a.getZ(),
                bx = b.getX(), by = b.getY(), bz = b.getZ(),
                cx = c.getX(), cy = c.getX(), cz = c.getZ();
            
            minX = Math.min(minX, Math.min(ax, Math.min(bx, cx)));
            minY = Math.min(minY, Math.min(ay, Math.min(by, cy)));
            minZ = Math.min(minZ, Math.min(az, Math.min(bz, cz)));
            maxX = Math.max(maxX, Math.max(ax, Math.max(bx, cx)));
            maxY = Math.max(maxY, Math.max(ay, Math.max(by, cy)));
            maxZ = Math.max(maxZ, Math.max(az, Math.max(bz, cz)));
        }
        
        return new BoundingBox6f(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    // PREDICATES
    
    /**
     * Returns whether this model contains no triangles.
     * 
     * @return whether this model is empty
     */
    public boolean isEmpty() {
        return triangles.isEmpty();
    }

    // MUTATORS

    public boolean add(STLTriangle triangle) {
        return triangles.add(triangle);
    }

    public void setHeader(String header) {
        if (header.startsWith("solid")) throw new IllegalArgumentException("header must not start with 'solid'");
        this.header = header;
    }
    
    // MISC
    
    @Override
    public String toString() {
        return STLModel.class.getSimpleName()+ "{triangles="+size()+"}";
    }

}
