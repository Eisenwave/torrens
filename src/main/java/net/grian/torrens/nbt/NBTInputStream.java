package net.grian.torrens.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class reads <b>NBT</b>, or <b>Named Binary Tag</b>
 * streams, and produces an object graph of subclasses of the {@code NBTTag}
 * object.
 * 
 * <p>The NBT format was created by Markus Persson, and the specification may be
 * found at <a href="http://www.minecraft.net/docs/NBT.txt">
 * http://www.minecraft.net/docs/NBT.txt</a>.</p>
 */
public final class NBTInputStream implements Closeable {

    private final static Charset UTF_8 = Charset.forName("UTF-8");

    private final DataInputStream is;

    /**
     * Creates a new {@code NBTInputStream}, which will source its data from the specified input stream.
     * 
     * @param is the input stream
     * @throws IOException if an I/O error occurs
     */
    public NBTInputStream(InputStream is) throws IOException {
        this.is = new DataInputStream(is);
    }

    /**
     * <p>
     *     Reads a tag and its name from the stream.
     * </p>
     * <p>
     *     Should the tag be of type {@link NBTType#COMPOUND} or {@link NBTType#LIST} will the full content (all
     *     elements in the compounds or list) be read.
     * </p>
     *
     * @return the tag that was read
     * @throws IOException if an I/O error occurs
     */
    public NBTNamedTag readNamedTag() throws IOException {
        return readNamedTag(0);
    }

    private NBTNamedTag readNamedTag(int depth) throws IOException {
        NBTType type = NBTType.fromId(is.readByte() & 0xFF);

        String name = type!= NBTType.END? readUTF() : "";

        return new NBTNamedTag(name, readTag(type, depth));
    }

    /**
     * <p>
     *     Reads the payload of a tag given the type.
     * </p>
     * <p>
     *     This method accepts a depth parameter which is necessary for recursive reading of compounds and lists.
     * </p>
     * <p>
     *     The depth parameter indicates what depth the currently called function has, starting with 0 if this method
     *     is being called initially.
     * </p>
     * <p>
     *     Should this method be called while reading a compound or list, the depth will be 1. Should these compounds
     *     or lists contain further compounds and lists will the depth be 2 (and so on).
     * </p>
     * 
     * @param type the type
     * @param depth the depth (used for recursive reading of lists or compounds)
     * @return the tag
     * @throws IOException if an I/O error occurs.
     */
    private NBTTag readTag(NBTType type, int depth) throws IOException {
        switch (type) {
            case END: return readTagEnd(depth);
            case BYTE: return new TagByte(is.readByte());
            case SHORT: return new TagShort(is.readShort());
            case INT: return new TagInt(is.readInt());
            case LONG: return new TagLong(is.readLong());
            case FLOAT: return new TagFloat(is.readFloat());
            case DOUBLE: return new TagDouble(is.readDouble());
            case BYTE_ARRAY: return readTagByteArray();
            case STRING: return readTagString();
            case LIST: return readTagList(depth);
            case COMPOUND: return readTagCompound(depth);
            case INT_ARRAY: return readTagIntArray();
            default: throw new IOException("invalid tag type: " + type);
        }
    }

    private TagEnd readTagEnd(int depth) throws IOException {
        if (depth == 0)
            throw new IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
        return new TagEnd();
    }

    private TagByteArray readTagByteArray() throws IOException {
        int length = is.readInt();
        byte[] bytes = new byte[length];
        is.readFully(bytes);
        return new TagByteArray(bytes);
    }

    private TagString readTagString() throws IOException {
        return new TagString(readUTF());
    }

    private TagList<?> readTagList(int depth) throws IOException {
        NBTType elementType = NBTType.fromId(is.readByte());
        final int length = is.readInt();

        List<NBTTag> tagList = new ArrayList<>();
        for (int i = 0; i < length; ++i) {
            NBTTag tag = readTag(elementType, depth + 1);
            if (tag instanceof TagEnd)
                throw new IOException("TAG_End not permitted in a list.");
            tagList.add(tag);
        }

        return new TagList<>(elementType, tagList);
    }

    private TagCompound readTagCompound(int depth) throws IOException {
        Map<String, NBTTag> tagMap = new HashMap<>();
        while (true) {
            NBTNamedTag namedTag = readNamedTag(depth + 1);
            NBTTag tag = namedTag.getTag();
            if (tag instanceof TagEnd) break;
            else tagMap.put(namedTag.getName(), tag);
        }

        return new TagCompound(tagMap);
    }

    private TagIntArray readTagIntArray() throws IOException {
        final int length = is.readInt();
        int[] data = new int[length];
        for (int i = 0; i < length; i++)
            data[i] = is.readInt();

        return new TagIntArray(data);
    }

    private String readUTF() throws IOException {
        int length = is.readShort();
        byte[] bytes = new byte[length];
        is.readFully(bytes);

        return new String(bytes, UTF_8);
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

}
