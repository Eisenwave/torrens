package net.grian.torrens.util.voxel;

import net.grian.spatium.geo3.BlockVector;

/**
 * A common interface for all three-dimensional, array-like collections.
 */
public interface BitArray3 {

    /**
     * Returns the dimension on the x-axis.
     *
     * @return the dimension on the x-axis
     */
    abstract int getSizeX();

    /**
     * Returns the dimension on the y-axis.
     *
     * @return the dimension on the y-axis
     */
    abstract int getSizeY();

    /**
     * Returns the dimension on the z-axis.
     *
     * @return the dimension on the z-axis
     */
    abstract int getSizeZ();

    /**
     * Returns the volume. This is equivalent to the product of all dimensions.
     *
     * @return the volume
     */
    default int getVolume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }

    /**
     * Returns the dimensions on x, y and z axis.
     *
     * @return the dimensions
     */
    default BlockVector getDimensions() {
        return BlockVector.fromXYZ(getSizeX(), getSizeY(), getSizeZ());
    }

    /**
     * Checks whether the field contains an element at the given position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return whether there is an element
     */
    abstract boolean contains(int x, int y, int z);

    /**
     * Checks whether the array contains an element at the given position.
     *
     * @param pos the position
     * @return whether there is an element
     */
    default boolean contains(BlockVector pos) {
        return contains(pos.getX(), pos.getY(), pos.getZ());
    }

    /**
     * Returns the total amount of elements in this array.
     *
     * @return the total amount of elements in this array
     */
    default int size() {
        final int limX = getSizeX(), limY = getSizeY(), limZ = getSizeZ();
        int count = 0;

        for (int x = 0; x<limX; x++)
            for (int y = 0; y<limY; y++)
                for (int z = 0; z<limZ; z++)
                    if (contains(x, y, z)) count++;

        return count;
    }

}
