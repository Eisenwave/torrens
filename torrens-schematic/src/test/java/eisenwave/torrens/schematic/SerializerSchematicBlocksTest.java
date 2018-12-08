package eisenwave.torrens.schematic;

import eisenwave.torrens.schematic.legacy.LegacyBlockStructure;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class SerializerSchematicBlocksTest {
    
    @Test
    public void serialize() throws IOException {
        LegacyBlockStructure blocks0 = new DeserializerSchematicBlocks().fromResource(getClass(), "bunny.schematic");
        
        byte[] bytes = new SerializerSchematicBlocks().toBytes(blocks0);
        //System.out.println(bytes.length/1000+" kB");
    
        LegacyBlockStructure blocks1 = new DeserializerSchematicBlocks().fromBytes(bytes);
        
        assertEquals(blocks0, blocks1);
    }
    
}
