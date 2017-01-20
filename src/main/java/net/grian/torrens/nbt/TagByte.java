package net.grian.torrens.nbt;

/**
 * The {@code TAG_Byte} tag.
 */
public final class TagByte extends Tag {

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

    @Override
    public TagType getType() {
        return TagType.BYTE;
    }

}
