package net.grian.torrens.nbt;

import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This class writes <strong>NBT</strong>, or <strong>Named Binary NBTTag</strong>
 * {@code NBTTag} objects to an underlying {@code OutputStream}.
 * 
 * <p>The NBT format was created by Markus Persson, and the specification may be
 * found at <a href="http://www.minecraft.net/docs/NBT.txt">
 * http://www.minecraft.net/docs/NBT.txt</a>.</p>
 */
public final class NBTOutputStream implements Closeable {

    private final static Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * The output stream.
     */
    private final DataOutputStream stream;

    /**
     * Creates a new {@code NBTOutputStream}, which will write data to the
     * specified underlying output stream.
     * 
     * @param stream the output stream
     */
    public NBTOutputStream(OutputStream stream) {
        this.stream = new DataOutputStream(stream);
    }

    /**
     * Writes a tag.
     * 
     * @param tag the tag to write
     * @throws IOException if an I/O error occurs
     */
    public void writeNamedTag(String name, NBTTag tag) throws IOException {
        Objects.requireNonNull(name);
        Objects.requireNonNull(tag);

        int typeId = tag.getType().getId();
        byte[] nameBytes = name.getBytes(UTF_8);

        stream.writeByte(typeId);
        stream.writeShort(nameBytes.length);
        stream.write(nameBytes);

        if (typeId == NBTType.END.getId())
            throw new IOException("Named TAG_End not permitted.");

        writeTag(tag);
    }

    /**
     * Writes a tag payload.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeTag(NBTTag tag) throws IOException {
        switch (tag.getType()) {
            case END: break;

            case BYTE: stream.writeByte(((TagByte) tag).getByteValue()); break;
            case SHORT: stream.writeShort(((TagShort) tag).getShortValue()); break;
            case INT: stream.writeInt(((TagInt) tag).getIntValue()); break;
            case LONG: stream.writeLong(((TagLong) tag).getLongValue()); break;
            case FLOAT: stream.writeFloat(((TagFloat) tag).getFloatValue()); break;
            case DOUBLE: stream.writeDouble(((TagDouble) tag).getDoubleValue()); break;

            case BYTE_ARRAY: writeTagByteArray((TagByteArray) tag); break;
            case STRING: writeTagString((TagString) tag); break;
            case LIST: writeTagList((TagList) tag); break;
            case COMPOUND: writeTagCompound((TagCompound) tag); break;
            case INT_ARRAY: writeTagIntArray((TagIntArray) tag); break;

            default: throw new IOException("invalid tag type: " + tag.getType());
        }
    }

    /**
     * Writes a {@code TAG_String} tag.
     *
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    private void writeTagString(TagString tag) throws IOException {
        byte[] bytes = tag.getValue().getBytes(UTF_8);
        stream.writeShort(bytes.length);
        stream.write(bytes);
    }

    /**
     * Writes a {@code TAG_Byte_Array} tag.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeTagByteArray(TagByteArray tag) throws IOException {
        byte[] bytes = tag.getValue();
        stream.writeInt(bytes.length);
        stream.write(bytes);
    }

    /**
     * Writes a {@code TAG_List} tag.
     * 
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    private void writeTagList(TagList<?> tag) throws IOException {
        NBTType type = tag.getElementType();
        List<? extends NBTTag> tags = tag.getValue();
        int size = tags.size();

        stream.writeByte(type.getId());
        stream.writeInt(size);
        for (NBTTag element : tags)
            writeTag(element);
    }

    /**
     * Writes a {@code TAG_Compound} tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeTagCompound(TagCompound tag) throws IOException {
        for (Map.Entry<String, NBTTag> entry : tag.getValue().entrySet()) {
            writeNamedTag(entry.getKey(), entry.getValue());
        }
        stream.writeByte((byte) 0); // end tag - better way?
    }

    /**
     * Writes a {@code TAG_Int_Array} tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeTagIntArray(TagIntArray tag) throws IOException {
        int[] data = tag.getValue();
        stream.writeInt(data.length);
        for (int aData : data) {
            stream.writeInt(aData);
        } 
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }

}
