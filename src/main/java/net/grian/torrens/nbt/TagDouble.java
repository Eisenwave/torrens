package net.grian.torrens.nbt;

/**
 * The {@code TAG_Double} tag.
 * 
 */
public final class TagDouble extends NBTTag {

    private final double value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagDouble(double value) {
        super();
        this.value = value;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.DOUBLE;
    }

}
