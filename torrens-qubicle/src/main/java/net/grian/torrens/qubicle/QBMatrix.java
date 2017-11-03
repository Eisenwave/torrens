package net.grian.torrens.qubicle;

import net.grian.torrens.object.BoundingBox6i;
import net.grian.torrens.object.Vertex3i;
import net.grian.torrens.voxel.VoxelArray;

import java.io.Serializable;
import java.util.Objects;

public class QBMatrix implements Cloneable, Serializable {

    private final String name;
    private final int minX, minY, minZ, maxX, maxY, maxZ;
    private final VoxelArray voxels;
    
    /**
     * Constructs a new QB matrix with given voxel contents.
     *
     * @param name the matrix name
     * @param x the x-coordinate of the matrix
     * @param y the y-coordinate of the matrix
     * @param z the z-coordinate of the matrix
     * @param voxels the matrix voxels
     */
    public QBMatrix(String name, int x, int y, int z, VoxelArray voxels) {
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.voxels = Objects.requireNonNull(voxels, "voxels must not be null");
        
        this.minX = x;
        this.minY = y;
        this.minZ = z;
        this.maxX = x + voxels.getSizeX()-1;
        this.maxY = y + voxels.getSizeY()-1;
        this.maxZ = z + voxels.getSizeZ()-1;
    }

    public QBMatrix(QBMatrix copyOf) {
        this.name = copyOf.name;
        this.minX = copyOf.minX;
        this.minY = copyOf.minY;
        this.minZ = copyOf.minZ;
        this.maxX = copyOf.maxX;
        this.maxY = copyOf.maxY;
        this.maxZ = copyOf.maxZ;
        this.voxels = copyOf.voxels.clone();
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

    public Vertex3i getPosition() {
        return new Vertex3i(minX, minY, minZ);
    }

    /**
     * Returns the boundaries of this matrix.
     *
     * @return the boundaries of this matrix
     */
    public BoundingBox6i getBoundaries() {
        return new BoundingBox6i(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Returns the voxel array positioned in this element.
     *
     * @return the voxels
     */
    public VoxelArray getVoxels() {
        return voxels;
    }
    
    // MISC

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
