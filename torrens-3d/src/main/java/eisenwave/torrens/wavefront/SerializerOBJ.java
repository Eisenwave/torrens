package eisenwave.torrens.wavefront;

import eisenwave.torrens.io.TextSerializer;
import eisenwave.torrens.object.Vertex2f;
import eisenwave.torrens.object.Vertex3f;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 *     A serializer for <b>Wavefront (.obj)</b> files.
 * </p>
 */
@SuppressWarnings("SpellCheckingInspection")
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
        final MTLLibrary materials = object.getMaterials();

        writeHeader(writer);
        if (materials != null) {
            writer.newLine(); writer.write("mtllib "+materials.getName());
        }

        writeVertices(writer);

        writer.newLine(); writer.write("s off");
        if (materials != null)
            writeUseMaterial(materials, writer);
        
        writeGroups(writer);
    }

    private void writeUseMaterial(MTLLibrary materials, BufferedWriter writer) throws IOException {
        if (!materials.isEmpty()) {
            MTLMaterial material = materials.iterator().next();
            writer.newLine();
            writer.write("usemtl "+material.getName());
        }
    }

    private void writeVertices(BufferedWriter writer) throws IOException {
        final int limV = model.getVertexCount();
        for (int i = 1; i <= limV; i++)
            writeVertex("v", model.getVertex(i), writer);

        final int limVN = model.getNormalCount();
        for (int i = 1; i <= limVN; i++)
            writeVertex("vn", model.getNormal(i), writer);

        final int limVT = model.getTextureVertexCount();
        for (int i = 1; i <= limVT; i++)
            writeVertex("vt", model.getTexture(i), writer);
    }

    private void writeGroups(BufferedWriter writer) throws IOException {
        for (OBJGroup group : model.getGroups()) {
            writeGroup(group, writer);
        }
    }
    
    private void writeGroup(OBJGroup group, BufferedWriter writer) throws IOException {
        final int limit = group.getFaceCount();
        if (limit == 0) return;
        
        String material = group.getMaterial();
        if (material != null) {
            writer.newLine();
            writer.write("usemtl ");
            writer.write(material);
        }
        
        for (int i = 0; i<limit; i++)
            writeFace(group.getFace(i), writer);
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
