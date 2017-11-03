package net.grian.torrens.nbt;

/**
 * The {@code TAG_Long} tag.
 * 
 */
public final class TagLong extends NBTTag {

    private final long value;
    
    public TagLong(long value) {
        super();
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    public long getLongValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.LONG;
    }

}
