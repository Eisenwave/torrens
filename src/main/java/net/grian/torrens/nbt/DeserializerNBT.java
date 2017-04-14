package net.grian.torrens.nbt;

import net.grian.torrens.io.Deserializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class DeserializerNBT implements Deserializer<NBTNamedTag[]> {
    
    private final boolean compress;
    
    public DeserializerNBT(boolean compress) {
        this.compress = compress;
    }
    
    public DeserializerNBT() {
        this(true);
    }
    
    @Override
    public NBTNamedTag[] fromStream(InputStream stream) throws IOException {
        try (NBTInputStream nbtStream = compress?
            new NBTInputStream(new GZIPInputStream(stream)) :
            new NBTInputStream(stream)) {
            
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
