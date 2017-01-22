package net.grian.torrens.object;

/**
 * A index triplet consisting of a vertex index, a normal index and a texture index.
 */
public class OBJTriplet {

    private final int v, vt, vn;

    public OBJTriplet(int v, int vt, int vn) {
        if (v < 1) throw new IllegalArgumentException("v must be at least 1 (is "+v+")");
        this.v = v;
        this.vt = vt;
        this.vn = vn;
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
