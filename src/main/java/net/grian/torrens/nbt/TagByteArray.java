package net.grian.torrens.nbt;

/**
 * The {@code TAG_Byte_Array} tag.
 */
public final class TagByteArray extends NBTTag {

    private final byte[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagByteArray(byte[] value) {
        super();
        this.value = value;
    }

    @Override
    public byte[] getValue() {
        return value;
    }

    @Override
    public NBTType getType() {
        return NBTType.BYTE_ARRAY;
    }

    @Override
    public String toString() {
        StringBuilder hex = new StringBuilder();
        for (byte b : value) {
            String hexDigits = Integer.toHexString(b).toUpperCase();
            if (hexDigits.length() == 1) {
                hex.append("0");
            }
            hex.append(hexDigits).append(" ");
        }
        return "TAG_Byte_Array(" + hex + ")";
    }

}
