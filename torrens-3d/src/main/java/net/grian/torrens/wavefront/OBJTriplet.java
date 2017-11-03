package net.grian.torrens.wavefront;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * A index triplet consisting of a vertex index, a normal index and a texture index.
 */
public class OBJTriplet implements Serializable, Cloneable {
    
    @NotNull
    public static OBJTriplet parse(String str) {
        if (str.isEmpty()) throw new IllegalArgumentException("empty string");
        String[] indices = str.split("/");
        if (indices.length < 1) throw new IllegalArgumentException("empty parts ("+str+")");
        
        int v = Integer.parseInt(indices[0]);
        if (indices.length < 2) return new OBJTriplet(v, 0, 0);
        
        int vt = indices[1].isEmpty()? 0 : Integer.parseInt(indices[1]);
        if (indices.length < 3) return new OBJTriplet(v, vt, 0);
        
        int vn = indices[2].isEmpty()? 0 : Integer.parseInt(indices[2]);
        return new OBJTriplet(v, vt, vn);
    }

    private final int v, vt, vn;

    public OBJTriplet(int v, int vt, int vn) {
        if (v < 1) throw new IllegalArgumentException("v must be at least 1 (is "+v+")");
        this.v = v;
        this.vt = vt;
        this.vn = vn;
    }
    
    public OBJTriplet(OBJTriplet copyOf) {
        this.v = copyOf.v;
        this.vt = copyOf.vt;
        this.vn = copyOf.vn;
    }
    
    // GETTERS
    
    /**
     * Returns the vertex index of the triplet.
     *
     * @return the vertex index
     */
    public int getVertexIndex() {
        return v;
    }
    
    /**
     * Returns the normal index of the triplet or 0 if it has none.
     *
     * @return the vertex index
     */
    public int getNormalIndex() {
        return vn;
    }
    
    /**
     * Returns the texture index of the triplet or 0 if it has none
     *
     * @return the texture index
     */
    public int getTextureIndex() {
        return vt;
    }
    
    // CHECKERS
    
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj) || obj instanceof OBJTriplet && equals((OBJTriplet) obj);
    }
    
    public boolean equals(OBJTriplet triplet) {
        return
            this.v  == triplet.v || (this.v < 1  && triplet.v < 1) &&
                this.vt == triplet.v || (this.vt < 1 && triplet.vt < 1) &&
                this.vn == triplet.v || (this.vn < 1 && triplet.vn < 1);
    }
    
    // MISC
    
    @Override
    public OBJTriplet clone() {
        return new OBJTriplet(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(v);

        if (vt > 0) {
            builder.append("/");
            builder.append(vt);

            if (vn > 0) {
                builder.append("/");
                builder.append(vn);
            }
        }

        else if (vn > 0) {
            builder.append("//");
            builder.append(vn);
        }

        return builder.toString();
    }

}
