package net.grian.torrens.nbt;

import java.util.Objects;

/**
 * The {@code TAG_Int_Array} tag.
 */
public final class TagIntArray extends Tag {

    private final int[] value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public TagIntArray(int[] value) {
        super();
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public int[] getValue() {
        return value;
    }

    @Override
    public TagType getType() {
        return TagType.INT_ARRAY;
    }

    @Override
    public String toString() {
        StringBuilder hex = new StringBuilder();
        for (int b : value) {
            String hexDigits = Integer.toHexString(b).toUpperCase();
            if (hexDigits.length() == 1)
                hex.append("0");
            hex.append(hexDigits).append(" ");
        }
        return "TAG_Int_Array(" + hex + ")";
    }

}
