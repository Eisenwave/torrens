package net.grian.torrens.nbt;

import java.util.*;

/**
 * Helps create list tags.
 */
public class NBTListBuilder {

    /**
     * Create a new builder instance.
     *
     * @return a new builder
     */
    @SafeVarargs
    public static <T extends NBTTag> NBTListBuilder createWith(T ... entries) {
        Objects.requireNonNull(entries);
        if (entries.length == 0)
            throw new IllegalArgumentException("entries must not be empty");

        NBTType type = entries[0].getType();
        for (int i = 1; i < entries.length; i++)
            if (type != entries[i].getType())
                throw new IllegalArgumentException("entries differ in type");

        return new NBTListBuilder(type).addAll(Arrays.asList(entries));
    }

    private final NBTType type;
    private final List<NBTTag> entries = new ArrayList<>();

    /**
     * Create a new instance.
     *
     * @param type of tag contained in this list
     */
    public NBTListBuilder(NBTType type) {
        this.type = Objects.requireNonNull(type);
    }

    /**
     * Add the given tag.
     *
     * @param value the tag
     * @return this object
     */
    public NBTListBuilder add(NBTTag value) {
        Objects.requireNonNull(value);
        if (this.type != value.getType())
            throw new IllegalArgumentException(value.getClass().getCanonicalName() + " is not of expected type " + type);
        entries.add(value);
        return this;
    }

    /**
     * Add all the tags in the given list.
     *
     * @param value a list of tags
     * @return this object
     */
    public NBTListBuilder addAll(Collection<? extends NBTTag> value) {
        Objects.requireNonNull(value);
        value.forEach(this::add);
        return this;
    }

    /**
     * Build an unnamed list tag with this builder's entries.
     *
     * @return the new list tag
     */
    public TagList build() {
        return new TagList(type, new ArrayList<>(entries));
    }

}
