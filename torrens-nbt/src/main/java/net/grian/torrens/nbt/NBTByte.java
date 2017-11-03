package net.grian.torrens.nbt;

/**
 * The {@code TAG_Byte} tag.
 */
public final class TagByte extends NBTTag {

    private final byte value;
    
    public TagByte(byte value) {
        super();
        this.value = value;
    }

    @Override
    public Byte getValue() {
        return value;
    }

    public byte getByteValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE;
    }

}
