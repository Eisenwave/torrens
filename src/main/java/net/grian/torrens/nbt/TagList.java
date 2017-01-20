package net.grian.torrens.nbt;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * The {@code TAG_List} tag.
 */
@SuppressWarnings("Duplicates")
public final class TagList<E extends NBTTag> extends NBTTag {

    private final TagType type;
    private final List<E> value;

    /**
     * Creates the tag with an empty name.
     *
     * @param type the type of tag
     * @param value the value of the tag
     */
    public TagList(TagType type, List<? extends E> value) {
        Objects.requireNonNull(type);
        this.type = type;
        this.value = Collections.unmodifiableList(value);
    }

    /**
     * Gets the type of item in this list.
     *
     * @return The type of item in this list.
     */
    public TagType getElementType() {
        return type;
    }

    @Override
    public List<E> getValue() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.LIST;
    }

    /**
     * Create a new list tag with this tag's name and type.
     *
     * @param list the new list
     * @return a new list tag
     */
    public TagList setValue(List<E> list) {
        return new TagList<>(getElementType(), list);
    }

    /**
     * Get the tag if it exists at the given index.
     * 
     * @param index the index
     * @return the tag or null
     */
    public NBTTag getIfExists(int index) {
        try {
            return value.get(index);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Get a byte array named with the given index.
     *
     * <p>If the index does not exist or its value is not a byte array tag,
     * then an empty byte array will be returned.</p>
     *
     * @param index the index
     * @return a byte array
     */
    public byte[] getByteArray(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagByteArray) {
            return ((TagByteArray) tag).getValue();
        } else {
            return new byte[0];
        }
    }

    /**
     * Get a byte named with the given index.
     *
     * <p>If the index does not exist or its value is not a byte tag,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return a byte
     */
    public byte getByte(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagByte) {
            return ((TagByte) tag).getValue();
        } else {
            return (byte) 0;
        }
    }

    /**
     * Get a double named with the given index.
     *
     * <p>If the index does not exist or its value is not a double tag,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return a double
     */
    public double getDouble(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagDouble) {
            return ((TagDouble) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get a double named with the given index, even if it's another
     * type of number.
     *
     * <p>If the index does not exist or its value is not a number,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return a double
     */
    public double asDouble(int index) {
        NBTTag tag = getIfExists(index);
        return tag!=null && tag.getType().isNumeric()?
                ((Number) tag.getValue()).doubleValue() :
                0;
    }

    /**
     * Get a float named with the given index.
     *
     * <p>If the index does not exist or its value is not a float tag,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return a float
     */
    public float getFloat(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagFloat) {
            return ((TagFloat) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get a {@code int[]} named with the given index.
     *
     * <p>If the index does not exist or its value is not an int array tag,
     * then an empty array will be returned.</p>
     *
     * @param index the index
     * @return an int array
     */
    public int[] getIntArray(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagIntArray) {
            return ((TagIntArray) tag).getValue();
        } else {
            return new int[0];
        }
    }

    /**
     * Get an int named with the given index.
     *
     * <p>If the index does not exist or its value is not an int tag,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return an int
     */
    public int getInt(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagInt) {
            return ((TagInt) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get an int named with the given index, even if it's another
     * type of number.
     *
     * <p>If the index does not exist or its value is not a number,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return an int
     */
    public int asInt(int index) {
        NBTTag tag = getIfExists(index);
        return tag!=null && tag.getType().isNumeric()?
                ((Number) tag.getValue()).intValue() :
                0;
    }

    /**
     * Get a list of tags named with the given index.
     *
     * <p>If the index does not exist or its value is not a list tag,
     * then an empty list will be returned.</p>
     *
     * @param index the index
     * @return a list of tags
     */
    @SuppressWarnings("unchecked")
    public List<NBTTag> getList(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagList) {
            return ((TagList) tag).getValue();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Get a {@code TagList} named with the given index.
     *
     * <p>If the index does not exist or its value is not a list tag,
     * then an empty tag list will be returned.</p>
     *
     * @param index the index
     * @return a tag list instance
     */
    public TagList<?> getListTag(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagList) {
            return (TagList) tag;
        } else {
            return new TagList<>(TagType.STRING, Collections.emptyList());
        }
    }

    /**
     * Get a list of tags named with the given index.
     *
     * <p>If the index does not exist or its value is not a list tag,
     * then an empty list will be returned. If the given index references
     * a list but the list of of a different type, then an empty
     * list will also be returned.</p>
     *
     * @param index the index
     * @param elementType the type of the list elements
     * @return a list of tags
     */
    @SuppressWarnings({"unchecked", "Duplicates"})
    public List<NBTTag> getList(int index, TagType elementType) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagList) {
            TagList listTag = (TagList) tag;
            return listTag.getElementType().equals(elementType)?
                    listTag.getValue() :
                    Collections.emptyList();
        }
        else return Collections.emptyList();
    }

    /**
     * Get a long named with the given index.
     *
     * <p>If the index does not exist or its value is not a long tag,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return a long
     */
    public long getLong(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagLong) {
            return ((TagLong) tag).getValue();
        } else {
            return 0L;
        }
    }

    /**
     * Get a long named with the given index, even if it's another
     * type of number.
     *
     * <p>If the index does not exist or its value is not a number,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return a long
     */
    public long asLong(int index) {
        NBTTag tag = getIfExists(index);
        return tag!=null && tag.getType().isNumeric()?
                ((Number) tag.getValue()).longValue() :
                0;
    }

    /**
     * Get a short named with the given index.
     *
     * <p>If the index does not exist or its value is not a short tag,
     * then {@code 0} will be returned.</p>
     *
     * @param index the index
     * @return a short
     */
    public short getShort(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagShort) {
            return ((TagShort) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get a string named with the given index.
     *
     * <p>If the index does not exist or its value is not a string tag,
     * then {@code ""} will be returned.</p>
     *
     * @param index the index
     * @return a string
     */
    public String getString(int index) {
        NBTTag tag = getIfExists(index);
        if (tag instanceof TagString) {
            return ((TagString) tag).getValue();
        } else {
            return "";
        }
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
        for (NBTTag t : value) {
            builder
                    .append("   ")
                    .append(t.toString().replaceAll("\r\n", "\r\n   "))
                    .append("\r\n");
        }

        return builder.append("}").toString();
    }

}
