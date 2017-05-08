package net.grian.torrens.wavefront;

import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.io.TextDeserializer;
import net.grian.torrens.object.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.logging.Logger;

public class DeserializerOBJ implements TextDeserializer<OBJModel> {
    
    @Nullable
    private final Logger logger;
    
    private final OBJModel model;
    private final File mtlDir;
    
    private OBJGroup group;
    
    private void warning(String msg) {
        if (logger != null)
            logger.warning(msg);
    }
    
    private void debug(String msg) {
        if (logger != null)
            logger.fine(msg);
    }
    
    public DeserializerOBJ(@Nonnull OBJModel model, @Nonnull File mtlDir, @Nullable Logger logger) {
        this.model = Objects.requireNonNull(model);
        this.mtlDir = Objects.requireNonNull(mtlDir);
        
        this.logger = logger;
    }
    
    public DeserializerOBJ(@Nonnull OBJModel model, @Nonnull File mtlDir) {
        this(model, mtlDir, null);
    }
    
    @Override
    public OBJModel fromReader(Reader reader) throws IOException {
        try (BufferedReader buffReader = new BufferedReader(reader)) {
            return fromReader(buffReader);
        }
    }
    
    public OBJModel fromReader(BufferedReader reader) throws IOException {
        this.group = model.getDefaultGroup();
        
        int number = 0;
        String content;
        while ((content = reader.readLine()) != null)
            readLine(++number, content);
    
        return model;
    }
    
    private void readLine(int number, String line) throws FileSyntaxException {
        line = line.trim();
        if (line.isEmpty()) return; //empty line
        if (line.charAt(0) == '#') return; //comment line
        
        String[] args = line.split("[ ]+"); //in case some idiot double spaced between arguments
        if (args.length < 2) error(number, "less than 2 arguments");
        
        try {
            switch (args[0]) {
                case "mtllib": readMtlLib(args); break;
                case "usemtl": readUseMtl(args); break;
                case "v": readV(args); break;
                case "vt": readVT(args); break;
                case "vn": readVN(args); break;
                case "f": readF(args); break;
                case "g": readG(args); break;
                default: debug(number+": unknown arg '"+args[0]+"'");
            }
        } catch (Exception ex) {
            error(number, ex);
        }
    }
    
    private void readMtlLib(String[] args) throws IOException {
        String path = args[1];
        MTLLibrary library = new MTLLibrary(path);
        model.setMaterials(library);
        
        File file = new File(mtlDir, path);
        if (!file.exists() || !file.isFile()) {
            warning("invalid mtllib reference: "+file);
            return;
        }
        
        debug("loading mtllib from: "+file);
        new DeserializerMTL(library, mtlDir, logger).fromFile(file);
    }
    
    private void readV(String[] args) {
        if (args.length < 4) throw new IllegalArgumentException("insufficient argument count");
        Vertex3f vertex = new Vertex3f(
            Float.parseFloat(args[1]),
            Float.parseFloat(args[2]),
            Float.parseFloat(args[3]));
        model.addVertex(vertex);
    }
    
    private void readVN(String[] args) {
        if (args.length < 4) throw new IllegalArgumentException("insufficient argument count");
        Vertex3f vertex = new Vertex3f(
            Float.parseFloat(args[1]),
            Float.parseFloat(args[2]),
            Float.parseFloat(args[3]));
        model.addNormal(vertex);
    }
    
    private void readVT(String[] args) {
        if (args.length < 3) throw new IllegalArgumentException("insufficient argument count");
        Vertex2f vertex = new Vertex2f(
            Float.parseFloat(args[1]),
            Float.parseFloat(args[2]));
        model.addTexture(vertex);
    }
    
    private void readF(String[] args) {
        OBJTriplet[] triplets = new OBJTriplet[args.length - 1];
        for (int i = 1; i<args.length; i++) {
            triplets[i-1] = OBJTriplet.parseTriplet(args[i]);
        }
        this.group.addFace(new OBJFace(triplets));
    }
    
    private void readG(String[] args) {
        this.group = new OBJGroup(model, args[1]);
        model.addGroup(group);
    }
    
    private void readUseMtl(String[] args) {
        MTLLibrary mtllib = model.getMaterials();
        if (mtllib == null) {
            warning("usmtl call \""+args[1]+"\" but obj has no mtllib");
            return;
        }
        MTLMaterial material = mtllib.getMaterial(args[1]);
        if (material == null) {
            warning("invalid usemtl reference in g \""+this.group.getName()+"\": "+args[1]);
            return;
        }
        
        this.group.setMaterial(args[1]);
    }
    
    private static void error(int line, String error) throws FileSyntaxException {
        throw new FileSyntaxException("line "+line+": "+error);
    }
    
    private static void error(int line, Exception error) throws FileSyntaxException {
        throw new FileSyntaxException("line "+line, error);
    }
    
}
