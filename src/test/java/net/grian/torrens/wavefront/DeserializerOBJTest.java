package net.grian.torrens.wavefront;

import org.junit.Test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeserializerOBJTest {
    
    @Test
    public void readsDebugOBJ() throws Exception {
        OBJModel model = new OBJModel();
        File unneeded = new File("");
        Logger logger = Logger.getLogger("voxelver.debug");
        logger.setLevel(Level.FINE);
        
        new DeserializerOBJ(model, unneeded, logger).fromResource(getClass(), "debug.obj");
        
        System.out.println(model);
    }
    
}