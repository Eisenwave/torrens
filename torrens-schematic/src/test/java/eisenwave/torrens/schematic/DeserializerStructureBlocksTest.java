package eisenwave.torrens.schematic;

import eisenwave.torrens.schematic.legacy.MicroLegacyUtil;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;

public class DeserializerStructureBlocksTest {
    
    @Test
    public void testSingleBlock() throws IOException {
        BlockStructure structure = new BlockStructure(1, 1, 1);
        structure.addBlock(0, 0, 0, BlockKey.minecraft("stone"), null);
        
        //System.out.println(new SerializerStructureBlocks().toMSONString(structure));
        
        byte[] bytes = new SerializerStructureBlocks().toBytes(structure);
        BlockStructure actual = new DeserializerStructureBlocks().fromBytes(bytes);
        
        assertEquals(structure, actual);
    }
    
    @Test
    public void testRandomBlocks() throws IOException {
        Random random = ThreadLocalRandom.current();
        
        BlockStructure expected = new BlockStructure(10, 10, 10);
        
        for (int x = 0; x < expected.getSizeX(); x++)
            for (int y = 0; y < expected.getSizeY(); y++)
                for (int z = 0; z < expected.getSizeZ(); z++) {
                    BlockKey key = MicroLegacyUtil.getByLegacyKey(random.nextInt(128), (byte) 0);
                    assert key != null;
                    expected.addBlock(x, y, z, key, null);
                }
        
        //structure.addBlock(0, 0, 0, BlockKey.minecraft("stone"), null);
        
        //System.out.println(new SerializerStructureBlocks().toMSONString(expected));
        
        byte[] bytes = new SerializerStructureBlocks().toBytes(expected);
        BlockStructure actual = new DeserializerStructureBlocks().fromBytes(bytes);
        
        assertEquals(expected, actual);
    }
    
}
