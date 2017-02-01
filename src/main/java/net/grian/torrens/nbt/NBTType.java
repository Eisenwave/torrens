package net.grian.torrens.nbt;

/**
 * The type of an NBT-NBTTag.
 */
public enum NBTType {
    /** Used to mark the end of compounds tags. May also be the type of empty list tags. */
    END("TAG_End", false),

    /** A signed integer (8 bits). Sometimes used for booleans. (-128 to 127) */
    BYTE("TAG_Byte", true),

    /** A signed integer (16 bits). (-2<sup>15</sup> to 2<sup>15</sup>-1) */
    SHORT("TAG_Short", true),

    /** A signed integer (32 bits). (-2<sup>31</sup> to 2<sup>31</sup>-1) */
    INT("TAG_Int", true),

    /** A signed integer (64 bits). (-2<sup>63</sup> to 2<sup>63</sup>-1) */
    LONG("TAG_Long", true),

    /** A signed (IEEE 754-2008) floating point number (32 bits).  */
    FLOAT("TAG_Float", true),

    /** A signed (IEEE 754-2008) floating point number (64 bits).  */
    DOUBLE("TAG_Double", true),

    /** An array of bytes with max payload size of maximum value of {@link #INT}. */
    BYTE_ARRAY("TAG_Byte_Array", false),

    /** UTF-8 encoded string. */
    STRING("TAG_String", false),

    /** A list of unnamed tags of equal type. */
    LIST("TAG_List", false),

    /** Compound of named tags followed by {@link #END}. */
    COMPOUND("TAG_Compound", false),

    /** An array of {@link #INT} with max payload size of maximum value of {@link #INT}. */
    INT_ARRAY("TAG_Int_Array", false);

    final String name;
    final boolean numeric;

    NBTType(String name, boolean numeric) {
        this.name = name;
        this.numeric = numeric;
    }

    public static NBTType fromId(int id) {
        return values()[id];
    }

    /**
     * <p>
     *     Returns the id of this tag type.
     * </p>
     * <p>
     *     Although this value is currently identical to {@link #ordinal()}, it should be used in preference to it to
     *     account for changes in the id system.
     * </p>
     *
     * @return the id of this tag type
     */
    public int getId() {
        return ordinal();
    }

    public String getName() {
        return name;
    }

    /**
     * <p>
     *     Returns whether this tag type is numeric.
     * </p>
     * <p>
     *     All tag types with payloads that are representable as a {@link Number} are compliant with this definition.
     * </p>
     *
     * @return whether this type is numeric
     */
    public boolean isNumeric() {
        return numeric;
    }
    
    @Override
    public String toString() {
        return getName();
    }
    
}
