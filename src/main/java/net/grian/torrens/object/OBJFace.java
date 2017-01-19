package net.grian.torrens.object;

import java.io.Serializable;

public class OBJFace {

    private final OBJTriplet[] shape;

    /**
     * Constructs a new face from an array of {@link OBJTriplet} objects, forming the shape of the face.
     *
     * @param shape the shape of the face
     */
    public OBJFace(OBJTriplet... shape) {
        if (shape.length < 1)throw new IllegalArgumentException("face must have at least one point");
        this.shape = shape;
    }

    public OBJTriplet[] getShape() {
        return shape;
    }

    /**
     * Returns amount of vertices this face is made of.
     *
     * @return the amount of vertices
     */
    public int size() {
        return shape.length;
    }

    /**
     * A index triplet consisting of a vertex index, a normal index and a texture index.
     */
    public static class OBJTriplet implements Serializable {

        private final int v, vn, vt;

        public OBJTriplet(int v, int vn, int vt) {
            this.v = v;
            this.vn = vn;
            this.vt = vt;
        }

        public int getVertexIndex() {
            return v;
        }

        public int getNormalIndex() {
            return vn;
        }

        public int getTextureIndex() {
            return vt;
        }

    }

}
