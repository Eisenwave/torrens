package net.grian.torrens.wavefront;

import org.junit.Test;

import java.io.File;
import java.util.logging.Logger;

public class DeserializerMTLTest {
    
    @Test
    public void readDebugMTL() throws Exception {
        MTLLibrary mtllib = new MTLLibrary("Debug");
        //debug.obj.mtl contains no map references
        //so it is unneeded by the deserializer but must be nonnull
        File unneeded = new File("");
        
        new DeserializerMTL(mtllib, unneeded, Logger.getLogger("voxelvert.debug.obj"))
            .fromResource(getClass(), "debug.mtl");
        
        System.out.println(mtllib);
    }
    
}