package net.grian.torrens.mson;

import net.grian.torrens.io.TextSerializer;
import net.grian.torrens.nbt.NBTNamedTag;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class SerializerMSON implements TextSerializer<NBTNamedTag> {
    
    @Override
    public void toWriter(NBTNamedTag nbt, Writer writer) throws IOException {
        new MojangsonWriter(writer).writeNamedTag(nbt);
    }
    
    @NotNull
    @Override
    public String toString(NBTNamedTag nbt) {
        StringWriter stringWriter = new StringWriter();
        try {
            new MojangsonWriter(stringWriter).writeNamedTag(nbt);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return stringWriter.toString();
    }
    
}
