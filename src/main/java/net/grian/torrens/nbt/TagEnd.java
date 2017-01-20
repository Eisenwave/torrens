package net.grian.torrens.nbt;

/**
 * The {@code TAG_End} tag.
 */
public final class TagEnd extends NBTTag {

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public TagType getType() {
        return TagType.END;
    }

    @Override
    public String toString() {
        return "TAG_End";
    }

}
