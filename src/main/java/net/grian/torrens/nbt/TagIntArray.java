package net.grian.torrens.nbt;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@code TAG_Int_Array} tag.
 */
public final class TagIntArray extends NBTTag {

    private final int[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagIntArray(int... value) {
        super();
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public int[] getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.INT_ARRAY;
    }
    
    @Override
    public String toString() {
        return getType()+"("+ Arrays.toString(getValue())+")";
    }

}
