package net.grian.torrens.nbt;

/**
 * The {@code TAG_Int} tag.
 */
public final class TagInt extends NBTTag {

    private final int value;
    
    public TagInt(int value) {
        super();
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public int getIntValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.INT;
    }
    
}