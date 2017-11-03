package net.grian.torrens.nbt;

import java.util.*;

/**
 * The {@code TAG_List} tag.
 */
public final class TagList extends NBTTag implements Iterable<NBTTag> {

    public static NBTListBuilder builder(NBTType type) {
        return new NBTListBuilder(type);
    }
    
    private final NBTType type;
    private final List<NBTTag> value;

    /**
     * Creates the tag with an empty name.
     *
     * @param type the type of tag
     * @param value the value of the tag
     */
    public TagList(NBTType type, List<? extends NBTTag> value) {
        this.type = Objects.requireNonNull(type);
        this.value = Collections.unmodifiableList(value);
    }
    
    /**
     * Creates the tag with an empty name.
     *
     * @param type the type of tag
     * @param value the value of the tag
     */
    public TagList(NBTType type, NBTTag... value) {
        this(type, Arrays.asList(value));
    }
    
    // GETTERS
    
    /**
     * Returns the size of this list.
     *
     * @return the size of this list
     */
    public int size() {
        return value.size();
    }
    
    /**
     * Returns whether this list is empty.
     *
     * @return whether this list is empty
     */
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public List<NBTTag> getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.LIST;
    }

    /**
     * Gets the type of item in this list.
     *
     * @return The type of item in this list.
     */
    public NBTType getElementType() {
        return type;
    }

    /**
     * Returns a tag named with the given index.
     *
     * @param index the index
     * @return a byte
     * @throws NoSuchElementException if there is no tag with given index
     */
    public NBTTag get(int index) {
        return value.get(index);
    }

    // MISC
    
    @Override
    public Iterator<NBTTag> iterator() {
        return value.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getType().toString());
        builder.append("[");
        
        Iterator<NBTTag> iter = iterator();
        boolean hasNext = iter.hasNext();
        while (hasNext) {
            builder.append(iter.next());
            if (hasNext = iter.hasNext())
                builder.append(", ");
        }

        return builder.append("]").toString();
    }

}
