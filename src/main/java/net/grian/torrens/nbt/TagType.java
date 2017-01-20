package net.grian.torrens.nbt;

public enum TagType {
    END("TAG_End", false),
    BYTE("TAG_Byte", true),
    SHORT("TAG_Short", true),
    INT("TAG_Int", true),
    LONG("TAG_Long", true),
    FLOAT("TAG_Float", true),
    DOUBLE("TAG_Double", true),
    BYTE_ARRAY("TAG_Byte_Array", false),
    STRING("TAG_String", false),
    LIST("TAG_List", false),
    COMPOUND("TAG_Compound", false),
    INT_ARRAY("TAG_Int_Array", false);

    final String name;
    final boolean numeric;

    TagType(String name, boolean numeric) {
        this.name = name;
        this.numeric = numeric;
    }

    public static TagType fromId(int id) {
        return values()[id];
    }

    public int getId() {
        return ordinal();
    }

    public String getName() {
        return name;
    }

    public boolean isNumeric() {
        return numeric;
    }

    /*
    public static final int TYPE_END = 0, TYPE_BYTE = 1, TYPE_SHORT = 2,
            TYPE_INT = 3, TYPE_LONG = 4, TYPE_FLOAT = 5, TYPE_DOUBLE = 6,
            TYPE_BYTE_ARRAY = 7, TYPE_STRING = 8, TYPE_LIST = 9,
            TYPE_COMPOUND = 10, TYPE_INT_ARRAY = 11;
     */

}
