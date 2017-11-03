package net.grian.torrens.qubicle;

import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.error.FileVersionException;
import net.grian.torrens.io.Deserializer;
import net.grian.torrens.io.LittleDataInputStream;
import net.grian.torrens.util.ColorMath;
import net.grian.torrens.voxel.VoxelArray;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * <p>
 *     A deserializer for <b>Qubicle Binary (.qb)</b> files.
 * </p>
 * <p>
 *     No version restrictions exist.
 * </p>
 * Qubicle Geometry note:<ul>
 *     <li>y-axis points upwards</li>
 *     <li>z-axis 90 degrees to the right of x-axis</li>
 * </ul>
 */
public class DeserializerQB implements Deserializer<QBModel> {

    private boolean compressed, visibilityMaskEncoded, zLeft;
    private int colorFormat, numMatrices;
    private QBModel mesh;

    @Nullable
    private final Logger logger;

    public DeserializerQB(@Nullable Logger logger) {
        this.logger = logger;
    }
    
    public DeserializerQB() {
        this(null);
    }
    
    private void debug(String msg) {
        if (logger != null)
            logger.fine(msg);
    }

    @NotNull
    @Override
    public QBModel fromStream(InputStream stream) throws IOException {
        debug("deserializing qb...");
        LittleDataInputStream dataStream = new LittleDataInputStream(stream);

        deserializeHeader(dataStream);

        mesh = new QBModel();
        for (int i = 0; i < numMatrices; i++)
            deserializeMatrix(dataStream);

        debug("deserialized matrices");
        dataStream.close();
        
        return mesh;
    }

    private void deserializeHeader(LittleDataInputStream stream) throws IOException {
        int version = stream.readInt(); //big endian
        if (version != SerializerQB.CURRENT_VERSION)
            throw new FileVersionException(version+" != current ("+ SerializerQB.CURRENT_VERSION+")");

        this.colorFormat = stream.readInt();
        if (colorFormat != SerializerQB.COLOR_FORMAT_RGBA && colorFormat != SerializerQB.COLOR_FORMAT_BGRA)
            throw new FileSyntaxException("unknown color format: "+colorFormat);

        int zAxisOrientation = stream.readInt();
        if (zAxisOrientation != SerializerQB.Z_ORIENT_LEFT && zAxisOrientation != SerializerQB.Z_ORIENT_RIGHT)
            throw new FileSyntaxException("unknown z axis orientation: "+zAxisOrientation);
        this.zLeft = zAxisOrientation== SerializerQB.Z_ORIENT_LEFT;

        int compressedInt = stream.readLittleInt();
        if (compressedInt != SerializerQB.UNCOMPRESSED && compressedInt != SerializerQB.COMPRESSED)
            throw new FileSyntaxException("unknown compression: "+compressedInt);
        this.compressed = compressedInt == SerializerQB.COMPRESSED;

        int visEncodedInt = stream.readLittleInt();
        if (visEncodedInt != SerializerQB.VIS_MASK_UNENCODED && visEncodedInt != SerializerQB.VIS_MASK_ENCODED)
            throw new FileSyntaxException("unknown vis mask encoding: "+visEncodedInt);
        this.visibilityMaskEncoded = visEncodedInt == SerializerQB.VIS_MASK_ENCODED;

        this.numMatrices = stream.readLittleInt();
    
        debug("deserializing "+numMatrices+" matrices with"+
            ": compression="+compressed+
            ", colorFormat="+colorFormat+
            ", visMaskEncoded="+visibilityMaskEncoded+
            ", zLeft="+zLeft);
    }

    private void deserializeMatrix(LittleDataInputStream stream) throws IOException {
        // read matrix name
        byte nameLength = stream.readByte();
        byte[] nameBytes = new byte[nameLength];
        if (stream.read(nameBytes) < nameLength) throw new IOException();
        String name = new String(nameBytes);

        final int
            sizeX = stream.readLittleInt(),
            sizeY = stream.readLittleInt(),
            sizeZ = stream.readLittleInt(),
            posX  = stream.readLittleInt(),
            posY  = stream.readLittleInt(),
            posZ  = stream.readLittleInt();
    
        debug("reading matrix: "+sizeX+"x"+sizeY+"x"+sizeZ+" at "+posX+", "+posY+", "+posZ);

        VoxelArray voxels = compressed?
                readCompressed(sizeX, sizeY, sizeZ, stream) :
                readUncompressed(sizeX, sizeY, sizeZ, stream);

        mesh.add(new QBMatrix(name, posX, posY, posZ, voxels));
    }

    private VoxelArray readUncompressed(int sizeX, int sizeY, int sizeZ, DataInputStream stream) throws IOException {
        VoxelArray matrix = new VoxelArray(sizeX, sizeY, sizeZ);
        final int maxZ = sizeZ-1;

        for(int slice = 0; slice < sizeZ; slice++) {
            final int z = zLeft? slice : maxZ - slice;
            
            for (int y = 0; y < sizeY; y++)
                for (int x = 0; x < sizeX; x++)
                    matrix.setRGB(x, y, z, asARGB(stream.readInt()));
        }

        return matrix;
    }

    private VoxelArray readCompressed(int sizeX, int sizeY, int sizeZ, LittleDataInputStream stream) throws IOException {
        VoxelArray voxels = new VoxelArray(sizeX, sizeY, sizeZ);
        final int maxZ = sizeZ-1;

        for (int slice = 0; slice < sizeZ; slice++) {
            final int z = zLeft? slice : maxZ-slice;
            int index = 0;

            while (true) {
                int data = stream.readLittleInt();
                if (data == SerializerQB.NEXTSLICEFLAG) break;
                
                else if (data == SerializerQB.CODEFLAG) {
                    int count = stream.readLittleInt();
                    data = stream.readInt();

                    for(int i = 0; i < count; i++) {
                        int x = index%sizeX, y = index/sizeX;
                        voxels.setRGB(x, y, z, asARGB(data));
                        index++;
                    }
                }
                else {
                    int x = index%sizeX, y = index/sizeX;
                    voxels.setRGB(x, y, z, asARGB(Integer.reverseBytes(data)));
                    index++;
                }
            }
        }

        return voxels;
    }

    /**
     * Converts a color integer using the qb's color format.
     *
     * @param color the color int
     * @return an ARGB int
     */
    private int asARGB(int color) {
        int argb;

        switch (colorFormat) {
            case SerializerQB.COLOR_FORMAT_RGBA: argb = ColorMath.fromRGB(
                    (color >> 24) & 0xFF,
                    (color >> 16) & 0xFF,
                    (color >> 8) & 0xFF,
                    color & 0xFF);
                break;
            case SerializerQB.COLOR_FORMAT_BGRA: argb = ColorMath.fromRGB(
                    color & 0xFF,
                    (color >> 24) & 0xFF,
                    (color >> 16) & 0xFF,
                    (color >> 8) & 0xFF);
                break;
            default: throw new AssertionError(colorFormat);
        }

        //if any side is visible, make color solid
        if (visibilityMaskEncoded) {
            if ((ColorMath.alpha(argb) != 0)) argb |= 0xFF_000000;
            else argb &= 0x00_FFFFFF;
        }

        return argb;
    }

}
