package net.grian.torrens.stl;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeserializerSTLTest {
    
    @Test
    public void deserializeDebugModel() throws Exception {
        STLModel model = new DeserializerSTL().fromResource(getClass(), "debug.stl");
        assertNotNull(model);
    }
    
}