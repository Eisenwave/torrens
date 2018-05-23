package eisenwave.torrens.io;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;

public class DeserializerString implements TextDeserializer<String> {
    
    @NotNull
    @Override
    public String fromReader(Reader reader) {
        StringBuilder builder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(reader);
        bufferedReader.lines().forEachOrdered(builder::append);
        return builder.toString();
    }
    
    @NotNull
    @Override
    public String fromFile(File file) throws IOException {
        return Files.readAllLines(file.toPath()).stream().reduce("", String::concat);
    }
    
    @NotNull
    @Override
    public String fromString(String str) {
        return str;
    }
    
}
