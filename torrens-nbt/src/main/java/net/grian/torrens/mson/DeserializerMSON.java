package net.grian.torrens.mson;

import net.grian.torrens.io.TextDeserializer;
import net.grian.torrens.nbt.NBTNamedTag;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.stream.Collectors;

public class DeserializerMSON implements TextDeserializer<NBTNamedTag> {
    
    @NotNull
    @Override
    public NBTNamedTag fromReader(Reader reader) throws IOException {
        BufferedReader buffReader = reader instanceof BufferedReader?
            (BufferedReader) reader :
            new BufferedReader(reader);
        
        String mson = buffReader.lines().collect(Collectors.joining());
        
        return MojangsonParser.parse(mson);
    }
    
}
