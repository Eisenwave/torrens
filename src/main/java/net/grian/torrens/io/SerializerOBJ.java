package net.grian.torrens.io;

import net.grian.torrens.object.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 *     A serializer for <b>Wavefront (.obj)</b> files.
 * </p>
 */
public class SerializerOBJ implements TextSerializer<OBJModel> {

    private OBJModel model;

    @Override
    public void toWriter(OBJModel object, Writer writer) throws IOException {
        try (BufferedWriter buffWriter = new BufferedWriter(writer)) {
            toWriter(object, buffWriter);
        }
    }

    public void toWriter(OBJModel object, BufferedWriter writer) throws IOException {
        this.model = object;

        writeHeader(writer);
        writeVertices(writer);

        writer.newLine(); writer.write("s off");

        writeFaces(writer);
    }

    private void writeVertices(BufferedWriter writer) throws IOException {
        final int limV = model.getVertexCount();
        for (int i = 0; i<limV; i++)
            writeVertex("v", model.getVertex(i), writer);

        final int limVN = model.getNormalCount();
        for (int i = 0; i<limVN; i++)
            writeVertex("vn", model.getNormal(i), writer);

        final int limVT = model.getTextureVertexCount();
        for (int i = 0; i<limVT; i++)
            writeVertex("vt", model.getTexture(i), writer);
    }

    private void writeFaces(BufferedWriter writer) throws IOException {
        final int lim = model.getFaceCount();
        for (int i = 0; i<lim; i++)
            writeFace(model.getFace(i), writer);
    }

    private void writeHeader(BufferedWriter writer) throws IOException {
        writer.write("# Generated using Grian Network software");
        writer.newLine();
        writer.write("# www.grian.net");
    }

    private void writeVertex(String prefix, Vertex3f vertex, BufferedWriter writer) throws IOException {
        writer.newLine();
        writer.write(prefix);
        writer.write(" ");
        writer.write(Float.toString(vertex.getX()));
        writer.write(" ");
        writer.write(Float.toString(vertex.getY()));
        writer.write(" ");
        writer.write(Float.toString(vertex.getZ()));
    }

    private void writeVertex(String prefix, Vertex2f vertex, BufferedWriter writer) throws IOException {
        writer.newLine();
        writer.write(prefix);
        writer.write(" ");
        writer.write(Float.toString(vertex.getX()));
        writer.write(" ");
        writer.write(Float.toString(vertex.getY()));
    }

    private void writeFace(OBJFace face, BufferedWriter writer) throws IOException {
        writer.newLine();
        writer.write("f");
        for (OBJTriplet triplet : face.getShape()) {
            writer.write(" ");
            writer.write(triplet.toString());
        }
    }

}
