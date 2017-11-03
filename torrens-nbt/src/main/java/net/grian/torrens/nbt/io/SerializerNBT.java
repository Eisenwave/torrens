package net.grian.torrens.nbt.io;

import net.grian.torrens.io.Serializer;
import net.grian.torrens.nbt.NBTNamedTag;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class SerializerNBT implements Serializer<NBTNamedTag> {
    
    private final boolean compress;
    
    public SerializerNBT(boolean compress) {
        this.compress = compress;
    }
    
    /**
     * Constructs a new NBT-Serializer with enabled gzip compression.
     */
    public SerializerNBT() {
        this(true);
    }
    
    @Override
    public void toStream(NBTNamedTag tag, OutputStream stream) throws IOException {
        if (compress) {
            GZIPOutputStream gzipStream = new GZIPOutputStream(stream);
            NBTOutputStream nbtStream = new NBTOutputStream(gzipStream);
            nbtStream.writeNamedTag(tag);
            gzipStream.finish();
        }
        else {
            new NBTOutputStream(stream).writeNamedTag(tag);
        }
    }
    
}
