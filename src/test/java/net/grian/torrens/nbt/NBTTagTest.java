package net.grian.torrens.nbt;

import org.junit.Test;

import static org.junit.Assert.*;

public class NBTTagTest {
    
    @Test
    public void toStringFormatting() throws Exception {
        assertEquals("TAG_End", new TagEnd().toString());
        assertEquals("TAG_Byte(8)", new TagByte((byte) 8).toString());
        assertEquals("TAG_Short(16)", new TagShort((short) 16).toString());
        assertEquals("TAG_Int(32)", new TagInt(32).toString());
        assertEquals("TAG_Long(64)", new TagLong(64).toString());
        assertEquals("TAG_Float(32.0)", new TagFloat(32F).toString());
        assertEquals("TAG_Double(64.0)", new TagDouble(64D).toString());
        assertEquals("TAG_Byte_Array([1, 2, 3])", new TagByteArray((byte) 1, (byte) 2, (byte) 3).toString());
        assertEquals("TAG_String(\"str\")", new TagString("str").toString());
        assertEquals("TAG_Int_Array([1, 2, 3])", new TagIntArray(1, 2, 3).toString());
    }
    
}