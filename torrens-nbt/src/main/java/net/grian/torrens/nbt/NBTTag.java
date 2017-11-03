package net.grian.torrens.util.nbt;

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
    public abstract Object getValue();

    /**
     * Returns the type of this tag.
     *
     * @return the type of this tag
     */
    @NotNull
    public abstract NBTType getType();

    @Override
    public String toString() {
        return getType()+"("+getValue()+")";
    }
}
