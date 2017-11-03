package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * The {@code TAG_Byte} tag.
 */
public final class NBTByte extends NBTTag implements Cloneable {

    private byte value;
    
    public NBTByte(byte value) {
        this.value = value;
    }

    @NotNull
    @Override
    public Byte getValue() {
        return value;
    }

    public byte getByteValue() {
        return value;
    }
    
    public void setShortValue(byte value) {
        this.value = value;
    }
    
    @NotNull
    @Override
    public NBTType getType() {
        return NBTType.BYTE;
    }
    
    // MISC
    
    @Override
    public String toMSONString() {
        return Byte.toUnsignedInt(value)+"b";
    }
    
    @Override
    public NBTByte clone() {
        return new NBTByte(value);
    }

}
