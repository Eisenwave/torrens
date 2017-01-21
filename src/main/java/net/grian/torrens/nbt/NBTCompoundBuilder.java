package net.grian.torrens.nbt;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Helps create compound tags.
 */
public class NBTCompoundBuilder {

    /**
     * Create a new builder instance.
     *
     * @return a new builder
     */
    public static NBTCompoundBuilder create() {
        return new NBTCompoundBuilder();
    }

    private final Map<String, NBTTag> entries;

    /**
     * Create a new instance.
     */
    private NBTCompoundBuilder() {
        this.entries = new HashMap<>();
    }

    /**
     * Create a new instance and use the given map (which will be modified).
     *
     * @param value the value
     */
    private NBTCompoundBuilder(Map<String, NBTTag> value) {
        Objects.requireNonNull(value);
        this.entries = value;
    }

    /**
     * Put the given key and tag into the compound tag.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder put(String key, NBTTag value) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(value);
        entries.put(key, value);
        return this;
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagByteArray}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putByteArray(String key, byte[] value) {
        return put(key, new TagByteArray(value));
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagByte}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putByte(String key, byte value) {
        return put(key, new TagByte(value));
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagDouble}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putDouble(String key, double value) {
        return put(key, new TagDouble(value));
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagFloat}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putFloat(String key, float value) {
        return put(key, new TagFloat(value));
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagIntArray}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putIntArray(String key, int[] value) {
        return put(key, new TagIntArray(value));
    }

    /**
     * Put the given key and value into the compound tag as an {@code TagInt}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putInt(String key, int value) {
        return put(key, new TagInt(value));
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagLong}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putLong(String key, long value) {
        return put(key, new TagLong(value));
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagShort}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putShort(String key, short value) {
        return put(key, new TagShort(value));
    }

    /**
     * Put the given key and value into the compound tag as a
     * {@code TagString}.
     *
     * @param key they key
     * @param value the value
     * @return this object
     */
    public NBTCompoundBuilder putString(String key, String value) {
        return put(key, new TagString(value));
    }

    /**
     * Put all the entries from the given map into this map.
     *
     * @param value the map of tags
     * @return this object
     */
    public NBTCompoundBuilder putAll(Map<String, ? extends NBTTag> value) {
        Objects.requireNonNull(value);
        for (Map.Entry<String, ? extends NBTTag> entry : value.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Build an unnamed compound tag with this builder's entries.
     *
     * @return the new compound tag
     */
    public TagCompound build() {
        return new TagCompound(new HashMap<>(entries));
    }

}
