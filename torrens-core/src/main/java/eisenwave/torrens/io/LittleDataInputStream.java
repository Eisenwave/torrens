package eisenwave.torrens.io;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Input stream which extends {@link DataInputStream} to provide methods for reading little endian data.
 *
 * @author Headaxe
 * @see LittleDataOutputStream
 */
public class LittleDataInputStream extends DataInputStream {
    
    public LittleDataInputStream(InputStream in) {
        super(in);
    }
    
    public final short readLittleShort() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (short) ((ch1) + (ch2 << 8));
    }
    
    public final int readLittleUnsignedShort() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (ch1) + (ch2 << 8);
    }
    
    public final char readLittleChar() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        if ((ch1 | ch2) < 0)
            throw new EOFException();
        return (char) ((ch1) + (ch2 << 8));
    }
    
    public final int readLittleInt() throws IOException {
        int ch1 = in.read();
        int ch2 = in.read();
        int ch3 = in.read();
        int ch4 = in.read();
        if ((ch1 | ch2 | ch3 | ch4) < 0)
            throw new EOFException();
        return ((ch1) + (ch2 << 8) + (ch3 << 16) + (ch4 << 24));
    }
    
    private byte readBuffer[] = new byte[8];
    
    public final long readLittleLong() throws IOException {
        readFully(readBuffer, 0, 8);
        return
            ((long)(readBuffer[0] & 255) << 56) +
            ((long)(readBuffer[1] & 255) << 48) +
            ((long)(readBuffer[2] & 255) << 40) +
            ((long)(readBuffer[3] & 255) << 32) +
            ((long)(readBuffer[4] & 255) << 24) +
            ((readBuffer[5] & 255) << 16) +
            ((readBuffer[6] & 255) <<  8) +
            ((readBuffer[7] & 255));
    }
    
    public final float readLittleFloat() throws IOException {
        return Float.intBitsToFloat(readLittleInt());
    }
    
    public final double readLittleDouble() throws IOException {
        return Double.longBitsToDouble(readLittleLong());
    }
    
}
