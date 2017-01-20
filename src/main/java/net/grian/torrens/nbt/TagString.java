package net.grian.torrens.nbt;

import java.util.Objects;

/**
 * The {@code TAG_String} tag.
 */
public final class TagString extends Tag {

    private final String value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
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
    public TagType getType() {
        return TagType.STRING;
    }

}
