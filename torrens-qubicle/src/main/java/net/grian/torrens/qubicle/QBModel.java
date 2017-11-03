package net.grian.torrens.util.qubicle;

import net.grian.spatium.geo3.BlockSelection;
import net.grian.torrens.voxel.VoxelArray;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class QBModel implements Cloneable, Serializable, Iterable<QBMatrix> {

    private final List<QBMatrix> matrices = new ArrayList<>();
    
    /**
     * Constructs an empty model.
     */
    public QBModel() {}
    
    /**
     * Constructs a model using several matrices.
     */
    public QBModel(QBMatrix... matrices) {
        this.addAll(Arrays.asList(matrices));
    }
    
    /**
     * Constructs a model by wrapping voxel data.
     */
    public QBModel(String matrixName, VoxelArray data) {
        matrices.add(new QBMatrix(matrixName, 0, 0, 0, data));
    }
    
    /**
     * Copy constructor.
     *
     * @param copyOf the copy
     */
    public QBModel(QBModel copyOf) {
        for (QBMatrix e : copyOf)
            matrices.add(e.clone());
    }

    /**
     * Returns the amount of matrices in this model.
     *
     * @return the matrix count
     */
    public int size() {
        return matrices.size();
    }

    /**
     * Returns the combined volume of all voxel arrays in this mesh.
     *
     * @return the combined volume of all arrays
     */
    public int getCombinedVolume() {
        int v = 0;
        for (QBMatrix e : matrices)
            v += e.getVoxels().getVolume();
        return v;
    }

    /**
     * Returns the total amount of voxels in this mesh.
     *
     * @return the voxel count
     */
    public int voxelCount() {
        int count = 0;
        for (QBMatrix e : matrices)
            count += e.getVoxels().size();
        return count;
    }

    public QBMatrix[] getMatrices() {
        return matrices.toArray(new QBMatrix[matrices.size()]);
    }

    /**
     * Returns the boundaries of this model.
     *
     * @return the model boundaries
     */
    public BlockSelection getBoundaries() {
        if (isEmpty()) throw new IllegalStateException("empty meshes have no boundaries");
        int[] result = {
            Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
            Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE
        };

        for (QBMatrix matrix : matrices) {
            int[] minMax = getPoints(matrix);

            if (minMax[0] < result[0]) result[0] = minMax[0];
            if (minMax[1] < result[1]) result[1] = minMax[1];
            if (minMax[2] < result[2]) result[2] = minMax[2];
            if (minMax[3] > result[3]) result[3] = minMax[3];
            if (minMax[4] > result[4]) result[4] = minMax[4];
            if (minMax[5] > result[5]) result[5] = minMax[5];
        }

        return BlockSelection.fromPoints(result[0], result[1], result[2], result[3], result[4], result[5]);
    }

    /**
     * Adds a matrix to this model.
     *
     * @param matrix the matrix
     */
    public void add(QBMatrix matrix) {
        this.matrices.add(Objects.requireNonNull(matrix));
    }
    
    /**
     * Adds multiple matrices to this model.
     *
     * @param matrices matrices
     */
    public void addAll(Collection<? extends QBMatrix> matrices) {
        for (QBMatrix matrix : matrices) {
            add(matrix);
        }
    }

    /**
     * Removes all matrices from this model.
     */
    public void clear() {
        this.matrices.clear();
    }

    // CHECKERS

    public boolean isEmpty() {
        return matrices.isEmpty();
    }

    // MISC

    @Override
    public String toString() {
        return QBModel.class.getSimpleName() + "{size="+size()+"}";
    }

    @Override
    public QBModel clone() {
        return new QBModel(this);
    }

    @NotNull
    @Override
    public Iterator<QBMatrix> iterator() {
        return matrices.iterator();
    }
    
    // UTIL
    
    private static int[] getPoints(QBMatrix matrix) {
        return new int[] {
            matrix.getMinX(), matrix.getMinY(), matrix.getMinZ(),
            matrix.getMaxX(), matrix.getMaxY(), matrix.getMaxZ()
        };
    }
    
}
