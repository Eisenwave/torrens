package eisenwave.torrens.io;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

public class SerializerCharArray implements TextSerializer<char[]> {
    
    @Override
    public void toWriter(char[] chars, Writer writer) throws IOException {
        writer.write(chars);
    }
    
    @NotNull
    @Override
    public char[] toCharArray(char[] chars) throws IOException {
        return Arrays.copyOf(chars, chars.length);
    }
    
}
