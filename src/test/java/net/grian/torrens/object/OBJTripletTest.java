package net.grian.torrens.object;

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

}