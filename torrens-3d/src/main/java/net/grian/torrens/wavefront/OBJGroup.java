package net.grian.torrens.wavefront;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class OBJGroup implements Iterable<OBJFace> {
    
    private final List<OBJFace> faces = new ArrayList<>();
    
    private final OBJModel model;
    private final String name;
    
    @Nullable
    private String material;
    
    public OBJGroup(@NotNull OBJModel model, @NotNull String name, @Nullable String material) {
        this.model = Objects.requireNonNull(model);
        this.name = Objects.requireNonNull(name);
        this.material = material;
    }
    
    public OBJGroup(OBJModel model, String name) {
        this(model, name, null);
    }
    
    public OBJModel getModel() {
        return model;
    }
    
    /**
     * Returns the name of this group.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Returns the amount of faces in this model.
     *
     * @return the amount of faces
     */
    public int getFaceCount() {
        return faces.size();
    }
    
    @Nullable
    public String getMaterial() {
        return material;
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
    
    // CHECKERS
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || obj instanceof OBJGroup && equals((OBJGroup) obj);
    }
    
    public boolean equals(OBJGroup group) {
        return this.model.equals(group.model) && this.name.equals(group.name);
    }
    
    // SETTERS
    
    public void setMaterial(@Nullable String material) {
        this.material = material;
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
    
    //MISC
    
    @Override
    public String toString() {
        return OBJGroup.class.getSimpleName()+
            "{name=\""+getName()+"\""+
            ",|f|="+getFaceCount()+
            ",mtl="+getMaterial()+"}";
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
    
    @NotNull
    @Override
    public Iterator<OBJFace> iterator() {
        return faces.iterator();
    }
    
    // UTIL
    
    private void validate(OBJTriplet triplet, int index) {
        if (triplet.getVertexIndex() > model.getVertexCount())
            throw new IndexOutOfBoundsException("v"+index+": "+triplet.getVertexIndex());
        if (triplet.getNormalIndex() > model.getNormalCount())
            throw new IndexOutOfBoundsException("vn"+index+": "+triplet.getNormalIndex());
        if (triplet.getTextureIndex() > model.getTextureVertexCount())
            throw new IndexOutOfBoundsException("vt"+index+": "+triplet.getTextureIndex());
    }
    
}
