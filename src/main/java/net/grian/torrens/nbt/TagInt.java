package net.grian.torrens.nbt;

/**
 * The {@code TAG_Int} tag.
 */
public final class TagInt extends NBTTag {

    private final int value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagInt(int value) {
        super();
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.INT;
    }

}
