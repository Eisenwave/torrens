package net.grian.torrens.mson;

import net.grian.spatium.util.Strings;
import net.grian.torrens.nbt.*;

import java.io.StringWriter;

public class MojangsonPrinter {
    
    private static final String
    NEWLINE = System.getProperty("line.separator"),
    INDENT = "    ";
    
    public static String print(String name, NBTCompound root) {
        MojangsonPrinter printer = new MojangsonPrinter(name, root);
        printer.write();
        return printer.writer.toString();
    }
    
    public static String print(NBTCompound compound) {
        return print("", compound);
    }
    
    private final StringWriter writer = new StringWriter();
    private final String name;
    private final NBTCompound root;
    
    private int indent = 0;
    
    private MojangsonPrinter(String name, NBTCompound root) {
        this.name = name;
        this.root = root;
    }
    
    public void write() {
        if (!name.isEmpty()) {
            writer
                .append(new NBTString(name).toMSONString())
                .append(": ");
        }
    
        writeCompound(root);
    }
    
    public String writeCompound(NBTCompound compound) {
        writer.write('{');
        
        if (!compound.isEmpty()) {
        
        }
        
        
    }
    
    // UTIL
    
    private void indent() {
        writer.write(Strings.repeat(INDENT, this.indent));
    }
    
    private void newLine() {
        writer.write(NEWLINE);
    }
    
    private void writeLine(String str) {
        indent();
        writer.write(str);
        writer.write(NEWLINE);
    }
    
}
