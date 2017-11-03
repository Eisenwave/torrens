package net.grian.torrens.util.stl;

import net.grian.torrens.io.Deserializer;
import net.grian.torrens.io.LittleDataInputStream;
import net.grian.torrens.object.Vertex3f;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *     A deserializer for <b>Stereo-Lithography / Standard-Tessellation-Language (.stl)</b> files.
 * </p>
 * <p>
 *     Only binary STL is supported, triangle attributes are given no special interpretation.
 * </p>
 */
public class DeserializerSTL implements Deserializer<STLModel> {

    @NotNull
    @Override
    public STLModel fromStream(InputStream stream) throws IOException {
        LittleDataInputStream dataStream = new LittleDataInputStream(stream);
        STLModel result = toStream(dataStream);
        dataStream.close();
        return result;
    }

    public STLModel toStream(LittleDataInputStream stream) throws IOException {
        String header = deserializeHeader(stream);
        STLModel model = new STLModel(header);

        final int size = stream.readLittleInt();
        for (int i = 0; i<size; i++)
            model.add(deserializeTriangle(stream));

        return model;
    }

    private String deserializeHeader(LittleDataInputStream stream) throws IOException {
        byte[] bytes = new byte[80];
        if (stream.read(bytes) < 80) throw new IOException();
        return new String(bytes);
    }

    private STLTriangle deserializeTriangle(LittleDataInputStream stream) throws IOException {
        return new STLTriangle(
            deserializeVertex(stream),
            deserializeVertex(stream),
            deserializeVertex(stream),
            deserializeVertex(stream),
            stream.readShort());
    }

    private Vertex3f deserializeVertex(LittleDataInputStream stream) throws IOException {
        return new Vertex3f(
            stream.readLittleFloat(),
            stream.readLittleFloat(),
            stream.readLittleFloat());
    }

}
