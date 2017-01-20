package net.grian.torrens.nbt;

/**
 * The {@code TAG_Float} tag.
 */
public final class TagFloat extends Tag {

    private final float value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagFloat(float value) {
        super();
        this.value = value;
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.FLOAT;
    }

}
