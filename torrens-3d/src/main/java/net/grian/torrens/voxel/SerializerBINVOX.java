package net.grian.torrens.util.voxel;

import net.grian.spatium.geo3.BlockVector;
import net.grian.torrens.io.Serializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class SerializerBINVOX implements Serializer<BitArray3> {
    
    private final boolean compression;
    
    private final static int MAX_RUN_LENGTH = 255;
    
    @SuppressWarnings("FieldCanBeLocal")
    private int dx, dy, dz, dyz;
    
    /**
     * Constructs a binvox serializer.
     *
     * @param compression whether run-length compression should be used
     */
    public SerializerBINVOX(boolean compression) {
        this.compression = compression;
    }
    
    /**
     * Constructs a binvox serializer with enabled run-length compression.
     */
    public SerializerBINVOX() {
        this(true);
    }
    
    @Override
    public void toStream(BitArray3 array, OutputStream stream) throws IOException {
        System.out.println(array);
        
        this.dx = array.getSizeX();
        this.dy = array.getSizeY();
        this.dz = array.getSizeZ();
        this.dyz = dy * dz;
    
        Writer writer = new OutputStreamWriter(stream);
        writer.write("#binvox 1\n");
        writer.write("dim "+dx+" "+dy+" "+dz+"\n");
        writer.write("translate 0 0 0\n");
        writer.write("scale 1\n");
        writer.write("data\n");
        writer.flush();
        
        DataOutputStream dataStream = new DataOutputStream(stream);
    
        int length = array.getVolume();
        
        if (compression) for (int i = 0; i < length;) {
            //System.out.println(posOf(i)+": "+array.contains(posOf(i)));
            boolean value = array.contains(posOf(i++));
            dataStream.writeBoolean(value);
            
            for (int count = 1; count <= MAX_RUN_LENGTH; i++, count++) {
                if (i == length || array.contains(posOf(i)) != value) {
                    dataStream.writeByte(count);
                    //System.out.println("count="+count);
                    break;
                }
    
                //System.out.println("..."+posOf(i));
            }
        }
        
        else for (int x = 0; x < dx; x++) for (int z = 0; z < dz; z++) for (int y = 0; y < dy; y++) {
            dataStream.writeBoolean(array.contains(x, y, z));
            dataStream.write(1);
        }
    }
    
    @Contract(pure = true)
    private int indexOf(int x, int y, int z) {
        // The y-coordinate runs fastest, then the z-coordinate, then the x-coordinate.
        return (x * dyz) + (z * dy) + y;
    }
    
    @NotNull
    private BlockVector posOf(int index) {
        return BlockVector.fromXYZ(
            index / dyz,
            index % dy,
            (index / dy) % dz
        );
    }
    
}
