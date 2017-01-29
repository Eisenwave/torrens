package net.grian.torrens.io;

import net.grian.torrens.nbt.NBTNamedTag;
import net.grian.torrens.nbt.NBTOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class SerializerNBT implements Serializer<NBTNamedTag[]> {
    
    @Override
    public void toStream(NBTNamedTag[] tags, OutputStream stream) throws IOException {
        try (NBTOutputStream nbtStream = new NBTOutputStream(new GZIPOutputStream(stream))) {
            for (NBTNamedTag tag : tags)
                nbtStream.writeNamedTag(tag);
        }
    }
    
}
