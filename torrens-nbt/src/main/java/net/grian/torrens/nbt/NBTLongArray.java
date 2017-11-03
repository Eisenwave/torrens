package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The {@code TAG_Long_Array} tag.
 */
public final class NBTLongArray extends NBTTag {

    private final long[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public NBTLongArray(@NotNull long... value) {
        this.value = Objects.requireNonNull(value);
    }
    
    public NBTLongArray(Number... numbers) {
        this.value = new long[numbers.length];
        for (int i = 0; i < numbers.length; i++)
            value[i] = numbers[i].longValue();
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
    public long[] getValue() {
        return value;
    }
    
    @NotNull
    @Override
    public NBTType getType() {
        return NBTType.LONG_ARRAY;
    }
    
    @Override
    public String toMSONString() {
        StringBuilder stringbuilder = new StringBuilder("[I;");
        for (int i = 0; i < this.value.length; i++) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.value[i]);
        }
        return stringbuilder.append(']').toString();
    }

}
