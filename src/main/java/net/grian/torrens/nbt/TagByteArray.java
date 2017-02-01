package net.grian.torrens.nbt;

import java.util.Arrays;

/**
 * The {@code TAG_Byte_Array} tag.
 */
public final class TagByteArray extends NBTTag {

    private final byte[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagByteArray(byte... value) {
        super();
        this.value = value;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE_ARRAY;
    }

    @Override
    public String toString() {
        return getType()+"("+ Arrays.toString(getValue())+")";
    }

}
