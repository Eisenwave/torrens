package net.grian.torrens.nbt;

/**
 * The {@code TAG_Long} tag.
 * 
 */
public final class TagLong extends Tag {

    private final long value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagLong(long value) {
        super();
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.LONG;
    }

}
