package eisenwave.torrens.wavefront;

import eisenwave.torrens.wavefront.OBJTriplet;
import org.junit.Test;

import static org.junit.Assert.*;

public class OBJTripletTest {

    @Test
    public void testToString() throws Exception {
        assertEquals("7",     new OBJTriplet(7, 0, 0).toString());
        assertEquals("9/10",  new OBJTriplet(9, 10, 0).toString());
        assertEquals("3//6",  new OBJTriplet(3, 0, 6).toString());
        assertEquals("1/1/2", new OBJTriplet(1, 1, 2).toString());
    }
    
    @Test
    public void parseTriplet() throws Exception {
        assertEquals(new OBJTriplet(7, 0, 0), OBJTriplet.parse("7"));
        assertEquals(new OBJTriplet(9, 10, 0), OBJTriplet.parse("9/10"));
        assertEquals(new OBJTriplet(3, 0, 6), OBJTriplet.parse("3//6"));
        assertEquals(new OBJTriplet(1, 1, 2), OBJTriplet.parse("1/1/2"));
    }

}
