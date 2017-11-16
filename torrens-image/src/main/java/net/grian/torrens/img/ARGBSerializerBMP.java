package net.grian.torrens.img;

import net.grian.torrens.io.LittleDataOutputStream;
import net.grian.torrens.util.ColorMath;
import net.grian.torrens.util.FileMagic;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.OutputStream;

public class ARGBSerializerBMP implements ARGBSerializer {
    
    private final static int
        BI_OFF_BITS = FileMagic.BMP_FILE_HEADER_LENGTH + FileMagic.BMP_INFO_HEADER_LENGTH,
        BI_PLANES = 1,
        BI_COMPRESSION = 0, //uncompressed
        BI_X_PIXELS_PER_METER = 0,
        BI_Y_PIXELS_PER_METER = 0,
        BI_CLR_USED = 0, //color table size in bytes
        BI_CLR_IMPORTANT = 0;
    
    private final boolean alpha;
    private final boolean header;
    private final boolean btmUp;
    
    public ARGBSerializerBMP(boolean alpha, boolean skipHeader, boolean btmUp) {
        this.alpha = alpha;
        this.header = !skipHeader;
        this.btmUp = btmUp;
    }
    
    public ARGBSerializerBMP(boolean alpha) {
        this(alpha, false, false);
    }
    
    public ARGBSerializerBMP() {
        this(true);
    }
    
    @Override
    public void toStream(BaseTexture img, OutputStream stream) throws IOException {
        LittleDataOutputStream dataStream = new LittleDataOutputStream(stream);
        toStream(img, dataStream);
    }
    
    public void toStream(BaseTexture img, LittleDataOutputStream stream) throws IOException {
        final int
            width = img.getWidth(),
            lines = img.getHeight(),
            bands = alpha? 4 : 3,
            lineLength = lineLengthOf(width, bands),
            data = lines * lineLength,
            padding = lineLength - width*bands;
        
        // BITMAPFILEHEADER
        if (header) {
            stream.writeShort(0x424D); // ASCII "BM"
            stream.writeLittleInt(BI_OFF_BITS + data); //file size, deemed "unreliable"
            stream.write(new byte[4]); //reserved
            stream.writeLittleInt(BI_OFF_BITS);
        }
        
        // BITMAPINFOHEADER
        stream.writeLittleInt(FileMagic.BMP_INFO_HEADER_LENGTH);
        stream.writeLittleInt(width);
        stream.writeLittleInt(btmUp? lines : -lines);
        stream.writeLittleShort(BI_PLANES);
        stream.writeLittleShort(bands * 8);
        stream.writeLittleInt(BI_COMPRESSION);
        stream.writeLittleInt(data);
        stream.writeLittleInt(BI_X_PIXELS_PER_METER);
        stream.writeLittleInt(BI_Y_PIXELS_PER_METER);
        stream.writeLittleInt(BI_CLR_USED);
        stream.writeLittleInt(BI_CLR_IMPORTANT);
        
        // ARGB ARRAY
        int[] argb = img.get(0, 0, width, lines);
        ARGBSerializer serializer = alpha?
            rgb -> new byte[] {
                (byte) ColorMath.blue(rgb),
                (byte) ColorMath.green(rgb),
                (byte) ColorMath.red(rgb),
                (byte) ColorMath.alpha(rgb)} :
            rgb -> new byte[] {
                (byte) ColorMath.blue(rgb),
                (byte) ColorMath.green(rgb),
                (byte) ColorMath.red(rgb)};
        
        if (btmUp) for (int line = lines-1; line >= 0; line--) {
            final int off = line*width;
            for (int i = 0; i < width; i++)
                stream.write( serializer.toBytes(argb[off+i]) );
            if (padding != 0)
                stream.write(new byte[padding]);
        }
        
        else for (int line = 0; line < lines; line++) {
            final int off = line*width;
            for (int i = 0; i < width; i++)
                stream.write( serializer.toBytes(argb[off+i]) );
            if (padding != 0)
                stream.write(new byte[padding]);
        }
    }
    
    @Contract(pure = true)
    public static int lineLengthOf(int width, int bands) {
        if (bands == 4)
            return width * bands;
        
        else if (bands == 3) {
            final int data = width * bands;
            int padding = data % 4;
            if (padding != 0)
                padding = 4 - padding;
    
            return data + padding;
        }
        
        throw new IllegalArgumentException("illegal bands: "+bands);
    }
    
    @FunctionalInterface
    private static interface ARGBSerializer {
        abstract byte[] toBytes(int i);
    }
    
}
