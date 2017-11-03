package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * The {@code TAG_Int} tag.
 */
public final class NBTInt extends NBTTag implements Cloneable {

    private int value;
    
    public NBTInt(int value) {
        this.value = value;
    }
    
    @NotNull
    @Override
    public Integer getValue() {
        return value;
    }

    public int getIntValue() {
        return value;
    }
    
    public void setIntValue(int value) {
        this.value = value;
    }
    
    @NotNull
    @Override
    public NBTType getType() {
        return NBTType.INT;
    }
    
    // MISC
    
    @Override
    public String toMSONString() {
        return Integer.toString(value);
    }
    
    @Override
    public NBTInt clone() {
        return new NBTInt(value);
    }
    
}
