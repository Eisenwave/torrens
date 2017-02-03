package net.grian.torrens.mc;

import net.grian.spatium.voxel.BlockArray;
import net.grian.spatium.voxel.BlockKey;
import net.grian.torrens.mc.DeserializerSchematic;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeserializerSchematicTest {

    public final static BlockKey COAL_BLOCK = new BlockKey(173, 0);

    @Test
    public void deserialize_bunny() throws Exception {
        BlockArray blocks = new DeserializerSchematic().fromResource(getClass(), "bunny.schematic");
        /*
        blocks.forEachPos((x,y,z) -> {
            if (blocks.getBlock(x, y, z).equals(COAL_BLOCK))
                System.out.println(BlockVector.fromXYZ(x, y, z));
        });
        */

        assertEquals(COAL_BLOCK, blocks.getBlock(50, 30, 36));
    }
    
    @Test
    public void deserialize_farm() throws Exception {
        BlockArray blocks = new DeserializerSchematic().fromResource(getClass(), "farm.schematic");
        System.out.println(blocks);
    }

}