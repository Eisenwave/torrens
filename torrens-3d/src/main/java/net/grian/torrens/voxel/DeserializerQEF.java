package net.grian.torrens.voxel;

import java.awt.*;
import java.io.*;
import java.util.logging.Logger;

import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.error.FileVersionException;
import net.grian.torrens.io.TextDeserializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 *     A parser for <b>Qubicle Exchange Format (.qef)</b> files.
 * </p>
 * <p>
 *     Only version <b>2.0</b> is supported.
 * </p>
 * Qubicle Geometry note:<ul>
 *     <li>y-axis points upwards</li>
 *     <li>z-axis 90 degrees to the right of x-axis</li>
 * </ul>
 */
public class DeserializerQEF implements TextDeserializer<VoxelArray> {

    @Nullable
    private final Logger logger;

    private VoxelArray voxels;
    private int[] colors;

    public DeserializerQEF(@Nullable Logger logger) {
        this.logger = logger;
    }

    public DeserializerQEF() {
        this(null);
    }
    
    private void debug(String msg) {
        if (logger != null)
            logger.fine(msg);
    }

    @NotNull
    @Override
    public VoxelArray fromReader(Reader reader) throws IOException {
        BufferedReader buffReader = reader instanceof BufferedReader?
            (BufferedReader) reader :
            new BufferedReader(reader);
        
        String line;
        int num = 0;
        while ((line = buffReader.readLine()) != null)
            parseLine(++num, line);

        if (num < 5)
            throw new FileSyntaxException("less than 5 lines read, QEF incomplete");
    
        debug("completed parsing qef ("+voxels.size()+"/"+voxels.getVolume()+" voxels)");

        buffReader.close();
        return voxels;
    }

    private void parseLine(int num, String line) throws IOException {
        if (num == 1 || num == 3) return; //header lines

        if (num == 2) {
            if (line.equals("Version 0.2")) {
                debug("parsing file of version '" + line + "'");
            }
            else
                throw new FileVersionException("version '"+line+"' not supported");
        }

        else if (num == 4) {
            parseDimensions(line);
            debug("parsing qef of dimensions "+voxels.getSizeX()+"x"+voxels.getSizeY()+"x"+voxels.getSizeZ());
        }

        else if (num == 5) {
            parseColorCount(line);
            debug("parsing "+colors.length+" colors ...");
        }

        else if (num < 6 + colors.length) {
            parseColorDefinition(num, line);
        }

        else {
            parseVoxelDefinition(line);
        }
    }

    private void parseDimensions(String line) throws FileSyntaxException {
        int[] dims = parseInts(line);
        if (dims.length < 3)
            throw new FileSyntaxException("less than 3 dimensions");
        voxels = new VoxelArray(dims[0], dims[1], dims[2]);
    }

    private void parseColorCount(String line) throws FileSyntaxException {
        try {
            int colorCount = Integer.parseInt(line);
            colors = new int[colorCount];
        } catch (NumberFormatException ex) {
            throw new FileSyntaxException(ex);
        }
    }

    private void parseColorDefinition(int num, String line) throws FileSyntaxException {
        float[] rgb = parseFloats(line);
        if (rgb.length < 3) throw new FileSyntaxException();
        colors[num - 6] = new Color(rgb[0], rgb[1], rgb[2]).getRGB();
    }

    private void parseVoxelDefinition(String line) throws FileSyntaxException {
        int[] ints = parseInts(line);
        voxels.setRGB(ints[0], ints[1], ints[2], colors[ints[3]]);
    }

    private static int[] parseInts(String line) throws FileSyntaxException {
        String[] splits = line.split(" ");
        int[] result = new int[splits.length];
        for (int i = 0; i<splits.length; i++) {
            try {
                result[i] = Integer.parseInt(splits[i]);
            } catch (NumberFormatException ex) {
                throw new FileSyntaxException(ex);
            }
        }
        return result;
    }

    private static float[] parseFloats(String line) throws FileSyntaxException {
        String[] splits = line.split(" ");
        float[] result = new float[splits.length];
        for (int i = 0; i<splits.length; i++) {
            try {
                result[i] = Float.parseFloat(splits[i]);
            } catch (NumberFormatException ex) {
                throw new FileSyntaxException(ex);
            }
        }
        return result;
    }

}
