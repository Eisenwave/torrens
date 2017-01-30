package net.grian.torrens.io;

import net.grian.torrens.object.OBJMaterialLibrary;
import org.junit.Test;

import java.io.File;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class DeserializerMTLTest {
    
    @Test
    public void readDebugMTL() throws Exception {
        OBJMaterialLibrary mtllib = new OBJMaterialLibrary("Debug");
        //debug.mtl contains no map references
        //so it is unneeded by the deserializer but must be nonnull
        File unneeded = new File("");
        
        new DeserializerMTL(mtllib, unneeded, Logger.getLogger("voxelvert.debug"))
            .fromResource(getClass(), "debug.mtl");
        
        System.out.println(mtllib);
    }
    
}