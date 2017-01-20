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
 * This class reads <b>NBT</b>, or <b>Named Binary NBTTag</b>
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
     * Reads an NBT from the stream.
     * 
     * @param depth the depth of this tag
     * @return the tag that was read
     * @throws IOException if an I/O error occurs
     */
    private NamedTag readNamedTag(int depth) throws IOException {
        TagType type = TagType.fromId(is.readByte() & 0xFF);

        String name;
        if (type != TagType.END) {
            int nameLength = is.readShort() & 0xFFFF;
            byte[] nameBytes = new byte[nameLength];
            is.readFully(nameBytes);
            name = new String(nameBytes, UTF_8);
        }
        else
            name = "";

        return new NamedTag(name, readTagPayload(type, depth));
    }

    /**
     * Reads an NBT tag from the stream.
     *
     * @return the tag that was read
     * @throws IOException if an I/O error occurs
     */
    public NamedTag readNamedTag() throws IOException {
        return readNamedTag(0);
    }

    /**
     * Reads the payload of a tag given the type.
     * 
     * @param type the type
     * @param depth the depth
     * @return the tag
     * @throws IOException if an I/O error occurs.
     */
    private NBTTag readTagPayload(TagType type, int depth) throws IOException {
        switch (type) {

            case END:
                if (depth == 0)
                    throw new IOException("TAG_End found without a TAG_Compound/TAG_List tag preceding it.");
                else
                    return new TagEnd();

            case BYTE: return new TagByte(is.readByte());
            case SHORT: return new TagShort(is.readShort());
            case INT: return new TagInt(is.readInt());
            case LONG: return new TagLong(is.readLong());
            case FLOAT: return new TagFloat(is.readFloat());
            case DOUBLE: return new TagDouble(is.readDouble());

            case BYTE_ARRAY:
                int length = is.readInt();
                byte[] bytes = new byte[length];
                is.readFully(bytes);
                return new TagByteArray(bytes);

            case STRING:
                length = is.readShort();
                bytes = new byte[length];
                is.readFully(bytes);
                return new TagString(new String(bytes, UTF_8));

            case LIST:
                TagType elementType = TagType.fromId(is.readByte());
                length = is.readInt();

                List<NBTTag> tagList = new ArrayList<>();
                for (int i = 0; i < length; ++i) {
                    NBTTag tag = readTagPayload(elementType, depth + 1);
                    if (tag instanceof TagEnd) throw new IOException("TAG_End not permitted in a list.");
                    tagList.add(tag);
                }
                return new TagList(elementType, tagList);

            case COMPOUND:
                Map<String, NBTTag> tagMap = new HashMap<>();
                while (true) {
                    NamedTag namedTag = readNamedTag(depth + 1);
                    NBTTag tag = namedTag.getTag();
                    if (tag instanceof TagEnd) break;
                    else tagMap.put(namedTag.getName(), tag);
                }
                return new TagCompound(tagMap);

            case INT_ARRAY:
                length = is.readInt();
                int[] data = new int[length];
                for (int i = 0; i < length; i++)
                    data[i] = is.readInt();
                return new TagIntArray(data);

            default: throw new IOException("invalid tag type: " + type);
        }
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

}
