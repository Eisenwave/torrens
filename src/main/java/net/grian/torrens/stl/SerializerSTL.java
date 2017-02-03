package net.grian.torrens.stl;

import net.grian.spatium.util.IOMath;
import net.grian.torrens.io.Serializer;
import net.grian.torrens.object.Vertex3f;

import javax.annotation.Nullable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * <p>
 *     A serializer for <b>Stereo-Lithography / Standard Tessellation Language (.stl)</b> files.
 * </p>
 * <p>
 *     This serializer produces binary STL files.
 * </p>
 */
public class SerializerSTL implements Serializer<STLModel> {
    
    @Nullable
    private final Logger logger;
    
    public SerializerSTL(Logger logger) {
        this.logger = logger;
    }
    
    public SerializerSTL() {
        this(null);
    }
    
    private void debug(String msg) {
        if (logger != null)
            logger.fine(msg);
    }

    @Override
    public void toStream(STLModel model, OutputStream stream) throws IOException {
        try (DataOutputStream dataStream = new DataOutputStream(stream)) {
            toStream(model, dataStream);
        }
    }

    public void toStream(STLModel model, DataOutputStream stream) throws IOException {
        debug("serializing "+model+" ...");
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
