package net.grian.torrens.nbt;

import java.util.*;

/**
 * Helps create list tags.
 */
public class ListTagBuilder {

    private final TagType type;
    private final List<Tag> entries;

    /**
     * Create a new instance.
     *
     * @param type of tag contained in this list
     */
    private ListTagBuilder(TagType type) {
        Objects.requireNonNull(type);
        this.type = type;
        this.entries = new ArrayList<>();
    }

    /**
     * Add the given tag.
     *
     * @param value the tag
     * @return this object
     */
    public ListTagBuilder add(Tag value) {
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
    public ListTagBuilder addAll(Collection<? extends Tag> value) {
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

    /**
     * Create a new builder instance.
     *
     * @return a new builder
     */
    public static ListTagBuilder create(TagType type) {
        return new ListTagBuilder(type);
    }

    /**
     * Create a new builder instance.
     *
     * @return a new builder
     */
    @SafeVarargs
    public static <T extends Tag> ListTagBuilder createWith(T ... entries) {
        Objects.requireNonNull(entries);

        if (entries.length == 0) {
            throw new IllegalArgumentException("This method needs an array of at least one entry");
        }

        TagType type = entries[0].getType();
        for (int i = 1; i < entries.length; i++) {
            if (type != entries[i].getType()) {
                throw new IllegalArgumentException("An array of different tag types was provided");
            }
        }

        ListTagBuilder builder = new ListTagBuilder(type);
        builder.addAll(Arrays.asList(entries));
        return builder;
    }

}
