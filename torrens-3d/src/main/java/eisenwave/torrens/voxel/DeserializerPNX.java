package eisenwave.torrens.voxel;

import eisenwave.torrens.img.DeserializerImage;
import eisenwave.torrens.io.Deserializer;
import eisenwave.torrens.io.LittleDataInputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.logging.Logger;

public class DeserializerPNX implements Deserializer<VoxelMesh> {
    
    private final static Charset UTF8 = StandardCharsets.UTF_8;
    
    @Nullable
    private final Logger logger;
    
    public DeserializerPNX(@Nullable Logger logger) {
        this.logger = logger;
    }
    
    public DeserializerPNX() {
        this(null);
    }
    
    private void debug(String msg) {
        if (this.logger != null)
            this.logger.fine(msg);
    }
    
    @NotNull
    @Override
    public VoxelMesh fromStream(InputStream stream) throws IOException {
        LittleDataInputStream dataStream = new LittleDataInputStream(stream);
        
        // read overall size
        /*final int[] dims = */read3ints(dataStream);
        final int
            layerCount = dataStream.readLittleInt(),
            imageCount = dataStream.readLittleInt();
    
        VoxelMesh result = new VoxelMesh();
        
        debug("reading voxels with boundaries: "+result);
        debug("reading "+imageCount+" images in "+layerCount+" layers");
        
        BufferedImage[] images = new BufferedImage[imageCount];
        for (int i = 0; i < imageCount; i++)
            images[i] = readImage(dataStream);
        
        for (int i = 0; i < layerCount; i++) {
            /* String layerName = */readLayerName(dataStream);
            /* boolean layerVisibility = */dataStream.readBoolean();
            /* boolean layerUnlocked = */dataStream.readBoolean();
            int[] size = read3ints(dataStream);
            int[] pos = read3ints(dataStream);
    
            // empty layer
            if (size[0] < 1 || size[1] < 1 || size[2] < 0)
                continue;
            
            VoxelArray voxels = new VoxelArray(size[0], size[1], size[2]);
            result.add(pos[0], pos[1], pos[2], voxels);
    
            debug("layer ("+i+") with size="+ Arrays.toString(size)+", pos="+Arrays.toString(pos));
            
            // read layer slice by slice
            for (int x = 0; x < size[0]; x++) {
                BufferedImage img = images[dataStream.readLittleInt()];
                
                for (int y = 0; y < size[1]; y++) for (int z = 0; z < size[2]; z++)
                    //noinspection SuspiciousNameCombination
                    voxels.setRGB(x, y, z, img.getRGB(y, z));
            }
        }
        
        return result;
    }
    
    @NotNull
    private static BufferedImage readImage(LittleDataInputStream stream) throws IOException {
        int length = stream.readLittleInt();
        byte[] bytes = new byte[length];
        if (length != stream.read(bytes))
            throw new EOFException();
        
        return new DeserializerImage().fromBytes(bytes);
    }
    
    @SuppressWarnings("UnusedReturnValue")
    @NotNull
    private static String readLayerName(LittleDataInputStream stream) throws IOException {
        int layerNameLength = stream.readLittleInt();
        byte[] bytes = new byte[layerNameLength];
        if (stream.read(bytes) < layerNameLength)
            throw new EOFException();
        
        return new String(bytes, UTF8);
    }
    
    /**
     * Reads 3 little endian byte order integers from the stream.
     *
     * @param stream the stream
     * @return an array of 3 ints
     * @throws IOException if an I/O error occurs
     */
    @NotNull
    private static int[] read3ints(LittleDataInputStream stream) throws IOException {
        return new int[] {
            stream.readLittleInt(),
            stream.readLittleInt(),
            stream.readLittleInt()
        };
    }
    
}
