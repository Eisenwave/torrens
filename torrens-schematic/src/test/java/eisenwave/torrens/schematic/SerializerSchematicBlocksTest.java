package eisenwave.torrens.schematic;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;


public class SerializerSchematicBlocksTest {
    
    @Test
    public void serialize() throws IOException {
        BlockStructure blocks0 = new DeserializerSchematicBlocks().fromResource(getClass(), "bunny.schematic");
        
        byte[] bytes = new SerializerSchematicBlocks().toBytes(blocks0);
        //System.out.println(bytes.length/1000+" kB");
    
        BlockStructure blocks1 = new DeserializerSchematicBlocks().fromBytes(bytes);
        
        assertEquals(blocks0, blocks1);
    }
    
}
