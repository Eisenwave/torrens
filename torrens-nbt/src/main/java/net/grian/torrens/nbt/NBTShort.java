package net.grian.torrens.nbt;

/**
 * The {@code TAG_Short} tag.
 */
public final class TagShort extends NBTTag {

    private final short value;
    
    public TagShort(short value) {
        super();
        this.value = value;
    }

    @Override
    public Short getValue() {
        return value;
    }

    public short getShortValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.SHORT;
    }

}
