package eisenwave.torrens.schematic;

import eisenwave.torrens.schematic.legacy.BlockKey;
import eisenwave.torrens.schematic.legacy.LegacyBlockStructure;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeserializerSchematicBlocksTest {

    public final static BlockKey COAL_BLOCK = new BlockKey(173, 0);

    @Test
    public void deserialize_bunny() throws Exception {
        LegacyBlockStructure blocks = new DeserializerSchematicBlocks().fromResource(getClass(), "bunny.schematic");
        //System.out.println(blocks);
        /*
        blocks.forEachPos((x,y,z) -> {
            if (blocks.getBlock(x, y, z).equals(COAL_BLOCK))
                System.out.println(BlockVector.fromXYZ(x, y, z));
        });
        */

        assertEquals(COAL_BLOCK, blocks.getBlock(50, 30, 36));
    }
    
    public void deserialize_farm() throws Exception {
        LegacyBlockStructure blocks = new DeserializerSchematicBlocks().fromResource(getClass(), "farm.schematic");
        System.out.println(blocks);
    }

}
