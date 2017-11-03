package net.grian.torrens.wavefront;

import net.grian.spatium.util.Spatium;
import net.grian.torrens.object.BoundingBox6f;
import net.grian.torrens.object.Vertex2f;
import net.grian.torrens.object.Vertex3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * Data representation of a Wavefront Object Model.
 */
public class OBJModel implements Serializable {

    private final List<Vertex3f> vertices = new ArrayList<>();
    private final List<Vertex3f> normals = new ArrayList<>();
    private final List<Vertex2f> textures = new ArrayList<>();
    
    @NotNull
    private final OBJGroup defGroup;
    private final Set<OBJGroup> groups = new HashSet<>();

    private MTLLibrary mtllib;
    
    public OBJModel() {
        this.defGroup = new OBJGroup(this, "Default");
        groups.add(defGroup);
    }

    // GETTERS
    
    /**
     * Returns the this model's material library or null if it has none.
     *
     * @return this model's material library
     */
    @Nullable
    public MTLLibrary getMaterials() {
        return mtllib;
    }

    /**
     * Returns the vertex with the given index.
     *
     * @param index the index
     * @return the vertex with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 1 || index &gt;= size()</code>)
     */
    public Vertex3f getVertex(int index) {
        if (index < 1) throw new IndexOutOfBoundsException("index < 1");
        return vertices.get(index-1);
    }

    /**
     * Returns the normal vertex with the given index.
     *
     * @param index the index
     * @return the normal vertex with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 1 || index &gt;= size()</code>)
     */
    public Vertex3f getNormal(int index) {
        if (index < 1) throw new IndexOutOfBoundsException("index < 1");
        return normals.get(index-1);
    }

    /**
     * Returns the texture vertex with the given index.
     *
     * @param index the index
     * @return the texture vertex with the given index
     * @throws IndexOutOfBoundsException if the index is out of range (<code>index &lt; 1 || index &gt;= size()</code>)
     */
    public Vertex2f getTexture(int index) {
        if (index < 1) throw new IndexOutOfBoundsException("index < 1");
        return textures.get(index-1);
    }
    
    @NotNull
    public OBJGroup getDefaultGroup() {
        return defGroup;
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
        int result = getDefaultGroup().getFaceCount();
        for (OBJGroup group : groups)
            result += group.getFaceCount();
        
        return result;
    }

    public int getGroupCount() {
        return groups.size();
    }
    
    @NotNull
    public Set<OBJGroup> getGroups() {
        return Collections.unmodifiableSet(groups);
    }
    
    public BoundingBox6f getBoundaries() {
        float
            minX = 0, minY = 0, minZ = 0,
            maxX = 0, maxY = 0, maxZ = 0;
        
        for (Vertex3f vertex : vertices) {
            float x = vertex.getX(), y = vertex.getY(), z = vertex.getZ();
            //else if statements based on the assumption a coordinate can not be < min AND > max
            if (x < minX) minX = x;
            else if (x > maxX) maxX = x;
            if (y < minY) minY = y;
            else if (y > maxY) maxY = y;
            if (z < minZ) minZ = z;
            else if (z > maxZ) maxZ = z;
        }
        
        return new BoundingBox6f(minX, minY, minZ, maxX, maxY, maxZ);
    }

    // PREDICATES

    /**
     * Returns whether this model has an attached material library.
     *
     * @return whether this model has materials
     */
    public boolean hasMaterials() {
        return mtllib != null;
    }

    // MUTATORS

    /**
     * Sets the material library of this model.
     *
     * @param mtllib the material library
     */
    public void setMaterials(@NotNull MTLLibrary mtllib) {
        this.mtllib = mtllib;
    }

    /**
     * Adds a vertex to this model.
     *
     * @param vertex the vertex
     */
    public void addVertex(Vertex3f vertex) {
        vertices.add(Objects.requireNonNull(vertex));
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
        textures.add(Objects.requireNonNull(vertex));
    }
    
    /**
     * Adds a group to this model.
     *
     * @param group the group
     */
    public void addGroup(OBJGroup group) {
        groups.add(Objects.requireNonNull(group));
    }
    
    // ITERATION
    
    /**
     * Performs an action for every vertex in the model.
     *
     * @param action the action
     */
    public void forEachVertex(Consumer<Vertex3f> action) {
        for (Vertex3f v : vertices)
            action.accept(v);
    }
    
    /**
     * Performs an action for every face in the model.
     *
     * @param action the action
     */
    public void forEachFace(Consumer<OBJFace> action) {
        for (OBJGroup group : groups)
            group.forEach(action);
    }
    
    //MISC

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(OBJModel.class.getSimpleName());
        builder
            .append("{mtllib=").append(getMaterials())
            .append(",|v|=").append(getVertexCount())
            .append(",|vt|=").append(getTextureVertexCount())
            .append(",|vn|=").append(getNormalCount())
            .append(",|f|=").append(getFaceCount())
            .append(",g=[").append(getDefaultGroup());
        
        groups.forEach(group -> builder.append(',').append(group));
        
        return builder.append("]}").toString();
    }

    

}
