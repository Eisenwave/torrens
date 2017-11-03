package net.grian.torrens.util.nbt;

import net.grian.torrens.nbt.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class NBTTagTest {
    
    @Test
    public void toStringFormatting() throws Exception {
        assertEquals("TAG_End", NBTEnd.INSTANCE.toString());
        assertEquals("TAG_Byte(8)", new NBTByte((byte) 8).toString());
        assertEquals("TAG_Short(16)", new NBTShort((short) 16).toString());
        assertEquals("TAG_Int(32)", new NBTInt(32).toString());
        assertEquals("TAG_Long(64)", new NBTLong(64).toString());
        assertEquals("TAG_Float(32.0)", new NBTFloat(32F).toString());
        assertEquals("TAG_Double(64.0)", new NBTDouble(64D).toString());
        assertEquals("TAG_Byte_Array([1, 2, 3])", new NBTByteArray((byte) 1, (byte) 2, (byte) 3).toString());
        assertEquals("TAG_String(\"str\")", new NBTString("str").toString());
        assertEquals("TAG_Int_Array([1, 2, 3])", new NBTIntArray(1, 2, 3).toString());
    }
    
}