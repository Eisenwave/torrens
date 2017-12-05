package eisenwave.torrens.img.gif;

import eisenwave.torrens.io.VoidOutputStream;

import java.io.InputStream;

//import static org.junit.Assert.*;

public class GIFDecoderTest {
    
    public void readAndWrite() throws Exception {
        InputStream source = getClass().getClassLoader().getResourceAsStream("ride.gif");
        GIFDecoder decoder = new GIFDecoder(source);
        GIFEncoder encoder = new GIFEncoder(new VoidOutputStream());
        
        encoder.writeHeader(decoder.getHeader());
        
        while (decoder.hasNext()) {
            GIFFrame frame = decoder.next();
            encoder.write(frame);
            System.out.println(frame);
        }
        
        //System.out.println(stream);
        //System.out.println(stream.get(0).getData());
    }
    
}
