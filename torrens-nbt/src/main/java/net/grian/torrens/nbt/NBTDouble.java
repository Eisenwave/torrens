package net.grian.torrens.nbt;

/**
 * The {@code TAG_Double} tag.
 * 
 */
public final class TagDouble extends NBTTag {

    private final double value;
    
    public TagDouble(double value) {
        super();
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    public double getDoubleValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.DOUBLE;
    }

}