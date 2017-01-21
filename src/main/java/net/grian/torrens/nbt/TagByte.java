package net.grian.torrens.nbt;

/**
 * The {@code TAG_Byte} tag.
 */
public final class TagByte extends NBTTagNumeric {

    private final byte value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
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
