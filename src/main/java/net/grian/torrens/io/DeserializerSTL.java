package net.grian.torrens.io;

import net.grian.spatium.util.IOMath;
import net.grian.torrens.object.STLModel;
import net.grian.torrens.object.Vertex3f;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *     A deserializer for <b>Stereo-Lithography / Standard-Tessellation-Language (.stl)</b> files.
 * </p>
 * <p>
 *     No version restrictions exist, triangle attributes are given no special interpretation.
 * </p>
 */
public class DeserializerSTL implements Deserializer<STLModel> {

    @Override
    public STLModel deserialize(InputStream stream) throws IOException {
        DataInputStream dataStream = new DataInputStream(stream);
        STLModel result = deserialize(dataStream);
        dataStream.close();
        return result;
    }

    public STLModel deserialize(DataInputStream stream) throws IOException {
        String header = deserializeHeader(stream);
        STLModel model = new STLModel(header);

        final int size = IOMath.invertBytes(stream.readInt());
        for (int i = 0; i<size; i++)
            model.add(deserializeTriangle(stream));

        return model;
    }

    private String deserializeHeader(DataInputStream stream) throws IOException {
        byte[] bytes = new byte[80];
        if (stream.read(bytes) < 80) throw new IOException();
        return new String(bytes);
    }

    private STLModel.STLTriangle deserializeTriangle(DataInputStream stream) throws IOException {
        return new STLModel.STLTriangle(
                deserializeVertex(stream),
                deserializeVertex(stream),
                deserializeVertex(stream),
                deserializeVertex(stream),
                stream.readShort());
    }

    private Vertex3f deserializeVertex(DataInputStream stream) throws IOException {
        return new Vertex3f(
                readLittleFloat(stream),
                readLittleFloat(stream),
                readLittleFloat(stream));
    }

    private float readLittleFloat(DataInputStream stream) throws IOException {
        return Float.intBitsToFloat(IOMath.invertBytes(stream.readInt()));
    }

}
