package net.grian.torrens.io;

import net.grian.spatium.util.ColorMath;
import net.grian.torrens.object.OBJMaterial;
import net.grian.torrens.object.OBJMaterialLibrary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class SerializerOBJMaterials implements TextSerializer<OBJMaterialLibrary> {

    @Override
    public void toWriter(OBJMaterialLibrary mtllib, Writer writer) throws IOException {
        try (BufferedWriter buffWriter = new BufferedWriter(writer)) {
            toWriter(mtllib, buffWriter);
        }
    }

    public void toWriter(OBJMaterialLibrary mtllib, BufferedWriter writer) throws IOException {
        for (OBJMaterial material : mtllib)
            writeMaterial(material, writer);
    }

    private void writeMaterial(OBJMaterial material, BufferedWriter writer) throws IOException {
        writeString("newmtl", material.getName(), writer);

        writeColor("Ka", material.getAmbientColor(), writer);
        writeColor("Kd", material.getDiffuseColor(), writer);
        writeColor("Ks", material.getSpecularColor(), writer);
        writeColor("Tf", material.getTransmissionFilter(), writer);

        writeInt("illum", material.getIlluminationModel(), writer);
        writeFloat("d", material.getDissolution(), writer);

        writeNonnullString("map_Ka", material.getAmbientMap(), writer);
        writeNonnullString("map_Kd", material.getDiffuseMap(), writer);
        writeNonnullString("map_Ks", material.getSpecularColorMap(), writer);
        writeNonnullString("map_d", material.getDissolutionMap(), writer);
        writeNonnullString("disp", material.getDisplacementMap(), writer);
        writeNonnullString("decal", material.getDecalMap(), writer);
        writeNonnullString("bump", material.getBumpMap(), writer);
    }

    private void writeColor(String title, int rgb, BufferedWriter writer) throws IOException {
        writer.newLine();
        writer.write(title);
        writer.write(" ");
        writer.write(DECIMALS.format(ColorMath.red(rgb) / 255D));
        writer.write(" ");
        writer.write(DECIMALS.format(ColorMath.green(rgb) / 255D));
        writer.write(" ");
        writer.write(DECIMALS.format(ColorMath.blue(rgb) / 255D));
    }

    private void writeString(String title, String str, BufferedWriter writer) throws IOException {
        writer.newLine();
        writer.write(title);
        writer.write(" ");
        writer.write(str);
    }

    private void writeNonnullString(String title, String str, BufferedWriter writer) throws IOException {
        if (str == null) return;
        writeString(title, str, writer);
    }

    private void writeInt(String title, int i, BufferedWriter writer) throws IOException {
        writeString(title, Integer.toString(i), writer);
    }

    private void writeFloat(String title, float f, BufferedWriter writer) throws IOException {
        writeString(title, DECIMALS.format(f), writer);
    }

    private final static DecimalFormat DECIMALS = new DecimalFormat("#.######");
    static {
        DECIMALS.setRoundingMode(RoundingMode.DOWN);
    }

}
