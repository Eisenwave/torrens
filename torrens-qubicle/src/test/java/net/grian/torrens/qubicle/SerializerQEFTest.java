package net.grian.torrens.qubicle;

import net.grian.torrens.voxel.VoxelArray;
import org.junit.Test;

import static org.junit.Assert.*;

public class SerializerQEFTest {

    @Test
    public void preserveVoxels() throws Exception {
        VoxelArray voxels1 = new DeserializerQEF().fromResource(getClass(), "debug.qef");
        char[] chars = new SerializerQEF().toCharArray(voxels1);
        VoxelArray voxels2 = new DeserializerQEF().fromCharArray(chars);

        assertEquals(voxels1, voxels2);
    }

}