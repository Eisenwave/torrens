package eisenwave.torrens.voxel;

import eisenwave.torrens.io.TextSerializer;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * <p>
 *     A serializer for <b>Qubicle Exchange Format (.qef)</b> files.
 * </p>
 * <p>
 *     Only version <b>0.2</b> is supported.
 * </p>
 * Qubicle Geometry note:<ul>
 *     <li>y-axis points upwards</li>
 *     <li>z-axis 90 degrees to the right of x-axis</li>
 * </ul>
 */
@SuppressWarnings("SpellCheckingInspection")
public class SerializerQEF implements TextSerializer<VoxelArray> {

    private Color[] colors;

    @Nullable
    private final Logger logger;

    public SerializerQEF(@Nullable Logger logger) {
        this.logger = logger;
    }

    public SerializerQEF() {
        this(null);
    }
    
    private void debug(String msg) {
        if (logger != null)
            logger.fine(msg);
    }

    @Override
    public void toWriter(VoxelArray array, Writer writer) throws IOException {
        BufferedWriter buffWriter = writer instanceof BufferedWriter?
            (BufferedWriter) writer :
            new BufferedWriter(writer);
        
        toWriter(array, buffWriter);
    }
    
    public void toWriter(VoxelArray array, BufferedWriter writer) throws IOException {
        debug("serializing voxel array ("+array.size()+" voxels) as QEF ...");
        
        writeHeader(writer);
        writeDimensions(array, writer);
        
        List<QEFVoxel> voxels = transcodeArray(array);
        
        writeColors(writer);
        writeVoxels(voxels, writer);
        
        writer.close();
    }

    private void writeHeader(BufferedWriter writer) throws IOException {
        writer.write("Qubicle Exchange Format"); writer.newLine();
        writer.write("Version 0.2"); writer.newLine();
        writer.write("www.minddesk.com"); writer.newLine();
    }

    private void writeDimensions(VoxelArray array, BufferedWriter writer) throws IOException {
        String dimensions = array.getSizeX()+" "+array.getSizeY()+" "+array.getSizeZ();
        debug("dimensions = "+dimensions);
        writer.write(dimensions); writer.newLine();
    }

    private void writeColors(BufferedWriter writer) throws IOException {
        debug("writing "+colors.length+" colors ...");
        writer.write(String.valueOf(colors.length));
        writer.newLine();

        for (Color color : colors) {
            writer.write(color.getRed()   / 255D+" ");
            writer.write(color.getGreen() / 255D+" ");
            writer.write(String.valueOf(color.getBlue() / 255D));
            writer.newLine();
        }
    }

    private void writeVoxels(List<QEFVoxel> voxels, BufferedWriter writer) throws IOException {
        debug("writing "+voxels.size()+" voxels to file ...");
        
        for (QEFVoxel v : voxels) {
            writer.write(v.x+" "+v.y+" "+v.z+" "+v.index);
            writer.newLine();
        }
        /*for (VoxelArray.Voxel voxel : compressedArray) {
            final int color = (voxel.getRGB() & 0xFFFFFF) -1;
            writer.write(voxel.getX()+" "+voxel.getY()+" "+voxel.getZ()+" "+color);
            writer.newLine();
        }*/
    }

    private List<QEFVoxel> transcodeArray(VoxelArray array) {
        List<QEFVoxel> result = new LinkedList<>();
        Map<Color, Integer> colors = new HashMap<>();
        int colorIndex = 0;

        for (VoxelArray.Voxel v : array) {
            Color color = new Color(v.getRGB(),false);
            final int i;
            
            if (colors.containsKey(color))
                i = colors.get(color);
            else
                colors.put(color, i = colorIndex++);
            
            result.add(new QEFVoxel(v.getX(), v.getY(), v.getZ(), i));
        }

        this.colors = new Color[colors.size()];
        for (Map.Entry<Color, Integer> entry : colors.entrySet())
            this.colors[entry.getValue()] = entry.getKey();
        
        return result;
    }
    
    private static class QEFVoxel {
        private final int x, y, z, index;
        
        public QEFVoxel(int x, int y, int z, int index) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.index = index;
        }
    }

}
