package net.grian.torrens.util.img.ico;

import net.grian.spatium.util.PrimArrays;
import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.error.FileVersionException;
import net.grian.torrens.io.Deserializer;
import net.grian.torrens.io.LittleDataInputStream;
import net.grian.torrens.util.FileConstants;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class DeserializerICO implements Deserializer<BufferedImage[]> {
    
    /** whether the two reserved bytes in ICO files should be tested for having the values given in the file format
     * specification <code>0x00</code>*/
    private final static boolean VERIFY_RESERVED = true;
    
    @NotNull
    @Override
    public BufferedImage[] fromStream(InputStream stream) throws IOException {
        LittleDataInputStream dataStream = new LittleDataInputStream(stream);
        return fromStream(dataStream);
    }
    
    private BufferedImage[] fromStream(LittleDataInputStream stream) throws IOException {
        final int reserved = stream.readLittleUnsignedShort();
        if (VERIFY_RESERVED && reserved != 0)
            throw new FileSyntaxException("reserved bytes must be 0");
        
        int type = stream.readLittleUnsignedShort();
        if (type != 1)
            throw new FileVersionException("image type is not .ico ("+type+")");
        
        int images = stream.readLittleUnsignedShort();
        ICODirEntry[] entries = new ICODirEntry[images];
        
        for (int i = 0; i < images; i++)
            entries[i] = readDirEntry(stream);
        
        // sort dir entries by offset in case they are not ordered correctly, so that image data is being read
        // in the correct order
        Arrays.sort(entries);
    
        BufferedImage[] result = new BufferedImage[entries.length];
        for (int i = 0; i < entries.length; i++) {
            //System.out.println("reading "+entries[i]);
            BufferedImage image = readImage(stream, entries[i]);
            result[i] = image;
        }
        
        return result;
    }
    
    private static ICODirEntry readDirEntry(LittleDataInputStream stream) throws IOException {
        final int
            width = stream.readUnsignedByte(),
            height = stream.readUnsignedByte(),
            palette = stream.readUnsignedByte(),
            reserved = stream.readUnsignedByte(),
            planes = stream.readLittleUnsignedShort(),
            bitsPerPixel = stream.readLittleUnsignedShort(),
            data = stream.readLittleInt(),   //site of image's data in bytes
            offset = stream.readLittleInt(); //offset of bmp or png from beginning of file
        
        if (VERIFY_RESERVED && reserved != 0)
            throw new FileSyntaxException("reserved bytes must be 0 (is "+reserved+")");
        
        return new ICODirEntry(
            width==0? 256 : width,
            height==0? 256 : height,
            palette, planes, bitsPerPixel, data, offset);
    }
    
    private static BufferedImage readImage(InputStream stream, ICODirEntry entry) throws IOException {
        byte[] bytes = new byte[entry.data];
        
        {
            int read = stream.read(bytes);
            if (read != entry.data) {
                String msg = String.format("%d bytes read but %d required by dir. entry", read, entry.data);
                throw new FileSyntaxException(msg);
            }
        }
        
        // Officially, ICO files can contain both PNG and BMP image data, although PNG is not supported by Windows
        
        if (FileConstants.isPNG(bytes)) {
            return ImageIO.read(new ByteArrayInputStream(bytes));
        }
    
        // BMP bytes without a BITMAPFILEHEADER remains only option
        
        {
            // BITMAPINFOHEADER in ICO files has twice the actual height and must be fixed before using ImageIO
            
            ByteBuffer buffer = ByteBuffer.wrap(bytes);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            buffer.putInt(4, entry.width);
            buffer.putInt(8, entry.height);
        }
        
        byte[] header = makeBitmapFileHeader(bytes);
        bytes = PrimArrays.concat(header, bytes);
        
        /* BMPImageReader bmpReader = (BMPImageReader) ImageIO.getImageReadersByFormatName("bmp").next();
        ImageInputStream imgStream = ImageIO.createImageInputStream(new ByteArrayInputStream(bytes));
        bmpReader.setInput(imgStream, true, true); */
        
        return ImageIO.read(new ByteArrayInputStream(bytes));
    }
    
    private final static byte[] BF_TYPE = {0x42, 0x4D}; // ASCII BM
    
    /**
     * Creates a BITMAPFILEHEADER for a given byte array starting with the BITMAPINFOHEADER of a BMP file.
     *
     * @param bmp the BITMAPINFOHEADER bytes
     * @return a BITMAPFILEHEADER byte array
     */
    public static byte[] makeBitmapFileHeader(byte[] bmp)  {
        byte[] header = new byte[14];
        ByteBuffer buffer = ByteBuffer.wrap(header);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        buffer.put(BF_TYPE, 0, BF_TYPE.length);
        buffer.putInt(2, FileConstants.BMP_FILE_HEADER_LENGTH + bmp.length);// bfSize
        buffer.putInt(6, 0x10c); //bfReserved
    
        int biSize;
        {
            ByteBuffer bmpBuffer = ByteBuffer.wrap(bmp);
            bmpBuffer.order(ByteOrder.LITTLE_ENDIAN);
            biSize = bmpBuffer.getInt(0);
        }
        
        buffer.putInt(10, FileConstants.BMP_FILE_HEADER_LENGTH + biSize);
    
        return header;
    }
    
}