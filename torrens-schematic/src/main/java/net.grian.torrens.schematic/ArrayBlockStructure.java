package net.grian.torrens.schematic;

import net.grian.spatium.array.AbstractArray3;
import net.grian.spatium.array.LowNibbleArray;
import org.jetbrains.annotations.*;

import java.io.Serializable;
import java.util.Arrays;

public class ArrayBlockStructure extends AbstractArray3 implements BlockStructure, Serializable, Cloneable {
    
    /** store block biomes */
    public final static int
        FLAG_BIOMES = 1,
    /** store block light */
    FLAG_LIGHT = 1 << 1;
    
    private byte[] arrayId;
    private LowNibbleArray arrayData;
    
    @Nullable
    private byte[] arrayBiome;
    @Nullable
    private byte[] arrayLight;
    
    private final int flags;
    
    public ArrayBlockStructure(int x, int y, int z, int flags) {
        super(x, y, z);
        if (super.length == 0)
            throw new IllegalArgumentException("size 0 voxel array");
        
        
        this.flags = flags;
        this.arrayId = new byte[length];
        this.arrayData = new LowNibbleArray(length);
        this.arrayBiome = (flags & FLAG_BIOMES) != 0? new byte[x * z] : null;
        this.arrayLight = (flags & FLAG_LIGHT) != 0? new byte[length] : null;
    }
    
    public ArrayBlockStructure(int x, int y, int z) {
        this(x, y, z, 0);
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
    public ArrayBlockStructure copy(int xmin, int ymin, int zmin, int xmax, int ymax, int zmax) {
        if (xmin < 0 || ymin < 0 || zmin < 0)
            throw new IllegalArgumentException("min (" + xmin + "," + ymin + "," + zmin + ") out of boundaries");
        if (xmax >= getSizeX() || ymax >= getSizeY() || zmax >= getSizeZ())
            throw new IllegalArgumentException("max (" + xmax + "," + ymax + "," + zmax + ") out of boundaries");
        
        ArrayBlockStructure result = new ArrayBlockStructure(xmax - xmin + 1, ymax - ymin + 1, zmax - zmin + 1, getFlags());
        for (int x = xmin; x <= xmax; x++)
            for (int y = ymin; y <= ymax; y++)
                for (int z = zmin; z <= zmax; z++) {
                    final int x2 = x - xmin, y2 = y - ymin, z2 = z - zmin;
                    result.setBlock(x2, y2, z2, getId(x, y, z), getData(x, y, z));
                    if (hasBiomes()) result.setBiome(x2, z2, getBiome(x, z));
                    if (hasLight()) result.setBlockLight(x2, y2, z2, getLight(x, y, z));
                }
        
        return result;
    }
    
    protected int indexOf(int x, int z) {
        return z * sizeX + x;
    }
    
    /**
     * Returns all the extra content flags this array was created with.
     *
     * @return this array's extra flags
     */
    public int getFlags() {
        return flags;
    }
    
    @Override
    public int getId(int x, int y, int z) {
        return arrayId[indexOf(x, y, z)] & 0xFF;
    }
    
    @Override
    public byte getData(int x, int y, int z) {
        return arrayData.get(indexOf(x, y, z));
    }
    
    @Override
    public byte getBiome(int x, int z) {
        if (!hasBiomes()) throw new IllegalStateException("block array stores no biomes");
        assert arrayBiome != null;
        return arrayBiome[indexOf(x, z)];
    }
    
    @Override
    public byte getLight(int x, int y, int z) {
        if (!hasLight()) throw new IllegalStateException("block array stores no light");
        assert arrayLight != null;
        return arrayLight[indexOf(x, y, z)];
    }
    
    @Override
    public int getBlockCount() {
        int size = 0;
        for (byte id : arrayId)
            if (id != 0) size++;
        
        return size;
    }
    
    // PREDICATES
    
    /**
     * Returns whether this block array stores biomes.
     *
     * @return whether this block array stores biomes
     * @see #FLAG_BIOMES
     */
    @Override
    public boolean hasBiomes() {
        return arrayBiome != null;
    }
    
    /**
     * Returns whether this block array stores block light.
     *
     * @return whether this block array stores block light
     * @see #FLAG_LIGHT
     */
    @Override
    public boolean hasLight() {
        return arrayLight != null;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof ArrayBlockStructure && equals((ArrayBlockStructure) obj);
    }
    
    public boolean equals(ArrayBlockStructure array) {
        return
            this.getSizeX() == array.getSizeX() &&
                this.getSizeY() == array.getSizeY() &&
                this.getSizeZ() == array.getSizeZ() &&
                Arrays.equals(this.arrayId, array.arrayId) &&
                this.arrayData.equals(array.arrayData) &&
                Arrays.equals(this.arrayBiome, array.arrayBiome) &&
                Arrays.equals(this.arrayLight, array.arrayLight);
    }
    
    // MUTATORS
    
    @Override
    public void setId(int x, int y, int z, int id) {
        //System.out.println(x+" "+y+" "+z);
        arrayId[indexOf(x, y, z)] = (byte) id;
    }
    
    @Override
    public void setData(int x, int y, int z, byte data) {
        arrayData.set(indexOf(x, y, z), data);
    }
    
    @Override
    public void setBlock(int x, int y, int z, int id, byte data) {
        arrayId[indexOf(x, y, z)] = (byte) id;
        arrayData.set(indexOf(x, y, z), data);
    }
    
    @Override
    public void setBiome(int x, int z, int biomeId) {
        if (!hasBiomes()) throw new IllegalArgumentException("this block array has no biomes");
        assert arrayBiome != null;
        arrayBiome[indexOf(x, z)] = (byte) biomeId;
    }
    
    @Override
    public void setBlockLight(int x, int y, int z, byte level) {
        if (!hasLight()) throw new IllegalArgumentException("this block array has no block light");
        assert arrayLight != null;
        arrayLight[indexOf(x, y, z)] = level;
    }
    
    /**
     * Clears the array, removing all block information and biome/light information if the array has those channels.
     *
     * @see #hasBiomes()
     * @see #hasLight()
     */
    public void clear() {
        this.arrayId = new byte[length];
        this.arrayData = new LowNibbleArray(length);
        
        if (hasBiomes()) this.arrayBiome = new byte[length];
        if (hasLight()) this.arrayLight = new byte[length];
    }
    
    /**
     * Fills the array with a single type of block.
     *
     * @param id the block id
     * @param data the block data
     */
    public void fill(int id, byte data) {
        byte byteId = (byte) id;
        
        for (int i = 0; i < length; i++) {
            arrayId[i] = byteId;
            arrayData.set(i, data);
        }
    }
    
    //MISC
    
    @Override
    public String toString() {
        return ArrayBlockStructure.class.getSimpleName() +
            "{dims=" + getSizeX() + "x" + getSizeY() + "x" + getSizeZ() +
            ", volume=" + getVolume() +
            ", blocks=" + getBlockCount() + "," +
            ", biomes=" + hasBiomes() + "}";
    }
    
    @Override
    public ArrayBlockStructure clone() {
        return copy(0, 0, 0, getSizeX() - 1, getSizeY() - 1, getSizeZ() - 1);
    }
    
}
