package eisenwave.torrens.schematic;

import net.grian.spatium.function.Int3Consumer;
import eisenwave.torrens.object.BoundingBox6i;
import eisenwave.torrens.object.Vertex2i;
import eisenwave.torrens.object.Vertex3i;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * A 3-dimensional structure of <i>Minecraft</i> blocks.
 */
public interface BlockStructure extends Iterable<BlockKey> {
    
    // ABSTRACT
    
    /**
     * Returns the block id at the specified position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return the block at the specified position
     */
    abstract int getId(int x, int y, int z);
    
    /**
     * Returns the block data at the specified position.
     *
     * @param x the x position
     * @param y the y position
     * @param z the z position
     * @return the block data at the specified position
     */
    abstract byte getData(int x, int y, int z);
    
    /**
     * Sets the block id at the specified position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    abstract void setId(int x, int y, int z, int id);
    
    /**
     * Sets the block data at the specified position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    abstract void setData(int x, int y, int z, byte data);
    
    /**
     * Sets the block at the specified position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    abstract void setBlock(int x, int y, int z, int id, byte data);
    
    /**
     * Returns whether the array has a non-air block at the given position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return whether the array contains a block
     */
    default boolean hasBlock(int x, int y, int z) {
        return getId(x, y, z) > 0;
    }
    
    
    /**
     * Returns whether the array has a non-air block at the given position.
     *
     * @param pos the position
     * @return whether the array contains a block
     */
    default boolean hasBlock(Vertex3i pos) {
        return hasBlock(pos.getX(), pos.getY(), pos.getZ());
    }
    
    /**
     * Returns the size of the array on the x-axis.
     *
     * @return the size of the array on the x-axis
     */
    abstract int getSizeX();
    
    /**
     * Returns the size of the array on the y-axis.
     *
     * @return the size of the array on the y-axis
     */
    abstract int getSizeY();
    
    /**
     * Returns the size of the array on the z-axis.
     *
     * @return the size of the array on the z-axis
     */
    abstract int getSizeZ();
    
    // OPTIONAL
    
    /**
     * Returns whether this block structure stores biome ids.
     *
     * @return whether this block structure stores biome ids
     */
    default boolean hasBiomes() {
        return false;
    }
    
    /**
     * Returns whether this block structure stores block light.
     *
     * @return whether this block structure stores block light
     */
    default boolean hasLight() {
        return false;
    }
    
    /**
     * Returns the biome at the specified position.
     *
     * @param x the x-coordinate
     * @param z the z-coordinate
     * @return the biome at the specified position
     * @throws IllegalStateException if the array stores no biomes
     */
    default byte getBiome(int x, int z) {
        throw new UnsupportedOperationException("block structure stores no biomes");
    }
    
    /**
     * Returns the block light at the specified position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return the biome at the specified position
     * @throws IllegalStateException if the array stores no light
     */
    default byte getLight(int x, int y, int z) {
        throw new UnsupportedOperationException("block structure stores no block light");
    }
    
    /**
     * Sets the biome at the specified position.
     *
     * @param x the x-coordinate
     * @param z the z-coordinate
     * @param biomeId the biome id
     */
    default void setBiome(int x, int z, int biomeId) {
        throw new UnsupportedOperationException("block structure stores no biomes");
    }
    
    /**
     * Sets the block light at the specified position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @param level the light level
     */
    default void setBlockLight(int x, int y, int z, byte level) {
        throw new UnsupportedOperationException("block structure stores no block light");
    }
    
    // DEFAULT
    
    /**
     * Returns the amount of non-air blocks in this structure.
     *
     * @return the amount of blocks
     */
    default int getBlockCount() {
        int result = 0;
        for (BlockKey block : this)
            if (block.getId() > 0)
                result++;
        return result;
    }
    
    /**
     * Returns the total capacity in blocks of this structure.
     *
     * @return the total block capacity
     */
    default int getVolume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }
    
    /**
     * Returns the boundaries of this block structure.
     *
     * @return the boundaries of this structure
     */
    default BoundingBox6i getBoundaries() {
        return new BoundingBox6i(0, 0, 0, getSizeX(), getSizeY(), getSizeZ());
    }
    
    /**
     * Removes the block at the specified position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     */
    default void remove(int x, int y, int z) {
        setBlock(x, y, z, 0, (byte) 0);
    }
    
    /**
     * Removes the block at the specified position.
     *
     * @param pos the position
     */
    default void remove(Vertex3i pos) {
        remove(pos.getX(), pos.getY(), pos.getZ());
    }
    
    /**
     * Clears the block structure. This can, but does not have to remove biomes and block light.
     */
    default void clear() {
        forEachPos((Int3Consumer) this::remove);
    }
    
    /**
     * Returns the block id at the specified position.
     *
     * @param pos the position
     * @return the block id at the specified position
     */
    default int getId(Vertex3i pos) {
        return getId(pos.getX(), pos.getY(), pos.getZ());
    }
    
    /**
     * Returns the block data at the specified position.
     *
     * @param pos the position
     * @return the block data at the specified position
     */
    default byte getData(Vertex3i pos) {
        return getData(pos.getX(), pos.getY(), pos.getZ());
    }
    
    /**
     * Returns the block at the specified position,
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param z the z-coordinate
     * @return the block at the specified position
     */
    default BlockKey getBlock(int x, int y, int z) {
        return new BlockKey(getId(x, y, z), getData(x, y, z));
    }
    
    /**
     * Returns the block at the specified position.
     *
     * @param v the position
     * @return the block at the specified position
     */
    default BlockKey getBlock(Vertex3i v) {
        return getBlock(v.getX(), v.getY(), v.getZ());
    }
    
    /**
     * Sets the block id at the specified position.
     *
     * @param pos the position
     * @param id the id
     */
    default void setId(Vertex3i pos, int id) {
        setId(pos.getX(), pos.getY(), pos.getZ(), id);
    }
    
    /**
     * Sets the block data at the specified position.
     *
     * @param pos the position
     * @param data the data
     */
    default void setData(Vertex3i pos, byte data) {
        setData(pos.getX(), pos.getY(), pos.getZ(), data);
    }
    
    /**
     * Sets the block at the specified position.
     *
     * @param pos the position
     * @param id the id
     * @param data the data
     */
    default void setBlock(Vertex3i pos, int id, byte data) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), id, data);
    }
    
    /**
     * Sets the block at the specified position.
     *
     * @param pos the position
     * @param block the block
     */
    default void setBlock(Vertex3i pos, BlockKey block) {
        setBlock(pos.getX(), pos.getY(), pos.getZ(), block.getId(), block.getData());
    }
    
    /**
     * Sets the biome id at the specified position.
     *
     * @param pos the position
     * @param biomeId the biome id
     */
    default void setBiome(Vertex2i pos, int biomeId) {
        setBiome(pos.getX(), pos.getY(), biomeId);
    }
    
    /**
     * Sets the block light at the specified position.
     *
     * @param pos the position
     * @param level the light level
     */
    default void setBlockLight(Vertex3i pos, byte level) {
        setBlockLight(pos.getX(), pos.getY(), pos.getZ(), level);
    }
    
    // ITERATION
    
    /**
     * Performs an action for every position within this structure's boundaries.
     *
     * @param action the action to perform
     */
    default void forEachPos(Int3Consumer action) {
        final int limX = getSizeX(), limY = getSizeY(), limZ = getSizeZ();
        
        for (int x = 0; x < limX; x++)
            for (int y = 0; y < limY; y++)
                for (int z = 0; z < limZ; z++)
                    action.accept(x, y, z);
    }
    
    /**
     * Performs an action for every position within this structure's boundaries.
     *
     * @param action the action to perform
     */
    default void forEachPos(Consumer<Vertex3i> action) {
        forEachPos((x, y, z) -> action.accept(new Vertex3i(x, y, z)));
    }
    
    /**
     * Performs an action for every non-air block within this structure's boundaries.
     *
     * @param action the action to perform
     */
    @Override
    default void forEach(Consumer<? super BlockKey> action) {
        forEachPos((x, y, z) -> {
            if (hasBlock(x, y, z))
                action.accept(getBlock(x, y, z));
        });
    }
    
    @NotNull
    @Override
    default Iterator<BlockKey> iterator() {
        return new BlockStructureIterator(this);
    }
    
}
