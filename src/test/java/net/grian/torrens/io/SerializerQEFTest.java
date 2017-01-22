package net.grian.torrens.io;

import net.grian.spatium.voxel.VoxelArray;
import org.junit.Test;

import static org.junit.Assert.*;

public class SerializerQEFTest {

    @Test
    public void preserveVoxels() throws Exception {
        VoxelArray voxels1 = new DeserializerQEF().fromResource(getClass(), "debug.qef");
        byte[] bytes = new SerializerQEF().toBytes(voxels1);
        VoxelArray voxels2 = new DeserializerQEF().fromBytes(bytes);

        assertEquals(voxels1.size(), voxels2.size());
    }

}