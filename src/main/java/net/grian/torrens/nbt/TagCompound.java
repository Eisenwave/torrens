package net.grian.torrens.nbt;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * The {@code TAG_Compound} tag.
 */
public final class TagCompound extends NBTTag {

    private final Map<String, NBTTag> value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagCompound(Map<String, NBTTag> value) {
        super();
        this.value = Collections.unmodifiableMap(value);
    }

    /**
     * Returns whether this compound tag contains the given key.
     *
     * @param key the given key
     * @return true if the tag contains the given key
     */
    public boolean containsKey(String key) {
        return value.containsKey(key);
    }

    @Override
    public Map<String, NBTTag> getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.COMPOUND;
    }

    /**
     * Return a new compound tag with the given values.
     *
     * @param value the value
     * @return the new compound tag
     */
    public TagCompound setValue(Map<String, NBTTag> value) {
        return new TagCompound(value);
    }

    /**
     * Get a byte array named with the given key.
     *
     * <p>If the key does not exist or its value is not a byte array tag,
     * then an empty byte array will be returned.</p>
     *
     * @param key the key
     * @return a byte array
     */
    public byte[] getByteArray(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagByteArray) {
            return ((TagByteArray) tag).getValue();
        } else {
            return new byte[0];
        }
    }

    /**
     * Get a byte named with the given key.
     *
     * <p>If the key does not exist or its value is not a byte tag,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return a byte
     */
    public byte getByte(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagByte) {
            return ((TagByte) tag).getValue();
        } else {
            return (byte) 0;
        }
    }

    /**
     * Get a double named with the given key.
     *
     * <p>If the key does not exist or its value is not a double tag,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return a double
     */
    public double getDouble(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagDouble) {
            return ((TagDouble) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get a double named with the given key, even if it's another
     * type of number.
     *
     * <p>If the key does not exist or its value is not a number,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return a double
     */
    public double asDouble(String key) {
        NBTTag tag = value.get(key);
        return tag.getType().isNumeric()?
                ((Number) tag.getValue()).doubleValue() :
                0;
    }

    /**
     * Get a float named with the given key.
     *
     * <p>If the key does not exist or its value is not a float tag,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return a float
     */
    public float getFloat(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagFloat) {
            return ((TagFloat) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get a {@code int[]} named with the given key.
     *
     * <p>If the key does not exist or its value is not an int array tag,
     * then an empty array will be returned.</p>
     *
     * @param key the key
     * @return an int array
     */
    public int[] getIntArray(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagIntArray) {
            return ((TagIntArray) tag).getValue();
        } else {
            return new int[0];
        }
    }

    /**
     * Get an int named with the given key.
     *
     * <p>If the key does not exist or its value is not an int tag,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return an int
     */
    public int getInt(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagInt) {
            return ((TagInt) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get an int named with the given key, even if it's another
     * type of number.
     *
     * <p>If the key does not exist or its value is not a number,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return an int
     */
    public int asInt(String key) {
        NBTTag tag = value.get(key);
        return tag.getType().isNumeric()?
                ((Number) tag.getValue()).intValue() :
                0;
    }

    /**
     * Get a list of tags named with the given key.
     *
     * <p>If the key does not exist or its value is not a list tag,
     * then an empty list will be returned.</p>
     *
     * @param key the key
     * @return a list of tags
     */
    public List<NBTTag> getList(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagList) {
            return ((TagList) tag).getValue();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Get a {@code TagList} named with the given key.
     *
     * <p>If the key does not exist or its value is not a list tag,
     * then an empty tag list will be returned.</p>
     *
     * @param key the key
     * @return a tag list instance
     */
    public TagList getListTag(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagList) {
            return (TagList) tag;
        } else {
            return new TagList(NBTType.STRING, Collections.emptyList());
        }
    }

    /**
     * Get a list of tags named with the given key.
     *
     * <p>If the key does not exist or its value is not a list tag,
     * then an empty list will be returned. If the given key references
     * a list but the list of of a different type, then an empty
     * list will also be returned.</p>
     *
     * @param key the key
     * @param elementType the type of the list elements
     * @return a list of tags
     * @param <T> the type of list
     */
    @SuppressWarnings("unchecked")
    public <T extends NBTTag> List<T> getList(String key, NBTType elementType) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagList) {
            TagList listTag = (TagList) tag;
            return listTag.getElementType().equals(elementType)?
                    (List<T>) listTag.getValue() :
                    Collections.emptyList();
        }
        else return Collections.emptyList();
    }

    /**
     * Get a long named with the given key.
     *
     * <p>If the key does not exist or its value is not a long tag,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return a long
     */
    public long getLong(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagLong) {
            return ((TagLong) tag).getValue();
        } else {
            return 0L;
        }
    }

    /**
     * Get a long named with the given key, even if it's another
     * type of number.
     *
     * <p>If the key does not exist or its value is not a number,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return a long
     */
    public long asLong(String key) {
        NBTTag tag = value.get(key);
        return tag.getType().isNumeric()?
                ((Number) tag.getValue()).longValue() :
                0;
    }

    /**
     * Get a short named with the given key.
     *
     * <p>If the key does not exist or its value is not a short tag,
     * then {@code 0} will be returned.</p>
     *
     * @param key the key
     * @return a short
     */
    public short getShort(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagShort) {
            return ((TagShort) tag).getValue();
        } else {
            return 0;
        }
    }

    /**
     * Get a string named with the given key.
     *
     * <p>If the key does not exist or its value is not a string tag,
     * then {@code ""} will be returned.</p>
     *
     * @param key the key
     * @return a string
     */
    public String getString(String key) {
        NBTTag tag = value.get(key);
        if (tag instanceof TagString) {
            return ((TagString) tag).getValue();
        } else {
            return "";
        }
    }

    @Override
    public String toString() {
        StringBuilder bldr = new StringBuilder();
        bldr.append(getType().getName()).append(": ").append(value.size()).append(" entries\r\n{\r\n");
        for (Map.Entry<String, NBTTag> entry : value.entrySet()) {
            bldr.append("   ").append(entry.getValue().toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        }
        bldr.append("}");
        return bldr.toString();
    }

}
