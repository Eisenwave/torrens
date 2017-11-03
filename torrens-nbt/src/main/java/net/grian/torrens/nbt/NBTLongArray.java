package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

/**
 * The {@code TAG_Int_Array} tag.
 */
public final class NBTIntArray extends NBTTag {

    private final int[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public NBTIntArray(int... value) {
        super();
        this.value = Objects.requireNonNull(value);
    }
    
    /**
     * Returns the length of this array.
     *
     * @return the length of this array
     */
    public int length() {
        return value.length;
    }
    
    @NotNull
    @Override
    public int[] getValue() {
        return value;
    }
    
    @NotNull
    @Override
    public NBTType getType() {
        return NBTType.INT_ARRAY;
    }
    
    @Override
    public String toString() {
        return getType()+"("+ Arrays.toString(getValue())+")";
    }

}
