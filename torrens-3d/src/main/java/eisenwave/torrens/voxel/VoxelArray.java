package eisenwave.torrens.voxel;

import eisenwave.spatium.array.AbstractArray3;
import eisenwave.spatium.enums.Direction;
import eisenwave.spatium.function.Int3Consumer;
import eisenwave.spatium.util.Incrementer3;
import eisenwave.torrens.object.BoundingBox6i;
import eisenwave.torrens.object.Vertex3i;
import eisenwave.torrens.util.ColorMath;
import eisenwave.torrens.util.RGBValue;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

/**
 * <p>
 *     An array of voxels backed by a three-dimensional array of integers.
 * </p>
 * <p>
 *     This class is optimized for randomly accessing, setting, drawing etc. of voxels.
 * </p>
 */
public class VoxelArray extends AbstractArray3 implements BitArray3, Cloneable, Serializable,
    Iterable<VoxelArray.Voxel> {

    private int[] voxels;

    public VoxelArray(int x, int y, int z) {
        super(x, y, z);
        if (x == 0 || y == 0 || z == 0) throw new IllegalArgumentException("size 0 voxel array");
        this.voxels = new int[x * y * z];
    }
    
    public VoxelArray(VoxelArray copyOf) {
        super(copyOf.sizeX, copyOf.sizeY, copyOf.sizeZ);
        this.voxels = Arrays.copyOf(copyOf.voxels, copyOf.voxels.length);
    }

    /**
     * Returns a copy of a part of this array.
     *
     * @param xmin the min x
     * @param ymin the min y
     * @param zmin the min z
     * @param xmax the max x
     * @param ymax the max y
     * @param zmax the max z
     * @return a new sub array, copied out of this array
     */
    @NotNull
    public VoxelArray copy(int xmin, int ymin, int zmin, int xmax, int ymax, int zmax) {
        if (xmin < 0 || ymin < 0 || zmin < 0)
            throw new IllegalArgumentException("min ("+xmin+","+ymin+","+zmin+") out of boundaries");
        if (xmax >= getSizeX() || ymax >= getSizeY() || zmax >= getSizeZ())
            throw new IllegalArgumentException("max ("+xmax+","+ymax+","+zmax+") out of boundaries");

        VoxelArray result = new VoxelArray(xmax-xmin+1, ymax-ymin+1, zmax-zmin+1);
        for (int x = xmin; x<=xmax; x++)
            for (int y = ymin; y<=ymax; y++)
                for (int z = zmin; z<=zmax; z++)
                    result.setRGB(x-xmin, y-ymin, z-zmin, getRGB(x, y, z));

        return result;
    }

    /**
     * Returns the size of the array on the x-axis.
     *
     * @return the size on the x-axis
     */
    @Override
    public int getSizeX() {
        return sizeX;
    }

    /**
     * Returns the size of the array on the y-axis.
     *
     * @return the size on the y-axis
     */
    @Override
    public int getSizeY() {
        return sizeY;
    }

    /**
     * Returns the size of the array on the z-axis.
     *
     * @return the size on the z-axis
     */
    @Override
    public int getSizeZ() {
        return sizeZ;
    }

    /**
     * Returns the boundaries of this voxel array.
     *
     * @return the array boundaries
     */
    public BoundingBox6i getBoundaries() {
        return new BoundingBox6i(0, 0, 0, sizeX-1, sizeY-1, sizeZ-1);
    }

    /**
     * Returns the voxel at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return the voxel at the specified coordinates
     */
    @NotNull
    public Voxel getVoxel(int x, int y, int z) {
        return new Voxel(x, y, z);
    }

    /**
     * Returns the voxel at the specified coordinates.
     *
     * @param v the position
     * @return the voxel at the specified position
     */
    @NotNull
    public Voxel getVoxel(Vertex3i v) {
        return getVoxel(v.getX(), v.getY(), v.getZ());
    }

    /**
     * Returns the RGB value of the voxel at the specified coordinates. If the alpha ({@code rgb >> 24}) is 0, there is
     * no voxel at the position.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the color of the voxel at the position
     */
    public int getRGB(int x, int y, int z) {
        return voxels[indexOf(x, y, z)];
    }

    /**
     * Returns the RGB value of the voxel at the specified position. If the alpha ({@code rgb >> 24}) is 0, there is
     * no voxel at the position.
     *
     * @param v the voxel position
     * @return the color of the voxel at the position
     */
    public int getRGB(Vertex3i v) {
        return getRGB(v.getX(), v.getY(), v.getZ());
    }

    /**
     * <p>
     *    Returns a bit field representing the visibility of each side of the voxel.
     * </p>
     * <p>
     *     A face is always visible if there is no voxel covering that face. This also applies if the voxel face can
     *     not be covered by another voxel due to the array containing it ending at the face.
     * </p>
     * <p>
     *     The ordinal of the {@link Direction} represents the index of the bit which can 0 (covered) or 1 (visible).
     *     Checking which side is visible can be done with the formulas:
     *     <ul>
     *         <li><code>value >> ordinal & 1 == 1</code></li>
     *         <li><code>value & 1 << ordinal != 0</code></li>
     *     </ul>
     * </p>
     * <p>
     *     If the returned byte is exactly 0, the voxel is covered from every side, if it is {@code 0b00111111}
     *     the voxel is visible from every side.
     * </p>
     *
     * @return a bitmap representing which faces are visible
     */
    public byte getVisibilityMask(int x, int y, int z) {
        byte result = 0;

        if (x==0 || !contains(x-1, y, z))
            result |= (1 << Direction.NEGATIVE_X.ordinal());
        if (y==0 || !contains(x, y-1, z))
            result |= (1 << Direction.NEGATIVE_Y.ordinal());
        if (z==0 || !contains(x, y, z-1))
            result |= (1 << Direction.NEGATIVE_Z.ordinal());

        if (x==sizeX-1 || !contains(x+1, y, z))
            result |= (1 << Direction.POSITIVE_X.ordinal());
        if (y==sizeY-1 || !contains(x, y+1, z))
            result |= (1 << Direction.POSITIVE_Y.ordinal());
        if (z==sizeZ-1 || !contains(x, y, z+1))
            result |= (1 << Direction.POSITIVE_Z.ordinal());

        return result;
    }

    //CHECKERS

    /**
     * <p>
     *     Returns whether the array contains a voxel at the given position.
     * </p>
     * <p>
     *     This is the case unless the voxel array contains a completely transparent voxel <code>(alpha = 0) </code>
     *     at the coordinates.
     * </p>
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return whether the array contains a voxel
     */
    @Override
    public boolean contains(int x, int y, int z) {
        return ColorMath.isVisible(getRGB(x, y, z));
    }

    /**
     * <p>
     *     Returns whether the array contains a voxel at the given position.
     * </p>
     * <p>
     *     This is the case unless the voxel array contains a completely transparent voxel <code>(alpha = 0)</code>
     *     at the position.
     * </p>
     *
     * @param pos the position
     * @return whether the array contains a voxel
     */
    @Override
    public boolean contains(Vertex3i pos) {
        return contains(pos.getX(), pos.getY(), pos.getZ());
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof VoxelArray && equals((VoxelArray) obj);
    }
    
    /**
     * Returns whether this array is equal to another array. This condition is met of the arrays are equal in size and
     * equal in content.
     *
     * @param array the array
     * @return whether the arrays are equal
     */
    public boolean equals(VoxelArray array) {
        return
            getSizeX() == array.getSizeX() &&
            getSizeY() == array.getSizeY() &&
            getSizeZ() == array.getSizeZ() &&
            Arrays.equals(this.voxels, array.voxels);
        
    }

    //SETTERS

    /**
     * Sets the voxel color at a given position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @param rgb the voxel color
     */
    public void setRGB(int x, int y, int z, int rgb) {
        voxels[indexOf(x, y, z)] = rgb;
    }

    /**
     * Sets the voxel color at a given position.
     *
     * @param pos the position
     * @param rgb the voxel color
     */
    public void setRGB(Vertex3i pos, int rgb) {
        setRGB(pos.getX(), pos.getY(), pos.getZ(), rgb);
    }

    public void remove(int x, int y, int z) {
        setRGB(x, y, z, ColorMath.INVISIBLE_WHITE);
    }
    
    /**
     * Fills this voxel array with another array at a given offset.
     *
     * @param array the array to paste this one with
     */
    public void paste(VoxelArray array, int x, int y, int z) {
        for (Voxel v : array)
            this.setRGB(v.getX()+x, v.getY()+y, v.getZ()+z, v.getRGB());
    }
    
    /**
     * Fills the entire voxel array with a single rgb value.
     *
     * @param rgb the rgb value
     */
    public void fill(int rgb) {
        Arrays.fill(voxels, rgb);
    }
    
    /**
     * Clears the voxel array.
     */
    public void clear() {
        this.voxels = new int[length];
    }

    //MISC

    @Override
    public String toString() {
        return VoxelArray.class.getSimpleName()+
            "{dims="+getSizeX()+"x"+getSizeY()+"x"+getSizeZ()+
            ", volume="+getVolume()+
            ", size="+size()+"}";
    }

    @Override
    public VoxelArray clone() {
        return new VoxelArray(this);
    }
    
    // ITERATION

    @Override
    public void forEach(Consumer<? super Voxel> action) {
        for (int x = 0; x < sizeX; x++) for (int y = 0; y < sizeY; y++) for (int z = 0; z < sizeZ; z++) {
            Voxel v = getVoxel(x, y, z);
            if (v.isVisible())
                action.accept(v);
        }
    }

    public void forEachPosition(Consumer<? super Vertex3i> action) {
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
                for (int z = 0; z < sizeZ; z++)
                    action.accept(new Vertex3i(x, y, z));
    }
    
    public void forEachPosition(Int3Consumer action) {
        for (int x = 0; x < sizeX; x++)
            for (int y = 0; y < sizeY; y++)
                for (int z = 0; z < sizeZ; z++)
                    action.accept(x, y, z);
    }

    /**
     * Equivalent to {@link #validatingIterator()}.
     *
     * @return a new iterator
     */
    @NotNull
    @Override
    public Iterator<Voxel> iterator() {
        return new ValidatingVoxelIterator();
    }

    /**
     * Returns an iterator that skips invisible (invalid) voxels.
     *
     * @return a new validating voxel iterator
     */
    public ValidatingVoxelIterator validatingIterator() {
        return new ValidatingVoxelIterator();
    }

    /**
     * Returns an iterator that does not skip any voxels, whether they are visible or not.
     *
     * @return a new voxel iterator
     */
    public VoxelIterator voxelIterator() {
        return new VoxelIterator();
    }

    public class VoxelIterator implements Iterator<Voxel> {

        private final Incrementer3 i = new Incrementer3(getSizeX(), getSizeY(), getSizeZ());

        @Override
        public boolean hasNext() {
            return i.canIncrement();
        }

        @Override
        public Voxel next() {
            int[] result = i.getAndIncrement();
            return new Voxel(result[0], result[1], result[2]);
        }

        @Override
        public void remove() {
            int[] current = i.get();
            VoxelArray.this.remove(current[0], current[1], current[2]);
        }

    }

    public class ValidatingVoxelIterator implements Iterator<Voxel> {

        private final int
                max = getVolume()-1,
                divX = getSizeX(),
                divY = getSizeY(),
                divZ = divX * divY;
        private int index = -1;
        
        private Voxel current;

        private ValidatingVoxelIterator() {
            skipToValid();
        }
        
        @Override
        public Voxel next() {
            current = peek();
            skip();
            skipToValid();
            return current;
        }

        private void skipToValid() {
            while (hasNext()) {
                if (peek().isVisible()) break;
                else skip();
            }
        }

        @Override
        public boolean hasNext() {
            return index < max;
        }

        private void skip() throws NoSuchElementException {
            if (++index > max) throw new NoSuchElementException();
        }
    
        @Override
        public void remove() {
            current.remove();
        }
    
        private Voxel peek() throws NoSuchElementException {
            int
            next = index + 1,
            x = next%divX, y = next/divX%divY, z = next/divZ;

            return new Voxel(x, y, z);
        }

    }

    /**
     * <p>
     *     A temporary-use class representing one position inside a {@link VoxelArray}.
     * </p>
     * <p>
     *     Permanently storing a {@link Voxel} should be strictly avoided as this will result in a reference to its
     *     underlying array.
     * </p>
     * <p>
     *     Ideally, this object should be disposed of at the end of iteration over the array.
     * </p>
     *
     */
    public class Voxel implements RGBValue {

        private final int x, y, z, index;

        private Voxel(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.index = indexOf(x, y, z);
        }

        private Voxel(Voxel copyOf) {
            this(copyOf.x, copyOf.y, copyOf.z);
        }

        private Voxel(Vertex3i pos) {
            this(pos.getX(), pos.getY(), pos.getZ());
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }

        /**
         * Returns the position of the voxel in its voxel array as a block vector.
         *
         * @return the position of the voxel
         */
        public Vertex3i getPosition() {
            return new Vertex3i(x, y, z);
        }

        @Override
        public int getRGB() {
            return VoxelArray.this.voxels[index];
        }

        /**
         * <p>
         *     Changes the RGB value of the voxel. This change is also applied to the underlying array in which the
         *     voxel is placed.
         * </p>
         * <p>
         *     Assigning an RGB value with an alpha value of 0 is equivalent to removing the voxel from its array, as
         *     voxels can not be invisible.
         * </p>
         *
         * @param rgb the rgb value to be assigned to the voxel
         * @see Voxel#remove(int, int, int)
         */
        public void setRGB(int rgb) {
            VoxelArray.this.voxels[index] = rgb;
        }

        /**
         * <p>
         *     Sets the RGB value of the voxel to {@link ColorMath#INVISIBLE_WHITE}. Although this color is technically
         *     white, its alpha channel is exactly 0. Using the white color it is possible to distinguish between
         *     unassigned voxels ({@link ColorMath#INVISIBLE_BLACK}) and deleted voxels.
         * </p>
         * <p>
         *     Assigning an RGB value with an alpha value of 0 is equivalent to removing the voxel from its array, as
         *     voxels can not be invisible.
         * </p>
         */
        public void remove() {
            setRGB(ColorMath.INVISIBLE_WHITE);
        }

        /**
         * <p>
         *     Returns whether a face of the voxel is visible.
         * </p>
         * <p>
         *     A face is always visible if there is no voxel covering that face. This also applies if the voxel face can
         *     not be covered by another voxel due to the array containing it ending at the face.
         * </p>
         *
         * @param side the side
         * @return whether the face of the voxel is visible
         */
        public boolean isVisible(Direction side) {
            switch (side) {
                case NEGATIVE_X: return x==0 || !VoxelArray.this.contains(x-1, y, z);
                case NEGATIVE_Y: return y==0 || !VoxelArray.this.contains(x, y-1, z);
                case NEGATIVE_Z: return z==0 || !VoxelArray.this.contains(x, y, z-1);
                case POSITIVE_X: return x==sizeX-1 || !VoxelArray.this.contains(x+1, y, z);
                case POSITIVE_Y: return y==sizeY-1 || !VoxelArray.this.contains(x, y+1, z);
                case POSITIVE_Z: return z==sizeZ-1 || !VoxelArray.this.contains(x, y, z+1);
                default: throw new IllegalArgumentException("unknown direction: "+side);
            }
        }

        /**
         * <p>
         *    Returns a bit field representing the visibility of each side of the voxel.
         * </p>
         * <p>
         *     A face is always visible if there is no voxel covering that face. This also applies if the voxel face can
         *     not be covered by another voxel due to the array containing it ending at the face.
         * </p>
         * <p>
         *     The ordinal of the {@link Direction} represents the index of the bit which can 0 (covered) or 1 (visible).
         *     Checking which side is visible can be done with the formulas:
         *     <ul>
         *         <li><code>value >> ordinal & 1 == 1</code></li>
         *         <li><code>value & 1 << ordinal != 0</code></li>
         *     </ul>
         * </p>
         * <p>
         *     If the returned byte is exactly 0, the voxel is covered from every side, if it is {@code 0b00111111}
         *     the voxel is visible from every side.
         * </p>
         *
         * @return a bitmap representing which faces are visible
         */
        public byte getVisibilityMask() {
            return VoxelArray.this.getVisibilityMask(x, y, z);
        }

        @Override
        public String toString() {
            return Voxel.class.getSimpleName()+
                    "{x=" + x +
                    ",y=" + y +
                    ",z=" + z +
                    ",rgb=" + Integer.toHexString(getRGB()).toUpperCase() + "}";
        }

    }

}
