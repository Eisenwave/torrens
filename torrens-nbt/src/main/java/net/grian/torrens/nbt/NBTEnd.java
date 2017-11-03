package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * The {@code TAG_End} tag.
 */
public final class NBTEnd extends NBTTag implements Cloneable {
    
    public final static NBTEnd INSTANCE = new NBTEnd();
    
    private NBTEnd() {}

    @NotNull
    @Override
    public Void getValue() {
        return null;
    }

    @NotNull
    @Override
    public NBTType getType() {
        return NBTType.END;
    }
    
    // MISC

    @Override
    public String toMSONString() {
        return "END";
    }
    
    @Override
    public NBTEnd clone() {
        return new NBTEnd();
    }
    
}
