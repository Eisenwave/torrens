package net.grian.torrens.io;

import net.grian.spatium.util.IOMath;
import net.grian.torrens.object.STLModel;
import net.grian.torrens.object.Vertex3f;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 *     A serializer for <b>Stereo-Lithography / Standard Tessellation Language (.stl)</b> files.
 * </p>
 * <p>
 *     No version restrictions exist.
 * </p>
 */
public class SerializerSTL implements Serializer<STLModel> {

    @Override
    public void serialize(STLModel model, OutputStream stream) throws IOException {
        DataOutputStream dataStream = new DataOutputStream(stream);
        serialize(model, dataStream);
        dataStream.close();
    }

    public void serialize(STLModel model, DataOutputStream stream) throws IOException {
        serializeHeader(model, stream);

        for (STLModel.STLTriangle triangle : model.getTriangles()) {
            serializeVertex(triangle.getNormal(), stream);
            serializeVertex(triangle.getA(), stream);
            serializeVertex(triangle.getB(), stream);
            serializeVertex(triangle.getC(), stream);
            stream.writeShort(IOMath.invertBytes(triangle.getAttribute()));
        }
    }

    private void serializeHeader(STLModel model, DataOutputStream stream) throws IOException {
        byte[] header = model.getHeader().getBytes();
        stream.write(header, 0, Math.min(header.length, 80));
        if (header.length < 80)
            stream.write(new byte[80 - header.length]);

        stream.writeInt(IOMath.invertBytes(model.size()));
    }

    private void serializeVertex(Vertex3f vertex, DataOutputStream stream) throws IOException {
        stream.writeInt( IOMath.invertBytes(Float.floatToIntBits(vertex.getX())) );
        stream.writeInt( IOMath.invertBytes(Float.floatToIntBits(vertex.getY())) );
        stream.writeInt( IOMath.invertBytes(Float.floatToIntBits(vertex.getZ())) );
    }

}