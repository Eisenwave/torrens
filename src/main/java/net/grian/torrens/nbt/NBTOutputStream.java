package net.grian.torrens.nbt;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * An NBTInputStream extends {@link DataOutputStream} by allowing to write named tags.
 */
public final class NBTOutputStream extends DataOutputStream {

    private final static Charset UTF_8 = Charset.forName("UTF-8");

    /**
     * Creates a new {@code NBTOutputStream}, which will write data to the
     * specified underlying output stream.
     * 
     * @param out the output stream
     */
    public NBTOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Writes a tag.
     * 
     * @param tag the tag to write
     * @throws IOException if an I/O error occurs
     */
    public void writeNamedTag(String name, NBTTag tag) throws IOException {
        Objects.requireNonNull(tag);

        int typeId = tag.getType().getId();
        byte[] nameBytes = name.getBytes(UTF_8);

        writeByte(typeId);
        writeShort(nameBytes.length);
        write(nameBytes);

        if (typeId == NBTType.END.getId())
            throw new IOException("Named TAG_End not permitted.");

        writeTag(tag);
    }
    
    /**
     * Writes a tag.
     *
     * @param tag the tag to write
     * @throws IOException if an I/O error occurs
     */
    public void writeNamedTag(NBTNamedTag tag) throws IOException {
        writeNamedTag(tag.getName(), tag.getTag());
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

            case BYTE: writeByte(((TagByte) tag).getByteValue()); break;
            case SHORT: writeShort(((TagShort) tag).getShortValue()); break;
            case INT: writeInt(((TagInt) tag).getIntValue()); break;
            case LONG: writeLong(((TagLong) tag).getLongValue()); break;
            case FLOAT: writeFloat(((TagFloat) tag).getFloatValue()); break;
            case DOUBLE: writeDouble(((TagDouble) tag).getDoubleValue()); break;

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
        writeShort(bytes.length);
        write(bytes);
    }

    /**
     * Writes a {@code TAG_Byte_Array} tag.
     * 
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeTagByteArray(TagByteArray tag) throws IOException {
        byte[] bytes = tag.getValue();
        writeInt(bytes.length);
        write(bytes);
    }

    /**
     * Writes a {@code TAG_List} tag.
     * 
     * @param tag the tag.
     * @throws IOException if an I/O error occurs
     */
    private void writeTagList(TagList tag) throws IOException {
        NBTType type = tag.getElementType();
        List<? extends NBTTag> tags = tag.getValue();
        int size = tags.size();

        writeByte(type.getId());
        writeInt(size);
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
        writeByte(0); // end tag - better way?
    }

    /**
     * Writes a {@code TAG_Int_Array} tag.
     *
     * @param tag the tag
     * @throws IOException if an I/O error occurs
     */
    private void writeTagIntArray(TagIntArray tag) throws IOException {
        int[] data = tag.getValue();
        writeInt(data.length);
        for (int aData : data) {
            writeInt(aData);
        } 
    }
    
}
