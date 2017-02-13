package net.grian.torrens.stl;

import net.grian.torrens.io.LittleDataOutputStream;
import net.grian.torrens.io.Serializer;
import net.grian.torrens.object.Vertex3f;

import javax.annotation.Nullable;
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
        try (LittleDataOutputStream dataStream = new LittleDataOutputStream(stream)) {
            toStream(model, dataStream);
        }
    }

    public void toStream(STLModel model, LittleDataOutputStream stream) throws IOException {
        debug("serializing "+model+" ...");
        serializeHeader(model, stream);

        for (STLModel.STLTriangle triangle : model.getTriangles()) {
            serializeVertex(triangle.getNormal(), stream);
            serializeVertex(triangle.getA(), stream);
            serializeVertex(triangle.getB(), stream);
            serializeVertex(triangle.getC(), stream);
            stream.writeLittleShort(triangle.getAttribute());
        }
    }

    private void serializeHeader(STLModel model, LittleDataOutputStream stream) throws IOException {
        byte[] header = model.getHeader().getBytes();
        stream.write(header, 0, Math.min(header.length, 80));
        if (header.length < 80)
            stream.write(new byte[80 - header.length]);

        stream.writeLittleInt(model.size());
    }

    private void serializeVertex(Vertex3f vertex, LittleDataOutputStream stream) throws IOException {
        stream.writeLittleFloat(vertex.getX());
        stream.writeLittleFloat(vertex.getY());
        stream.writeLittleFloat(vertex.getZ());
    }

}
