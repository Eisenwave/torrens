package net.grian.torrens.voxel;

import net.grian.torrens.io.LittleDataOutputStream;
import net.grian.torrens.io.Serializer;
import org.jetbrains.annotations.Nullable;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * <p>
 *     A serializer for <b>Qubicle Binary (.qb)</b> files.
 * </p>
 * <p>
 *     No version restrictions exist.
 * </p>
 * Qubicle Geometry note:<ul>
 *     <li>y-axis points upwards</li>
 *     <li>z-axis 90 degrees to the right of x-axis</li>
 * </ul>
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
        VIS_MASK_ENCODED = 1,
        VIS_MASK_UNENCODED = 0,
        CODEFLAG = 2,
        NEXTSLICEFLAG = 6;

    @Nullable
    private final Logger logger;
    
    private QBModel model;

    private int colorFormat;
    
    public SerializerQB(@Nullable Logger logger) {
        this.logger = logger;
    }
    
    public SerializerQB() {
        this(null);
    }
    
    private void debug(String msg) {
        if (logger != null)
            logger.fine(msg);
    }
    
    private void debug(QBMatrix matrix) {
        if (logger != null)
            logger.fine("serializing "+matrix+" ...");
    }

    @Override
    public void toStream(QBModel model, OutputStream stream) throws IOException {
        LittleDataOutputStream dataStream = new LittleDataOutputStream(stream);
        toStream(model, dataStream);
    }

    public void toStream(QBModel mesh, LittleDataOutputStream stream) throws IOException {
        this.model = mesh;
        serializeHeader(stream);
        for (QBMatrix matrix : mesh)
            serializeMatrix(matrix, stream);
    }

    private void serializeHeader(LittleDataOutputStream stream) throws IOException {
        stream.writeInt(CURRENT_VERSION);
        stream.writeInt(colorFormat = COLOR_FORMAT_RGBA);
        stream.writeInt(Z_ORIENT_LEFT);
        stream.writeInt(UNCOMPRESSED);
        stream.writeLittleInt(VIS_MASK_UNENCODED);
        stream.writeLittleInt(model.size());
    }

    private void serializeMatrix(QBMatrix matrix, LittleDataOutputStream stream) throws IOException {
        debug(matrix);
        byte[] name = matrix.getName().getBytes();
        stream.write(name.length);
        stream.write(name);

        VoxelArray array = matrix.getVoxels();
        stream.writeLittleInt(array.getSizeX()); //matrix dims
        stream.writeLittleInt(array.getSizeY());
        stream.writeLittleInt(array.getSizeZ());
        stream.writeLittleInt(matrix.getMinX()); //matrix pos
        stream.writeLittleInt(matrix.getMinY());
        stream.writeLittleInt(matrix.getMinZ());
        
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
        byte[] bytes = toBytes(argb);

        if (colorFormat == COLOR_FORMAT_RGBA)
            return ((bytes[1]&0xFF)<<24) | ((bytes[2]&0xFF)<<16) | ((bytes[3]&0xFF)<<8) | (bytes[0]&0xFF);

        else if (colorFormat == COLOR_FORMAT_BGRA)
            return ((bytes[3]&0xFF)<<24) | ((bytes[2]&0xFF)<<16) | ((bytes[1]&0xFF)<<8) | (bytes[0]&0xFF);

        else
            throw new IllegalStateException("unknown color format");
    }
    
    public static byte[] toBytes(int Int) {
        return new byte[]{
            (byte) ((Int >> 24) & 0xFF),
            (byte) ((Int >> 16) & 0xFF),
            (byte) ((Int >> 8) & 0xFF),
            (byte) (Int & 0xFF)};
    }

}
