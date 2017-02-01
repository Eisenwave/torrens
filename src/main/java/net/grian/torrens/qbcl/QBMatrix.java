package net.grian.torrens.qbcl;

import net.grian.spatium.geo3.BlockSelection;
import net.grian.spatium.geo3.BlockVector;
import net.grian.spatium.voxel.VoxelArray;

import java.util.Objects;

public class QBMatrix {

    private final String name;
    private final int minX, minY, minZ, maxX, maxY, maxZ;
    private final VoxelArray voxels;

    public QBMatrix(String name, int x, int y, int z, VoxelArray voxels) {
        Objects.requireNonNull(voxels, "voxels must not be null");
        this.name = name;
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + voxels.getSizeX()-1;
        this.maxY = y + voxels.getSizeY()-1;
        this.maxZ = z + voxels.getSizeZ()-1;
        this.voxels = voxels;
    }

    public QBMatrix(QBMatrix copyOf) {
        this(copyOf.name, copyOf.minX, copyOf.minY, copyOf.minZ, copyOf.voxels);
    }

    /**
     * Returns the name of this matrix.
     *
     * @return the name of this matrix
     */
    public String getName() {
        return name;
    }

    public int getMinX() {
        return minX;
    }

    public int getMinY() {
        return minY;
    }

    public int getMinZ() {
        return minZ;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getMaxZ() {
        return maxZ;
    }

    public BlockVector getPosition() {
        return BlockVector.fromXYZ(minX, minY, minZ);
    }

    /**
     * Returns the boundaries of this matrix.
     *
     * @return the boundaries of this matrix
     */
    public BlockSelection getBoundaries() {
        return BlockSelection.fromPoints(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Returns the voxel array positioned in this element.
     *
     * @return the voxels
     */
    public VoxelArray getVoxels() {
        return voxels;
    }

    @Override
    public QBMatrix clone() {
        return new QBMatrix(this);
    }

    @Override
    public String toString() {
        return QBMatrix.class.getSimpleName()+
                "{name="+
                ",voxels="+voxels+"}";
    }
}
