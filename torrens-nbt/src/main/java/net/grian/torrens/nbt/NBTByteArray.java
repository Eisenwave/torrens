package net.grian.torrens.nbt;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * The {@code TAG_Byte_Array} tag.
 */
public final class NBTByteArray extends NBTTag {

    private final byte[] value;
    
    public NBTByteArray(byte[] value) {
        this.value = value;
    }
    
    public NBTByteArray(Number[] numbers) {
        this.value = new byte[numbers.length];
        for (int i = 0; i < numbers.length; i++)
            value[i] = numbers[i].byteValue();
    }
    
    /**
     * Returns the length of this array.
     *
     * @return the length of this array
     */
    @Contract(pure = true)
    public int length() {
        return value.length;
    }

    @NotNull
    @Override
    public byte[] getValue() {
        return value;
    }

    @NotNull
    @Override
    public NBTType getType() {
        return NBTType.BYTE_ARRAY;
    }

    @Override
    public String toMSONString() {
        StringBuilder stringbuilder = new StringBuilder("[B;");
        for (int i = 0; i < this.value.length; i++) {
            if (i != 0) {
                stringbuilder.append(',');
            }
            stringbuilder.append(this.value[i]).append('B');
        }
        return stringbuilder.append(']').toString();
    }

}
