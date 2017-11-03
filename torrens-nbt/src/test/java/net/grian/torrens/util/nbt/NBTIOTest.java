package net.grian.torrens.nbt;

import net.grian.torrens.io.SerializerByteArray;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class NBTIOTest {
    
    private final static File DEBUG_FILE = new File("/home/user/debug.nbt");
    
    /**
     * Tests whether NBT data can be written and read.
     *
     * @throws IOException if the test fails
     */
    @Test
    public void writeNamedTag() throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        NBTOutputStream nbtOut = new NBTOutputStream(byteOut);
        
        nbtOut.writeNamedTag("byte", new TagByte((byte) 8));
        nbtOut.writeNamedTag("short", new TagShort((short) 16));
        nbtOut.writeNamedTag("int", new TagInt(2));
        nbtOut.writeNamedTag("long", new TagLong(64L));
        nbtOut.writeNamedTag("float", new TagFloat(32F));
        nbtOut.writeNamedTag("double", new TagDouble(64D));
        nbtOut.writeNamedTag("bytes", new TagByteArray((byte) 1, (byte) 2, (byte) 3));
        nbtOut.writeNamedTag("ints", new TagIntArray(1, 2, 3, 4, 5, 6));
        nbtOut.writeNamedTag("list", new TagList(NBTType.INT, new TagInt(1), new TagInt(2), new TagInt(3)));
        nbtOut.writeNamedTag("compound", new TagCompound(
            new NBTNamedTag("a", new TagInt(1)),
            new NBTNamedTag("b", new TagFloat(2))
        ));
        
        byte[] bytes = byteOut.toByteArray();
        byteOut.close();
    
        //System.out.println(DEBUG_FILE.getAbsolutePath());
        if (DEBUG_FILE.canWrite()) {
            System.out.println("writing to debug file");
            new SerializerByteArray().toFile(bytes, DEBUG_FILE);
        }
        
        ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
        NBTInputStream nbtIn = new NBTInputStream(byteIn);
    
        NBTNamedTag tag;
        while ((tag = nbtIn.readNamedTag()) != null) {
            assertNotNull(tag.getTag().getValue());
        }
    }
    
}