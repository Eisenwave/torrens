package net.grian.torrens.object;

import net.grian.spatium.Spatium;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Data representation of a Wavefront Object Model.
 */
public class OBJModel {

    private final List<Vertex3f> vertices = new ArrayList<>();
    private final List<Vertex3f> normals = new ArrayList<>();
    private final List<Vertex2f> textures = new ArrayList<>();
    private final List<OBJFace> faces = new ArrayList<>();

    /**
     * Returns the vertex with the given index.
     *
     * @param index the index
     * @return the vertex with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    public Vertex3f getVertex(int index) {
        return vertices.get(index);
    }

    /**
     * Returns the normal vertex with the given index.
     *
     * @param index the index
     * @return the normal vertex with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    public Vertex3f getNormal(int index) {
        return normals.get(index);
    }

    /**
     * Returns the texture vertex with the given index.
     *
     * @param index the index
     * @return the texture vertex with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    public Vertex2f getTexture(int index) {
        return textures.get(index);
    }

    /**
     * Returns the face with the given index.
     *
     * @param index the index
     * @return the face with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 0 || index &gt;= size()</code>)
     */
    public OBJFace getFace(int index) {
        return faces.get(index);
    }

    /**
     * Returns the amount of vertices in this model.
     *
     * @return the amount of vertices
     */
    public int getVertexCount() {
        return vertices.size();
    }

    /**
     * Returns the amount of normal vertices in this model.
     *
     * @return the amount of normal vertices
     */
    public int getNormalCount() {
        return normals.size();
    }

    /**
     * Returns the amount of texture vertices in this model.
     *
     * @return the amount of texture vertices
     */
    public int getTextureVertexCount() {
        return textures.size();
    }

    /**
     * Returns the amount of faces in this model.
     *
     * @return the amount of faces
     */
    public int getFaceCount() {
        return faces.size();
    }

    /**
     * Adds a vertex to this model.
     *
     * @param vertex the vertex
     */
    public void addVertex(Vertex3f vertex) {
        Objects.requireNonNull(vertex);
        vertices.add(vertex);
    }

    /**
     * Adds a normal vertex to this model. This must be a unit vector.
     *
     * @param vertex the vertex
     */
    public void addNormal(Vertex3f vertex) {
        final float x = vertex.getX(), y = vertex.getY(), z = vertex.getZ();
        if (!Spatium.equals(1, x*x + y*y + z*z))
            throw new IllegalArgumentException("normal is not a unit vector");
        normals.add(vertex);
    }

    /**
     * Adds a texture vertex to this model.
     *
     * @param vertex the vertex
     */
    public void addTexture(Vertex2f vertex) {
        Objects.requireNonNull(vertex);
        textures.add(vertex);
    }

    /**
     * Adds a face to this model.
     *
     * @param face the face
     */
    public void addFace(OBJFace face) {
        OBJTriplet[] triplets = face.getShape();
        for (int i = 0; i<triplets.length; i++)
            validate(triplets[i], i);

        faces.add(face);
    }

    private void validate(OBJTriplet triplet, int index) {
        if (triplet.getVertexIndex() > getVertexCount())
            throw new IndexOutOfBoundsException("v"+index+": "+triplet.getVertexIndex());
        if (triplet.getNormalIndex() > getNormalCount())
            throw new IndexOutOfBoundsException("vn"+index+": "+triplet.getNormalIndex());
        if (triplet.getTextureIndex() > getTextureVertexCount())
            throw new IndexOutOfBoundsException("vt"+index+": "+triplet.getTextureIndex());
    }

}
