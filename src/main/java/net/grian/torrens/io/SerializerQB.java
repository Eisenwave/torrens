package net.grian.torrens.io;

import net.grian.spatium.util.IOMath;
import net.grian.spatium.voxel.VoxelArray;
import net.grian.torrens.object.QBMatrix;
import net.grian.torrens.object.QBModel;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 *     A serializer for <b>Qubicle Binary (.qb)</b> files.
 * </p>
 * <p>
 *     No version restrictions exist.
 * </p>
 */
public class SerializerQB implements Serializer<QBModel> {

    public final static int
            CURRENT_VERSION = 0x01_01_00_00,
            COLOR_FORMAT_RGBA = 0,
            COLOR_FORMAT_BGRA = 1,
            Z_ORIENT_LEFT = 0,
            Z_ORIENT_RIGHT = 1,
            UNCOMPRESSED = 0,
            COMPRESSED = 1,
            VIS_MASK_ENCODED = 0,
            VIS_MASK_UNENCODED = 1,
            CODEFLAG = 2,
            NEXTSLICEFLAG = 6;

    private QBModel model;

    private int colorFormat;

    @Override
    public void toStream(QBModel model, OutputStream stream) throws IOException {
        DataOutputStream dataStream = new DataOutputStream(stream);
        toStream(model, dataStream);
    }

    public void toStream(QBModel mesh, DataOutputStream stream) throws IOException {
        this.model = mesh;
        serializeHeader(stream);
        for (QBMatrix matrix : mesh)
            serializeMatrix(matrix, stream);
    }

    private void serializeHeader(DataOutputStream stream) throws IOException {
        stream.writeInt(CURRENT_VERSION);
        stream.writeInt(colorFormat = COLOR_FORMAT_RGBA);
        stream.writeInt(Z_ORIENT_LEFT);
        stream.writeInt(UNCOMPRESSED);
        writeLittleInt(stream, VIS_MASK_UNENCODED);
        writeLittleInt(stream, model.size());
    }

    private void serializeMatrix(QBMatrix matrix, DataOutputStream stream) throws IOException {
        byte[] name = matrix.getName().getBytes();
        stream.write(name.length);
        stream.write(name);

        VoxelArray array = matrix.getVoxels();
        writeLittleInt(stream, array.getSizeX());
        writeLittleInt(stream, array.getSizeY());
        writeLittleInt(stream, array.getSizeZ());
        writeLittleInt(stream, matrix.getMinX());
        writeLittleInt(stream, matrix.getMinY());
        writeLittleInt(stream, matrix.getMinZ());

        serializeUncompressed(array, stream);
    }

    private void serializeUncompressed(VoxelArray array, DataOutputStream stream) throws IOException {
        final int
                limX = array.getSizeX(),
                limY = array.getSizeY(),
                limZ = array.getSizeZ();

        for (int z = 0; z<limZ; z++)
            for (int y = 0; y<limY; y++)
                for (int x = 0; x<limX; x++)
                    stream.writeInt(asColor(array.getRGB(x, y, z)));
    }

    private int asColor(int argb) {
        byte[] bytes = IOMath.asBytes(argb);

        if (colorFormat == COLOR_FORMAT_RGBA)
            return ((bytes[1]&0xFF)<<24) | ((bytes[2]&0xFF)<<16) | ((bytes[3]&0xFF)<<8) | (bytes[0]&0xFF);

        else if (colorFormat == COLOR_FORMAT_BGRA)
            return ((bytes[3]&0xFF)<<24) | ((bytes[2]&0xFF)<<16) | ((bytes[1]&0xFF)<<8) | (bytes[0]&0xFF);

        else
            throw new IllegalStateException("unknown color format");
    }

    /**
     * Writes a little endian integer to the stream.
     *
     * @param stream the stream
     * @param number the integer
     * @throws IOException if an I/O error occurs
     */
    private static void writeLittleInt(OutputStream stream, int number) throws IOException {
        stream.write(new byte [] {
                (byte) (number &0xFF),
                (byte) ((number>>8) &0xFF),
                (byte) ((number>>16) &0xFF),
                (byte) ((number>>24) &0xFF)});
    }

}
