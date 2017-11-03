package net.grian.torrens.nbt;

import java.util.Objects;

/**
 * The {@code TAG_String} tag.
 */
public final class TagString extends NBTTag {

    private final String value;
    
    public TagString(String value) {
        super();
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.STRING;
    }
    
    @Override
    public String toString() {
        return getType()+"(\""+getValue()+"\")";
    }
}
