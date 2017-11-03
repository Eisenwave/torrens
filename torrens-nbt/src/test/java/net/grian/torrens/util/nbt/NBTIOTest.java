package net.grian.torrens.util.nbt;

import net.grian.torrens.io.SerializerByteArray;
import net.grian.torrens.nbt.*;
import net.grian.torrens.nbt.io.NBTInputStream;
import net.grian.torrens.nbt.io.NBTOutputStream;
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
        
        nbtOut.writeNamedTag("byte", new NBTByte((byte) 8));
        nbtOut.writeNamedTag("short", new NBTShort((short) 16));
        nbtOut.writeNamedTag("int", new NBTInt(2));
        nbtOut.writeNamedTag("long", new NBTLong(64L));
        nbtOut.writeNamedTag("float", new NBTFloat(32F));
        nbtOut.writeNamedTag("double", new NBTDouble(64D));
        nbtOut.writeNamedTag("bytes", new NBTByteArray((byte) 1, (byte) 2, (byte) 3));
        nbtOut.writeNamedTag("ints", new NBTIntArray(1, 2, 3, 4, 5, 6));
        nbtOut.writeNamedTag("list", new NBTList(NBTType.INT, new NBTInt(1), new NBTInt(2), new NBTInt(3)));
        nbtOut.writeNamedTag("compound", new NBTCompound(
            new NBTNamedTag("a", new NBTInt(1)),
            new NBTNamedTag("b", new NBTFloat(2))
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