package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a NBT tag.
 */
public abstract class NBTTag {

    /**
     * Gets the value of this tag.
     * 
     * @return the value of this tag
     */
    @NotNull
    public abstract Object getValue();

    /**
     * Returns the type of this tag.
     *
     * @return the type of this tag
     */
    @NotNull
    public abstract NBTType getType();
    
    /**
     * Convenience method for getting the id of this tag's type.
     *
     * @return the type id
     */
    public byte getTypeId() {
        return getType().getId();
    }
    
    /**
     * Returns a Mojangson string representing this NBT tag.
     *
     * @return a Mojangson string representing this NBT tag
     */
    public abstract String toMSONString();
    
    @Override
    public String toString() {
        return toMSONString();
    }
    
}
