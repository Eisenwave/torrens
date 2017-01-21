package net.grian.torrens.nbt;

import java.util.*;

/**
 * The {@code TAG_List} tag.
 */
public final class TagList<E extends NBTTag> extends NBTTag implements Iterable<E> {

    private final NBTType type;
    private final List<E> value;

    /**
     * Creates the tag with an empty name.
     *
     * @param type the type of tag
     * @param value the value of the tag
     */
    public TagList(NBTType type, List<? extends E> value) {
        Objects.requireNonNull(type);
        this.type = type;
        this.value = Collections.unmodifiableList(value);
    }

    @Override
    public List<E> getValue() {
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

    public int size() {
        return value.size();
    }

    /**
     * Returns a tag named with the given index.
     *
     * @param index the index
     * @return a byte
     * @throws NoSuchElementException if there is no tag with given index
     */
    public E get(int index) {
        return value.get(index);
    }

    @Override
    public Iterator<E> iterator() {
        return value.iterator();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder
                .append(type.getName())
                .append(": ")
                .append(value.size())
                .append(" entries of type ")
                .append(type.getName())
                .append("\r\n{\r\n");
        
        for (NBTTag t : value)
            builder
                    .append("   ")
                    .append(t.toString().replaceAll("\r\n", "\r\n   "))
                    .append("\r\n");

        return builder.append("}").toString();
    }

}
