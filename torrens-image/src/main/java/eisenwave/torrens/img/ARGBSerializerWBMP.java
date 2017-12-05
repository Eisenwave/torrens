package eisenwave.torrens.img;

import eisenwave.torrens.util.ColorMath;
import org.jetbrains.annotations.Contract;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.IntPredicate;

public class ARGBSerializerWBMP implements ARGBSerializer {
    
    public static int
        TYPE_BRIGHTNESS = 0,
        TYPE_ALPHA = 1;
    
    private final static int
        TYPE = 0,
        RESERVED = 0;
    
    private final int type;
    
    public ARGBSerializerWBMP(int type) {
        if (type != TYPE_BRIGHTNESS && type != TYPE_ALPHA)
            throw new IllegalArgumentException("unknown type: " + type);
        this.type = type;
    }
    
    public ARGBSerializerWBMP() {
        this(TYPE_BRIGHTNESS);
    }
    
    @Override
    public void toStream(BaseTexture texture, OutputStream stream) throws IOException {
        final int
            w = texture.getWidth(),
            h = texture.getHeight();
        
        stream.write(new byte[] {TYPE, RESERVED});
        stream.write(intToMultiByte(w));
        stream.write(intToMultiByte(h));
        
        // reuse same array for each row
        int[] argb = new int[w];
        
        for (int i = 0; i < h; i++)
            stream.write(serializeRow(texture, argb, i));
    }
    
    private byte[] serializeRow(BaseTexture texture, int[] argb, int row) {
        texture.get(0, row, argb.length, 1, argb, 0);
        
        IntPredicate predicate = type == TYPE_BRIGHTNESS?
            color -> ColorMath.brightness(color) >= 0.5F :
            ColorMath::isVisible;
        
        final byte[] bytes = argb.length % 8 == 0?
            new byte[argb.length / 8] :
            new byte[argb.length / 8 + 1];
        
        for (int i = 0; i < argb.length; i++) {
            //System.out.println(row+" "+i+" "+predicate.test(argb[i])+" "+Integer.toHexString(argb[i]));
            int r = i % 8;
            byte bit = predicate.test(argb[i])? (byte) 0b10000000 : 0;
            
            if (r == 0)
                bytes[i / 8] = bit;
            else
                bytes[i / 8] |= (bit & 0xFF) >>> r;
            
            //System.out.println(row+": "+Integer.toBinaryString(bytes[i/8] & 0xFF));
        }
        
        return bytes;
    }
    
    // Convert an int value to WBMP multi-byte format.
    @Contract(pure = true)
    private static byte[] intToMultiByte(int intValue) {
        int numBitsLeft = getNumBits(intValue);
        byte[] multiBytes = new byte[(numBitsLeft + 6) / 7];
        
        int maxIndex = multiBytes.length - 1;
        for (int b = 0; b <= maxIndex; b++) {
            multiBytes[b] = (byte) ((intValue >>> ((maxIndex - b) * 7)) & 0x7f);
            if (b != maxIndex) {
                multiBytes[b] |= (byte) 0x80;
            }
        }
        
        return multiBytes;
    }
    
    // Get the number of bits required to represent an int.
    @Contract(pure = true)
    private static int getNumBits(int intValue) {
        int numBits = 32;
        int mask = 0x80000000;
        while (mask != 0 && (intValue & mask) == 0) {
            numBits--;
            mask >>>= 1;
        }
        return numBits;
    }
    
}
