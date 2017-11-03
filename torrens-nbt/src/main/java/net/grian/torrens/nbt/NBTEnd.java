package net.grian.torrens.nbt;

/**
 * The {@code TAG_End} tag.
 */
public final class TagEnd extends NBTTag {
    
    public final static TagEnd INSTANCE = new TagEnd();
    
    private TagEnd() {}

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public NBTType getType() {
        return NBTType.END;
    }

    @Override
    public String toString() {
        return getType().getName();
    }

}