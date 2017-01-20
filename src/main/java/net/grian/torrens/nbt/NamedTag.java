package net.grian.torrens.nbt;

import java.util.Objects;

/**
 * A named tag consisting of a {@link String} for its name and a {@link Tag} for its value.
 */
public class NamedTag {

    private final String name;
    private final Tag tag;

    /**
     * Constructs a new named tag.
     *
     * @param name the name
     * @param tag the tag
     */
    public NamedTag(String name, Tag tag) {
        Objects.requireNonNull(name);
        Objects.requireNonNull(tag);
        this.name = name;
        this.tag = tag;
    }

    /**
     * Get the name of the tag.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the tag.
     *
     * @return the tag
     */
    public Tag getTag() {
        return tag;
    }

}
