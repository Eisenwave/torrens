package eisenwave.torrens.wavefront;

import eisenwave.torrens.error.FileSyntaxException;
import eisenwave.torrens.img.DeserializerImage;
import eisenwave.torrens.img.Texture;
import eisenwave.torrens.io.TextDeserializer;
import eisenwave.torrens.util.ColorMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.logging.Logger;

public class DeserializerMTL implements TextDeserializer<MTLLibrary> {
    
    private final Logger logger;
    
    private final MTLLibrary mtllib;
    private final File mapDir;
    
    private MTLMaterial newmtl;
    
    public DeserializerMTL(@NotNull MTLLibrary mtllib, @NotNull File mapDir, @Nullable Logger logger) {
        this.mtllib = Objects.requireNonNull(mtllib);
        this.mapDir = Objects.requireNonNull(mapDir);
        this.logger = logger;
    }
    
    public DeserializerMTL(MTLLibrary mtllib, File mapDir) {
        this(mtllib, mapDir, null);
    }
    
    @NotNull
    @Override
    public MTLLibrary fromReader(Reader reader) throws IOException {
        try (BufferedReader buffReader = new BufferedReader(reader)) {
            return fromReader(buffReader);
        }
    }
    
    public MTLLibrary fromReader(BufferedReader reader) throws IOException {
        int number = 0;
        String content;
        while ((content = reader.readLine()) != null)
            readLine(++number, content);
        
        return mtllib;
    }
    
    private void readLine(int number, String line) throws FileSyntaxException {
        line = line.trim();
        if (line.isEmpty()) return; //empty line
        if (line.charAt(0) == '#') return; //comment line
        
        String[] args = line.split("[ ]+"); //in case some idiot double spaced between arguments
        if (args.length < 2) error(number, "less than 2 arguments");
        
        if (args[0].equals("newmtl")) {
            this.newmtl = new MTLMaterial(mtllib, args[1]);
            mtllib.addMaterial(newmtl);
            return;
        }
        
        if (newmtl == null)
            error(number, "material property defined before material");
        try {
            switch (args[0]) {
                case "Ka": readKa(args); break;
                case "Kd": readKd(args); break;
                case "Ks": readKs(args); break;
                case "Tf": readTf(args); break;
                case "d": readD(args); break;
                case "sharpness": readSharpness(args); break;
                case "illum": readIllum(args); break;
                case "map_Ka": readMapKa(args); break;
                case "map_Kd": readMapKd(args); break;
                case "map_Ks": readMapKs(args); break;
                case "map_Ns": readMapNs(args); break;
                case "map_d": readMapD(args); break;
                case "bump": readBump(args); break;
                case "disp": readDisp(args); break;
                case "decal": readDecal(args); break;
                default: if (logger != null) logger.fine(number+": unknown arg '"+args[0]+"'");
            }
        } catch (Exception ex) {
            error(number, ex.getMessage());
        }
    }
    
    private void readKa(String[] args) {
        int rgb = deserializeColor(args);
        newmtl.setAmbientColor(rgb);
    }
    
    private void readKd(String[] args) {
        int rgb = deserializeColor(args);
        newmtl.setDiffuseColor(rgb);
    }
    
    private void readKs(String[] args) {
        int rgb = deserializeColor(args);
        newmtl.setSpecularColor(rgb);
    }
    
    private void readTf(String[] args) {
        int rgb = deserializeColor(args);
        newmtl.setTransmissionFilter(rgb);
    }
    
    private void readIllum(String[] args) {
        byte illum = Byte.parseByte(args[1]);
        newmtl.setIlluminationModel(illum);
    }
    
    private void readD(String[] args) {
        float d = parseFloat(args);
        newmtl.setDissolution(d);
    }
    
    private void readSharpness(String[] args) {
        float sharpness = parseFloat(args);
        newmtl.setSharpness(sharpness);
    }
    
    private void readMapKa(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setAmbientMap(path);
    
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    private void readMapKd(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setDiffuseMap(path);
    
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    private void readMapKs(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setSpecularColorMap(path);
    
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    private void readMapNs(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setSpecularHighlightMap(path);
    
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    private void readMapD(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setDissolutionMap(path);
    
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    private void readBump(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setBumpMap(path);
    
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    private void readDisp(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setDisplacementMap(path);
    
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    private void readDecal(String[] args) throws IOException {
        String path = args[args.length-1];
        newmtl.setDecalMap(path);
        
        Texture map = deserializeMap(path);
        if (map != null) mtllib.addMap(path, map);
    }
    
    // UTIL
    
    private Texture deserializeMap(String path) throws IOException {
        File file = new File(mapDir, path);
        if (!file.exists() || !file.isFile()) {
            if (logger != null) logger.warning("invalid texture reference: "+file);
            return null;
        }
    
        BufferedImage image = new DeserializerImage().fromFile(file);
        return Texture.copy(image);
    }
    
    private static int deserializeColor(String[] args) throws IllegalArgumentException {
        if (args.length < 4) throw new IllegalArgumentException("insufficient argument count");
        float r = Float.parseFloat(args[1]);
        float g = Float.parseFloat(args[2]);
        float b = Float.parseFloat(args[3]);
        
        return ColorMath.fromRGB(r, g, b);
    }
    
    private static float parseFloat(String[] args) throws IllegalArgumentException {
        if (args.length < 2) throw new IllegalArgumentException("insufficient argument count");
        return Float.parseFloat(args[1]);
    }
    
    private static void error(int line, String error) throws FileSyntaxException {
        throw new FileSyntaxException("line "+line+": "+error);
    }
    
}
