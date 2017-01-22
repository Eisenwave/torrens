package net.grian.torrens.object;

import org.junit.Test;

import static org.junit.Assert.*;

public class OBJTripletTest {

    @Test
    public void testToString() throws Exception {
        assertEquals("7", new OBJTriplet(7, -1, -1).toString());
        assertEquals("9/10", new OBJTriplet(9, 10, -1).toString());
        assertEquals("3//6", new OBJTriplet(3, -1, 6).toString());
        assertEquals("0/1/2", new OBJTriplet(0, 1, 2).toString());
    }

}