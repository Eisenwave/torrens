package net.grian.torrens.nbt;

import net.grian.torrens.io.Deserializer;
import net.grian.torrens.nbt.NBTInputStream;
import net.grian.torrens.nbt.NBTNamedTag;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class DeserializerNBT implements Deserializer<NBTNamedTag[]> {
    
    @Override
    public NBTNamedTag[] fromStream(InputStream stream) throws IOException {
        try (NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream))) {
            return fromStream(nbtStream);
        }
    }
    
    public NBTNamedTag[] fromStream(NBTInputStream stream) throws IOException {
        List<NBTNamedTag> tags = new ArrayList<>();
        
        NBTNamedTag tag;
        while ((tag = stream.readNamedTag()) != null) {
            tags.add(tag);
        }
        
        return tags.toArray(new NBTNamedTag[tags.size()]);
    }
    
}
