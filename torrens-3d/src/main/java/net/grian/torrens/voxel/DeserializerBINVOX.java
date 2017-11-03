package net.grian.torrens.voxel;

import net.grian.torrens.error.FileFormatException;
import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.error.FileVersionException;
import net.grian.torrens.io.Deserializer;
import net.grian.torrens.util.ColorMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class DeserializerBINVOX implements Deserializer<VoxelArray> {
    
    private final static int LINE_BUFFER_SIZE = 40;
    
    @Nullable
    private final Logger logger;
    private final int voxelRGB;
    
    private int sx = 0, sy = 0, sz = 0;
    
    private double tx = 0, ty = 0, tz = 0, scale;
    
    public DeserializerBINVOX(@Nullable Logger logger, int voxelRGB) {
        this.logger = logger;
        this.voxelRGB = voxelRGB;
    }
    
    public DeserializerBINVOX(int voxelRGB) {
        this(null, voxelRGB);
    }
    
    public DeserializerBINVOX(@Nullable Logger logger) {
        this(logger, ColorMath.SOLID_WHITE);
    }
    
    public DeserializerBINVOX() {
        this(null);
    }
    
    /**
     * Returns the dimensions of the bounding box containing the voxels.
     *
     * @return the dimensions
     */
    public final int[] getDimensions() {
        return new int[] {sx, sy, sz};
    }
    
    /**
     * Returns the amount of translation applied to the entire model.
     *
     * @return the translation
     */
    public final double[] getTranslation() {
        return new double[] {tx, ty, tz};
    }
    
    /**
     * Returns the scale factor of the model.
     *
     * @return the scale factor
     */
    public final double getScale() {
        return scale;
    }
    
    @NotNull
    @Override
    public VoxelArray fromStream(InputStream stream) throws IOException {
        // header
        String line = readASCIILine(stream, LINE_BUFFER_SIZE);
        if (!line.startsWith("#binvox"))
            throw new FileFormatException("file is not a binvox file");
        
        // version
        String version_string = line.substring(8);
        if (!version_string.equals("1"))
            throw new FileVersionException("unsupported version: "+version_string);
        
        DataInputStream dataStream = new DataInputStream(stream);
        
        while (true) {
            line = readASCIILine(stream, LINE_BUFFER_SIZE);
            
            if (line.startsWith("data")) {
                return parseData(dataStream);
                
            } else if (line.startsWith("dim")) {
                parseDimensions(dataStream, line);
                
            } else if (line.startsWith("translate")) {
                parseTranslation(dataStream, line);
                
            } else if (line.startsWith("scale")) {
                parseScale(dataStream, line);
                
            } else {
                throw new FileSyntaxException("unknown line type: "+line);
            }
            
        }
    }
    
    private void parseDimensions(DataInputStream stream, String line) throws IOException {
        String[] dimensions = line.split(" ");
        if (dimensions.length == 4) {
            // the values are in the same line
            try {
                sx = Integer.parseInt(dimensions[1]);
                sy = Integer.parseInt(dimensions[2]);
                sz = Integer.parseInt(dimensions[3]);
            } catch (NumberFormatException ex) {
                throw new FileSyntaxException("failed to parse dimensions: "+line);
            }
        } else {
            // the values are in the next line(s)
            sx = stream.readInt();
            sy = stream.readInt();
            sz = stream.readInt();
        }
        
        if (sx < 1 || sy < 1 || sz < 1)
            throw new FileSyntaxException("dimensions must be at least 1");
    }
    
    private void parseTranslation(DataInputStream stream, String line) throws IOException {
        String[] translations = line.split(" ");
        if (translations.length == 4) {
            // the values are in the same line
            try {
                tx = Double.parseDouble(translations[1]);
                ty = Double.parseDouble(translations[2]);
                tz = Double.parseDouble(translations[3]);
            } catch (NumberFormatException ex) {
                throw new FileSyntaxException("failed to parse translation: "+line);
            }
        } else {
            // the values are in the next line(s)
            tx = stream.readDouble();
            ty = stream.readDouble();
            tz = stream.readDouble();
        }
    
        if (!Double.isFinite(tx) || !Double.isFinite(ty) || !Double.isFinite(tz))
            throw new FileSyntaxException("translation must be finite");
    }
    
    private void parseScale(DataInputStream stream, String line) throws IOException  {
        String[] scaleLine = line.split(" ");
        if (scaleLine.length == 2) {
            // the values are in the same line
            try {
                scale = Double.parseDouble(scaleLine[1]);
            } catch (NumberFormatException ex) {
                throw new FileSyntaxException("failed to parse scale: "+line);
            }
        } else {
            // the value is in the next line
            scale = stream.readDouble();
        }
    
        if (!Double.isFinite(scale))
            throw new FileSyntaxException("scale must be finite");
    }
    
    @NotNull
    private VoxelArray parseData(DataInputStream stream) throws IOException {
        if (sx == 0 || sy == 0 || sz == 0)
            throw new FileSyntaxException("binvox header is missing dimensions");
        
        VoxelArray result = new VoxelArray(sx, sy, sz);
        final int length = result.getLength();
    
        if (logger != null) logger.fine("saving binvox data in: "+result);
        
        // read voxel data
        int value, count;
    
        for (int i = 0, next = 0; i < length; i = next) {
            //System.out.println(i);
            value = stream.readUnsignedByte();
            count = stream.readUnsignedByte();
            
            //debug(i+", "+value+", "+count);
            
            next += count;
            
            // no voxels present for the next <count> voxels
            if (value == 0)
                continue;
            
            // voxels present for the the next <count> voxels
            if (value == 1) {
                for (int j = i; j < next; j++) {
                    int x = j % sx;
                    int y = (j / sx) % sz;
                    int z = (j / (sx * sy));
    
                    //noinspection SuspiciousNameCombination
                    result.setRGB(x, y, z, voxelRGB);
                }
                continue;
            }
            
            // corrupted data
            throw new FileSyntaxException("value must be 0 or 1");
        }
    
        return result;
    }
    
    // UTIL
    
    private static String readASCIILine(InputStream stream, int bufferSize) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        int limit = 0;
        
        for (;;) {
            int point = stream.read();
            //System.out.print((char) point);
            
            if (point < 0)
                throw new EOFException();
            if (point > 127)
                throw new FileSyntaxException("line contains non-ascii characters");
            if (point == '\n')
                break;
            
            // Windows sometimes produces files which use a carriage return followed by a newline, thus the
            // carriage return character can be safely ignored
            if (point == '\r')
                continue;
    
            buffer.put((byte) point);
            limit++;
        }
        
        byte[] bytes = new byte[limit];
        buffer.position(0);
        buffer.get(bytes, 0, limit);
        return new String(bytes, StandardCharsets.US_ASCII);
    }
    
}
