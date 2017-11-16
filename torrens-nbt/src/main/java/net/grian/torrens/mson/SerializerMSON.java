package net.grian.torrens.mson;

import net.grian.torrens.io.TextSerializer;
import net.grian.torrens.nbt.NBTNamedTag;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class SerializerMSON implements TextSerializer<NBTNamedTag> {
    
    private final boolean pretty;
    
    public SerializerMSON(boolean pretty) {
        this.pretty = pretty;
    }
    
    public SerializerMSON() {
        this(false);
    }
    
    @Override
    public void toWriter(NBTNamedTag nbt, Writer writer) throws IOException {
        MojangsonWriter msonWriter = new MojangsonWriter(writer, pretty);
        msonWriter.writeNamedTag(nbt);
        msonWriter.endLn(); // end last line to comply with POSIX standard
    }
    
    @NotNull
    @Override
    public String toString(NBTNamedTag nbt) {
        StringWriter stringWriter = new StringWriter();
        try {
            toWriter(nbt, stringWriter);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        return stringWriter.toString();
    }
    
}
