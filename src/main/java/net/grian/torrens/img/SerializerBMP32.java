package net.grian.torrens.img;

import net.grian.torrens.io.LittleDataOutputStream;
import net.grian.torrens.io.Serializer;
import net.grian.torrens.util.FileConstants;

import java.io.IOException;
import java.io.OutputStream;

public class SerializerBMP32 implements Serializer<BaseTexture> {
    
    private final static int
        BI_OFF_BITS = FileConstants.BMP_FILE_HEADER_LENGTH + FileConstants.BMP_INFO_HEADER_LENGTH,
        BI_PLANES = 1,
        BI_BIT_COUNT = 32,
        BI_COMPRESSION = 0, //uncompressed
        BI_X_PIXELS_PER_METER = 0,
        BI_Y_PIXELS_PER_METER = 0,
        BI_CLR_USED = 0, //color table size in bytes
        BI_CLR_IMPORTANT = 0;
    
    private final boolean header;
    private final boolean btmUp;
    
    public SerializerBMP32(boolean skipHeader, boolean btmUp) {
        this.header = !skipHeader;
        this.btmUp = btmUp;
    }
    
    public SerializerBMP32() {
        this(false, false);
    }
    
    @Override
    public void toStream(BaseTexture img, OutputStream stream) throws IOException {
        LittleDataOutputStream dataStream = new LittleDataOutputStream(stream);
        toStream(img, dataStream);
    }
    
    public void toStream(BaseTexture img, LittleDataOutputStream stream) throws IOException {
        final int
            width = img.getWidth(),
            height = img.getHeight(),
            data = width * height * 4;
        
        // BITMAPFILEHEADER
        if (header) {
            stream.writeShort(0x424D); // ASCII "BM"
            stream.writeLittleInt(BI_OFF_BITS + data); //file size, deemed "unreliable"
            stream.write(new byte[4]); //reserved
            stream.writeLittleInt(BI_OFF_BITS);
        }
        
        // BITMAPINFOHEADER
        stream.writeLittleInt(FileConstants.BMP_INFO_HEADER_LENGTH);
        stream.writeLittleInt(width);
        stream.writeLittleInt(btmUp? height : -height);
        stream.writeLittleShort(BI_PLANES);
        stream.writeLittleShort(BI_BIT_COUNT);
        stream.writeLittleInt(BI_COMPRESSION);
        stream.writeLittleInt(data);
        stream.writeLittleInt(BI_X_PIXELS_PER_METER);
        stream.writeLittleInt(BI_Y_PIXELS_PER_METER);
        stream.writeLittleInt(BI_CLR_USED);
        stream.writeLittleInt(BI_CLR_IMPORTANT);
        
        // ARGB ARRAY
        int[] argb = img.get(0, 0, width, height);
        
        if (btmUp) {
            for (int row = height-1; row >= 0; row--) {
                final int off = row*width;
                for (int i = 0; i < width; i++)
                    stream.writeLittleInt(argb[off+i]);
            }
        }
        
        else {
            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < argb.length; i++)
                stream.writeLittleInt(argb[i]);
        }
    }
    
}