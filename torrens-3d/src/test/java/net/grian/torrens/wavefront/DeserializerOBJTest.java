package net.grian.torrens.wavefront;

import net.grian.torrens.wavefront.DeserializerOBJ;
import net.grian.torrens.wavefront.OBJModel;
import org.junit.Test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeserializerOBJTest {
    
    @Test
    public void readsDebugOBJ() throws Exception {
        OBJModel model = new OBJModel();
        File unneeded = new File("");
        Logger logger = Logger.getLogger("voxelvert.debug");
        logger.setLevel(Level.FINE);
        
        new DeserializerOBJ(model, unneeded, logger).fromResource(getClass(), "debug.obj");
        
        System.out.println(model);
    }
    
}