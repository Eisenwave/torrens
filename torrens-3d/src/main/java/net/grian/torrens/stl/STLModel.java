package net.grian.torrens.util.stl;

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

    //GETTERS

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

    //SETTERS

    public boolean add(STLTriangle triangle) {
        return triangles.add(triangle);
    }

    public void setHeader(String header) {
        if (header.startsWith("solid")) throw new IllegalArgumentException("header must not start with 'solid'");
        this.header = header;
    }
    
    //MISC
    
    @Override
    public String toString() {
        return STLModel.class.getSimpleName()+ "{triangles="+size()+"}";
    }

}
