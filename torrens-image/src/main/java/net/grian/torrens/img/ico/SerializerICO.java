package net.grian.torrens.img.ico;

import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.img.ARGBSerializerBMP;
import net.grian.torrens.img.SerializerPNG;
import net.grian.torrens.img.Texture;
import net.grian.torrens.io.LittleDataOutputStream;
import net.grian.torrens.io.Serializer;
import net.grian.torrens.util.FileConstants;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SerializerICO implements Serializer<BufferedImage[]> {
    
    public final static int
        TYPE_BMP = 1,
        TYPE_PNG = 2;
    
    private final int format;
    
    public SerializerICO(int format) {
        if (format != TYPE_PNG && format != TYPE_BMP)
            throw new IllegalArgumentException("unknown format: "+format);
        
        this.format = format;
    }
    
    public SerializerICO() {
        this(TYPE_BMP);
    }
    
    @Override
    public void toStream(BufferedImage[] icons, OutputStream stream) throws IOException {
        LittleDataOutputStream dataStream = new LittleDataOutputStream(stream);
        toStream(icons, dataStream);
    }
    
    private void toStream(BufferedImage[] icons, LittleDataOutputStream stream) throws IOException {
        if (icons.length == 0)
            throw new FileSyntaxException("ICO file can't be written with 0 images");
        
        stream.writeLittleShort(0); // reserved
        stream.writeLittleShort(1); // 1 for ICO, 2 for CUR
        stream.writeLittleShort(icons.length);
    
        ICODirEntry[] entries = new ICODirEntry[icons.length];
        byte[][] imgDataArr = new byte[icons.length][];
        
        for (int i = 0; i < icons.length; i++) {
            
            BufferedImage icon = icons[i];
            final int width = icon.getWidth(), height = icon.getHeight();
            if (width > 256 || height > 256)
                throw new FileSyntaxException("ICO file format does not support images with w./h. > 256 pixels");
            
            byte[] imgData;
            ICODirEntry entry;
            
            if (format == TYPE_BMP) {
                imgData = new ARGBSerializerBMP(true, true, true).toBytes(Texture.wrapOrCopy(icon));
                entry = makeBMPEntry(imgData, width, height, 0);
                
                // windows icons require double height due to bullshit
                ByteBuffer buffer = ByteBuffer.wrap(imgData);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.putInt(8, entry.height*2);
            }
            
            else {
                assert format == TYPE_PNG;
                imgData = new SerializerPNG().toBytes(icon);
                //System.out.println("writing PNG: "+ByteArrays.toHexString(imgData));
                entry = makePNGEntry(imgData, width, height, 0);
            }
    
            imgDataArr[i] = imgData;
            entries[i] = entry;
        }
        
        entries[0].offset = FileConstants.ICO_HEADER_LENGTH + entries.length*FileConstants.ICO_ICONDIRENTRY_LENGTH;
        for (int i = 1; i < entries.length; i++)
            entries[i].offset = entries[i-1].offset + entries[i-1].data;
    
        for (ICODirEntry entry : entries)
            writeEntry(entry, stream);
        
        for (byte[] imgData : imgDataArr)
            stream.write(imgData);
    }
    
    @NotNull
    private static ICODirEntry makeBMPEntry(byte[] bmp, int width, int height, int offset) {
        ByteBuffer buffer = ByteBuffer.wrap(bmp);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        final int
            planes = buffer.getShort(12),
            bpPixel = buffer.getShort(14),
            palette = buffer.getInt(34);
        
        return new ICODirEntry(width, height, palette, planes, bpPixel, bmp.length, offset);
    }
    
    @NotNull
    private static ICODirEntry makePNGEntry(byte[] png, int width, int height, int offset) {
        /*
        ByteBuffer buffer = ByteBuffer.wrap(png);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        final int
            planes = buffer.getShort(12),
            bpPixel = buffer.getShort(14),
            palette = buffer.getInt(34);
        */
        
        return new ICODirEntry(width, height, 0, 0, 0, png.length, offset);
    }
    
    private void writeEntry(ICODirEntry entry, LittleDataOutputStream stream) throws IOException {
        stream.writeByte(entry.width);
        stream.writeByte(entry.height);
        stream.writeByte(entry.palette);
        stream.writeByte(0); //reserved
        stream.writeLittleShort(entry.planes);
        stream.writeLittleShort(entry.bitsPerPixel);
        stream.writeLittleInt(entry.data);
        stream.writeLittleInt(entry.offset);
    }
    
}