package net.grian.torrens.nbt;

import java.util.Objects;

/**
 * A named tag consisting of a {@link String} for its name and a {@link NBTTag} for its value.
 */
public class NamedTag {

    private final String name;
    private final NBTTag tag;

    /**
     * Constructs a new named tag.
     *
     * @param name the name
     * @param tag the tag
     */
    public NamedTag(String name, NBTTag tag) {
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
    public NBTTag getTag() {
        return tag;
    }

}
