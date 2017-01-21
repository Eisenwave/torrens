package net.grian.torrens.nbt;

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
    public abstract NBTType getType();

    @Override
    public String toString() {
        return getType().getName()+"("+getValue()+")";
    }
}
