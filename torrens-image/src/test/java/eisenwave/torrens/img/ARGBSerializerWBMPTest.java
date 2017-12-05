package eisenwave.torrens.img;

import eisenwave.torrens.io.SerializerByteArray;
import eisenwave.torrens.util.ColorMath;
import org.junit.Test;

import java.io.File;

public class ARGBSerializerWBMPTest {
    
    private final static File DEBUG_FILE = new File("F:/Porn/WBMPTest.wbmp");
    
    @Test
    public void toStream() throws Exception {
        Texture t = Texture.alloc(3, 3);
        t.getGraphics().drawAll(ColorMath.SOLID_WHITE);
        t.set(1, 0, ColorMath.SOLID_BLACK);
        t.set(0, 1, ColorMath.SOLID_BLACK);
        t.set(2, 1, ColorMath.SOLID_BLACK);
        t.set(1, 2, ColorMath.SOLID_BLACK);
        
        //byte b = (byte) 0b10000000;
        //System.out.println( Integer.toBinaryString(b & 0xFF) );
        //System.out.println( Integer.toBinaryString((b & 0xFF) >> 3) );
        //System.out.println( Integer.toBinaryString((b & 0xFF) >>> 3) );
        
        byte[] bytes = new ARGBSerializerWBMP(ARGBSerializerWBMP.TYPE_BRIGHTNESS).toBytes(t);
        
        if (DEBUG_FILE.canWrite())
            new SerializerByteArray().toFile(bytes, DEBUG_FILE);
    }
    
}
