package net.grian.torrens.nbt;

import java.util.Arrays;

/**
 * The {@code TAG_Byte_Array} tag.
 */
public final class TagByteArray extends NBTTag {

    private final byte[] value;
    
    public TagByteArray(byte... value) {
        super();
        this.value = value;
    }
    
    /**
     * Returns the length of this array.
     *
     * @return the length of this array
     */
    public int length() {
        return value.length;
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
