package net.grian.torrens.object;

import net.grian.spatium.geo.BlockSelection;
import net.grian.spatium.voxel.VoxelArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QBModel implements Serializable, Iterable<QBMatrix> {

    private final List<QBMatrix> matrices = new ArrayList<>();

    public QBModel() {}

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
        int[] pointsR = {
                Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE,
                Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

        for (QBMatrix matrix : matrices) {
            int[] pointsM = getPoints(matrix);

            if (pointsM[0] < pointsR[0]) pointsR[0] = pointsM[0];
            if (pointsM[1] < pointsR[1]) pointsR[1] = pointsM[1];
            if (pointsM[2] < pointsR[2]) pointsR[2] = pointsM[2];
            if (pointsM[3] > pointsR[3]) pointsR[3] = pointsM[3];
            if (pointsM[4] > pointsR[4]) pointsR[4] = pointsM[4];
            if (pointsM[5] > pointsR[5]) pointsR[5] = pointsM[5];
        }

        return BlockSelection.fromPoints(pointsR[0], pointsR[1], pointsR[2], pointsR[3], pointsR[4], pointsR[5]);
    }

    private static int[] getPoints(QBMatrix matrix) {
        return new int[] {
                matrix.getMinX(), matrix.getMinY(), matrix.getMinZ(),
                matrix.getMaxX(), matrix.getMaxY(), matrix.getMaxZ()
        };
    }

    /**
     * Adds a matrix to this model.
     *
     * @param matrix the matrix
     */
    public void add(QBMatrix matrix) {
        this.matrices.add(matrix);
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

    @Override
    public Iterator<QBMatrix> iterator() {
        return matrices.iterator();
    }

}
