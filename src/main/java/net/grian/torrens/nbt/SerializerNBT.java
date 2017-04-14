package net.grian.torrens.nbt;

import net.grian.torrens.io.Serializer;
import net.grian.torrens.nbt.NBTNamedTag;
import net.grian.torrens.nbt.NBTOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class SerializerNBT implements Serializer<NBTNamedTag[]> {
    
    private final boolean compress;
    
    public SerializerNBT(boolean compress) {
        this.compress = compress;
    }
    
    public SerializerNBT() {
        this(true);
    }
    
    @Override
    public void toStream(NBTNamedTag[] tags, OutputStream stream) throws IOException {
        try (NBTOutputStream nbtStream = compress?
            new NBTOutputStream(new GZIPOutputStream(stream)) :
            new NBTOutputStream(stream)) {
            
            for (NBTNamedTag tag : tags)
                nbtStream.writeNamedTag(tag);
        }
    }
    
}
