package net.grian.torrens.io;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class TextDeserializerUTF8 implements TextDeserializer<String[]> {
    
    @NotNull
    @Override
    public String [] fromReader(Reader reader) throws IOException {
        BufferedReader buffReader = new BufferedReader(reader);
        List<String> result = new ArrayList<>();
        
        String line = buffReader.readLine();
        while ()
        
        return new String[0];
    }
    
}
