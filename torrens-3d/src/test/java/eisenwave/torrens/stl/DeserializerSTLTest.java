package eisenwave.torrens.stl;

import org.junit.Test;

import static org.junit.Assert.*;

public class DeserializerSTLTest {
    
    @Test
    public void deserializeDebugModel() throws Exception {
        STLModel model = new DeserializerSTL().fromResource(getClass(), "debug.stl");
        assertNotNull(model);
    }
    
    @Test
    public void deserializeAsciiModel() throws Exception {
        STLModel model = new DeserializerSTL().fromResource(getClass(), "ascii.stl");
        assertEquals(4, model.size());
    }
    
}
