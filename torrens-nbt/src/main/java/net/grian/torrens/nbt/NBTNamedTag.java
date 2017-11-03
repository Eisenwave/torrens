package net.grian.torrens.nbt;

import org.jetbrains.annotations.NotNull;

/**
 * A named tag consisting of a {@link String} for its name and a {@link NBTTag} for its value.
 */
public class NBTNamedTag {

    private final String name;
    private final NBTTag tag;

    /**
     * Constructs a new named tag.
     *
     * @param name the name
     * @param tag the tag
     */
    public NBTNamedTag(@NotNull String name, @NotNull NBTTag tag) {
        this.name = name;
        this.tag = tag;
    }

    /**
     * Returns the name of the tag.
     *
     * @return the name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Returns the tag.
     *
     * @return the tag
     */
    @NotNull
    public NBTTag getTag() {
        return tag;
    }

}
