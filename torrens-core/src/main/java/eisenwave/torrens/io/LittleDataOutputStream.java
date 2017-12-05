package eisenwave.torrens.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Output stream which extends {@link DataOutputStream} to provide methods for writing little endian data.
 *
 * @author Headaxe
 * @see LittleDataInputStream
 */
public class LittleDataOutputStream extends DataOutputStream {
    
    public LittleDataOutputStream(OutputStream out) {
        super(out);
    }
    
    private void incCount(int value) {
        int temp = written + value;
        if (temp < 0) {
            temp = Integer.MAX_VALUE;
        }
        written = temp;
    }
    
    public final void writeLittleShort(int v) throws IOException {
        out.write((v) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        incCount(2);
    }
    
    public final void writeLittleChar(int v) throws IOException {
        out.write((v) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        incCount(2);
    }
    
    public final void writeLittleInt(int v) throws IOException {
        out.write((v) & 0xFF);
        out.write((v >>> 8) & 0xFF);
        out.write((v >>>  16) & 0xFF);
        out.write((v >>>  24) & 0xFF);
        incCount(4);
    }
    
    private byte writeBuffer[] = new byte[8];
    
    public final void writeLittleLong(long v) throws IOException {
        writeBuffer[0] = (byte)(v >>> 56);
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v);
        out.write(writeBuffer, 0, 8);
        incCount(8);
    }
    
    public final void writeLittleFloat(float v) throws IOException {
        writeLittleInt(Float.floatToIntBits(v));
    }
    
    public final void writeLittleDouble(double v) throws IOException {
        writeLittleLong(Double.doubleToLongBits(v));
    }
    
}
