/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.grian.torrens.nbt;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class reads <strong>NBT</strong>, or <strong>Named Binary Tag</strong>
 * streams, and produces an object graph of subclasses of the {@code Tag}
 * object.
 * 
 * <p>The NBT format was created by Markus Persson, and the specification may be
 * found at <a href="http://www.minecraft.net/docs/NBT.txt">
 * http://www.minecraft.net/docs/NBT.txt</a>.</p>
 */
public final class NBTInputStream implements Closeable {

    private final DataInputStream is;

    /**
     * Creates a new {@code NBTInputStream}, which will source its data
     * from the specified input stream.
     * 
     * @param is the input stream
     * @throws IOException if an I/O error occurs
     */
    public NBTInputStream(InputStream is) throws IOException {
        this.is = new DataInputStream(is);
    }

    /**
     * Reads an NBT tag from the stream.
     * 
     * @return The tag that was read.
     * @throws IOException if an I/O error occurs.
     */
    public NamedTag readNamedTag() throws IOException {
        return readNamedTag(0);
    }

    /**
     * Reads an NBT from the stream.
     * 
     * @param depth the depth of this tag
     * @return The tag that was read.
     * @throws IOException if an I/O error occurs.
     */
    private NamedTag readNamedTag(int depth) throws IOException {
        TagType type = TagType.fromId(is.readByte() & 0xFF);

        String name;
        if (type != TagType.END) {
            int nameLength = is.readShort() & 0xFFFF;
            byte[] nameBytes = new byte[nameLength];
            is.readFully(nameBytes);
            name = new String(nameBytes, NBTUtils.CHARSET);
        } else {
            name = "";
        }

        return new NamedTag(name, readTagPayload(type, depth));
    }

    /**
     * Reads the payload of a tag given the type.
     * 
     * @param type the type
     * @param depth the depth
     * @return the tag
     * @throws IOException if an I/O error occurs.
     */
    private Tag readTagPayload(TagType type, int depth) throws IOException {
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
                return new TagString(new String(bytes, NBTUtils.CHARSET));

            case LIST:
                TagType elementType = TagType.fromId(is.readByte());
                length = is.readInt();

                List<Tag> tagList = new ArrayList<>();
                for (int i = 0; i < length; ++i) {
                    Tag tag = readTagPayload(elementType, depth + 1);
                    if (tag instanceof TagEnd) throw new IOException("TAG_End not permitted in a list.");
                    tagList.add(tag);
                }
                return new TagList(elementType, tagList);

            case COMPOUND:
                Map<String, Tag> tagMap = new HashMap<>();
                while (true) {
                    NamedTag namedTag = readNamedTag(depth + 1);
                    Tag tag = namedTag.getTag();
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

            default: throw new IOException("Invalid tag type: " + type + ".");
        }
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

}
