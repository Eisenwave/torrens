package net.grian.torrens.nbt;

import java.util.*;

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
        this.value = new LinkedHashMap<>(value);
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
     * Returns whether this compound tag contains the given key.
     *
     * @param key the given key
     * @return true if the tag contains the given key
     */
    public boolean containsKey(String key) {
        return value.containsKey(key);
    }

    /**
     * Returns whether this compound tag contains the given key and its value is of a given type.
     *
     * @param key the given key
     * @param type the type of the value
     * @return true if the tag contains an entry with given key and of given type
     */
    public boolean containsKey(String key, NBTType type) {
        Objects.requireNonNull(type);
        return value.containsKey(key) && value.get(key).getType() == type;
    }

    /**
     * Returns a tag named with the given key.
     *
     * @param key the key
     * @return a byte
     * @throws NoSuchElementException if there is no tag with given name
     */
    public NBTTag getTag(String key) {
        if (!containsKey(key)) throw new NoSuchElementException(key);
        return value.get(key);
    }

    /**
     * Returns a byte named with the given key.
     *
     * @param key the key
     * @return a byte
     * @throws NoSuchElementException if there is no byte with given name
     */
    public byte getByte(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagByte)) throw new NoSuchElementException(key);
        return ((TagByte) tag).getValue();
    }

    /**
     * Returns an short named with the given key.
     *
     * @param key the key
     * @return an short
     * @throws NoSuchElementException if there is no short with given name
     */
    public short getShort(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagShort)) throw new NoSuchElementException(key);
        return ((TagShort) tag).getValue();
    }

    /**
     * Returns an int named with the given key.
     *
     * @param key the key
     * @return an int
     * @throws NoSuchElementException if there is no int with given name
     */
    public int getInt(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagInt)) throw new NoSuchElementException(key);
        return ((TagInt) tag).getValue();
    }

    /**
     * Returns an long named with the given key.
     *
     * @param key the key
     * @return an long
     * @throws NoSuchElementException if there is no long with given name
     */
    public long getLong(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagLong)) throw new NoSuchElementException(key);
        return ((TagLong) tag).getValue();
    }

    /**
     * Returns float named with the given key.
     *
     * @param key the key
     * @return a float
     * @throws NoSuchElementException if there is no float with given name
     */
    public float getFloat(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagFloat)) throw new NoSuchElementException(key);
        return ((TagFloat) tag).getValue();
    }

    /**
     * Returns a double named with the given key.
     *
     * @param key the key
     * @return a double
     * @throws NoSuchElementException if there is no int with given name
     */
    public double getDouble(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagDouble)) throw new NoSuchElementException(key);
        return ((TagDouble) tag).getValue();
    }

    /**
     * Returns a byte array named with the given key.
     *
     * @param key the key
     * @return a byte array
     * @throws NoSuchElementException if there is no int with given name
     */
    public byte[] getByteArray(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagByteArray)) throw new NoSuchElementException(key);
        return ((TagByteArray) tag).getValue();
    }

    /**
     * Returns a string named with the given key.
     *
     * @param key the key
     * @return a string
     * @throws NoSuchElementException if there is no int with given name
     */
    public String getString(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagString)) throw new NoSuchElementException(key);
        return ((TagString) tag).getValue();
    }

    /**
     * Returns a list named with the given key.
     *
     * @param key the key
     * @return a list
     * @throws NoSuchElementException if there is no int with given name
     */
    @SuppressWarnings("unchecked")
    public List<NBTTag> getList(String key) {
        return getTagList(key).getValue();
    }

    /**
     * Returns a list named with the given key.
     *
     * @param key the key
     * @return a list
     * @throws NoSuchElementException if there is no list with given name
     */
    public TagList getTagList(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagList)) throw new NoSuchElementException(key);
        return (TagList) tag;
    }

    /**
     * Returns a list named with the given key.
     *
     * @param key the key
     * @return a list
     * @throws NoSuchElementException if there is no compound with given name
     */
    public Map<String, NBTTag> getCompound(String key) {
        return getTagCompound(key).getValue();
    }

    /**
     * Returns a compound named with the given key.
     *
     * @param key the key
     * @return a compound
     * @throws NoSuchElementException if there is no compound with given name
     */
    public TagCompound getTagCompound(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagCompound)) throw new NoSuchElementException(key);
        return (TagCompound) tag;
    }

    /**
     * Returns an int array named with the given key.
     *
     * @param key the key
     * @return a int array
     * @throws NoSuchElementException if there is no int array with given name
     */
    public int[] getIntArray(String key) {
        NBTTag tag = value.get(key);
        if (!(tag instanceof TagIntArray)) throw new NoSuchElementException(key);
        return ((TagIntArray) tag).getValue();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getType().toString());
        builder.append("{");

        Iterator<Map.Entry<String, NBTTag>> iter = value.entrySet().iterator();
        boolean hasNext = iter.hasNext();
        while (hasNext) {
            Map.Entry<String, NBTTag> next = iter.next();
            builder
                .append('\"')
                .append(next.getKey())
                .append("\": ")
                .append(next.getValue());
            if (hasNext = iter.hasNext())
                builder.append(", ");
        }

        builder.append("}");
        return builder.toString();
    }

}
