package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * The {@code TAG_Double} tag.
 * 
 */
public final class NBTDouble extends NBTTag implements Cloneable {

    private double value;
    
    public NBTDouble(double value) {
        this.value = value;
    }

    @NotNull
    @Override
    public Double getValue() {
        return value;
    }

    public double getDoubleValue() {
        return value;
    }
    
    public void setDoubleValue(double value) {
        this.value = value;
    }
    
    @NotNull
    @Override
    public NBTType getType() {
        return NBTType.DOUBLE;
    }
    
    // MISC
    
    @Override
    public String toMSONString() {
        return value+"d";
    }
    
    @Override
    public NBTDouble clone() {
        return new NBTDouble(value);
    }

}