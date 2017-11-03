package net.grian.torrens.nbt;

import net.grian.torrens.io.Deserializer;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class DeserializerNBT implements Deserializer<NBTNamedTag[]> {
    
    private final boolean compressed;
    
    /**
     * Constructs a new NBT-Deserializer.
     *
     * @param compressed whether the input is g-zip compressed
     */
    public DeserializerNBT(boolean compressed) {
        this.compressed = compressed;
    }
    
    /**
     * Constructs a new NBT-Deserializer with enabled g-zip decompression.
     */
    public DeserializerNBT() {
        this(true);
    }
    
    @NotNull
    @Override
    public NBTNamedTag[] fromStream(InputStream stream) throws IOException {
        NBTInputStream nbtStream = compressed?
            new NBTInputStream(new GZIPInputStream(stream)) :
            new NBTInputStream(stream);
        
        return fromStream(nbtStream);
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
