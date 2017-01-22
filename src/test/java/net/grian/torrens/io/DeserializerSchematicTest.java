package net.grian.torrens.io;

import net.grian.spatium.voxel.BlockArray;
import net.grian.spatium.voxel.BlockKey;
import org.junit.Test;

import static org.junit.Assert.*;

public class DeserializerSchematicTest {

    public final static BlockKey COAL_BLOCK = new BlockKey(173, 0);

    @Test
    public void deserialize() throws Exception {
        BlockArray blocks = new DeserializerSchematic().fromResource(getClass(), "bunny.schematic");
        /*
        blocks.forEachPos((x,y,z) -> {
            if (blocks.getBlock(x, y, z).equals(COAL_BLOCK))
                System.out.println(BlockVector.fromXYZ(x, y, z));
        });
        */

        assertEquals(COAL_BLOCK, blocks.getBlock(50, 30, 36));
    }

}