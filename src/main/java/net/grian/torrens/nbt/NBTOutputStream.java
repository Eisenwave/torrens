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
 * This class writes <strong>NBT</strong>, or <strong>Named Binary Tag</strong>
 * {@code Tag} objects to an underlying {@code OutputStream}.
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
    private final DataOutputStream os;

    /**
     * Creates a new {@code NBTOutputStream}, which will write data to the
     * specified underlying output stream.
     * 
     * @param os the output stream
     * @throws IOException if an I/O error occurs
     */
    public NBTOutputStream(OutputStream os) throws IOException {
        this.os = new DataOutputStream(os);
    }

    /**
     * Writes a tag.
     * 
     * @param tag the tag to write
     * @throws IOException if an I/O error occurs
     */
    public void writeNamedTag(String name, Tag tag) throws IOException {
        Objects.requireNonNull(name);
        Objects.requireNonNull(tag);

        int typeId = tag.getType().getId();
        byte[] nameBytes = name.getBytes(UTF_8);

        os.writeByte(typeId);
        os.writeShort(nameBytes.length);
        os.write(nameBytes);

        if (typeId == TagType.END.getId())
            throw new IOException("Named TAG_End not permitted.");

        writeTagPayload(tag);
    }

    /**
     * Writes a tag payload.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeTagPayload(Tag tag) throws IOException {
        switch (tag.getType()) {
            case END: writeEndTagPayload((TagEnd) tag); break;
            case BYTE: writeByteTagPayload((TagByte) tag); break;
            case SHORT: writeShortTagPayload((TagShort) tag); break;
            case INT: writeIntTagPayload((TagInt) tag); break;
            case LONG: writeLongTagPayload((TagLong) tag); break;
            case FLOAT: writeFloatTagPayload((TagFloat) tag); break;
            case DOUBLE: writeDoubleTagPayload((TagDouble) tag); break;
            case BYTE_ARRAY: writeByteArrayTagPayload((TagByteArray) tag); break;
            case STRING: writeStringTagPayload((TagString) tag); break;
            case LIST: writeListTagPayload((TagList) tag); break;
            case COMPOUND: writeCompoundTagPayload((TagCompound) tag); break;
            case INT_ARRAY: writeIntArrayTagPayload((TagIntArray) tag); break;
            default: throw new IOException("invalid tag type: " + tag.getType());
        }
    }

    /**
     * Writes a {@code TAG_End} tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("UnusedParameters")
    private void writeEndTagPayload(TagEnd tag) throws IOException {
        /* empty */
    }

    /**
     * Writes a {@code TAG_Byte} tag.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeByteTagPayload(TagByte tag) throws IOException {
        os.writeByte(tag.getValue());
    }

    /**
     * Writes a {@code TAG_Byte_Array} tag.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeByteArrayTagPayload(TagByteArray tag) throws IOException {
        byte[] bytes = tag.getValue();
        os.writeInt(bytes.length);
        os.write(bytes);
    }

    /**
     * Writes a {@code TAG_Compound} tag.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeCompoundTagPayload(TagCompound tag) throws IOException {
        for (Map.Entry<String, Tag> entry : tag.getValue().entrySet()) {
            writeNamedTag(entry.getKey(), entry.getValue());
        }
        os.writeByte((byte) 0); // end tag - better way?
    }

    /**
     * Writes a {@code TAG_List} tag.
     * 
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    private void writeListTagPayload(TagList tag) throws IOException {
        TagType type = tag.getElementType();
        List<Tag> tags = tag.getValue();
        int size = tags.size();

        os.writeByte(type.getId());
        os.writeInt(size);
        for (Tag tag1 : tags) {
            writeTagPayload(tag1);
        }
    }

    /**
     * Writes a {@code TAG_String} tag.
     * 
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    private void writeStringTagPayload(TagString tag) throws IOException {
        byte[] bytes = tag.getValue().getBytes(UTF_8);
        os.writeShort(bytes.length);
        os.write(bytes);
    }

    /**
     * Writes a {@code TAG_Double} tag.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeDoubleTagPayload(TagDouble tag) throws IOException {
        os.writeDouble(tag.getValue());
    }

    /**
     * Writes a {@code TAG_Float} tag.
     * 
     * @param tag The tag
     * @throws IOException if an I/O error occurs
     */
    private void writeFloatTagPayload(TagFloat tag) throws IOException {
        os.writeFloat(tag.getValue());
    }

    /**
     * Writes a {@code TAG_Long} tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeLongTagPayload(TagLong tag) throws IOException {
        os.writeLong(tag.getValue());
    }

    /**
     * Writes a {@code TAG_Int} tag.
     * 
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    private void writeIntTagPayload(TagInt tag) throws IOException {
        os.writeInt(tag.getValue());
    }

    /**
     * Writes a {@code TAG_Short} tag.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeShortTagPayload(TagShort tag) throws IOException {
        os.writeShort(tag.getValue());
    }

    /**
     * Writes a {@code TAG_Int_Array} tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeIntArrayTagPayload(TagIntArray tag) throws IOException {
        int[] data = tag.getValue();
        os.writeInt(data.length);
        for (int aData : data) {
            os.writeInt(aData);
        } 
    }

    @Override
    public void close() throws IOException {
        os.close();
    }

}
