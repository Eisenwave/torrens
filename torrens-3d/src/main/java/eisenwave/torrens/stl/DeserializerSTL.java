package eisenwave.torrens.stl;

import eisenwave.torrens.error.FileSyntaxException;
import eisenwave.torrens.io.Deserializer;
import eisenwave.torrens.io.LittleDataInputStream;
import eisenwave.torrens.object.Vertex3f;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * <p>
 * A deserializer for <b>Stereo-Lithography / Standard-Tessellation-Language (.stl)</b> files.
 * <p>
 * Only binary STL is supported, triangle attributes are given no special interpretation.
 */
public class DeserializerSTL implements Deserializer<STLModel> {
    
    private final static Charset US_ASCII = Charset.forName("US-ASCII");
    private final static String ASCII_STL_MAGIC = "solid";
    
    /** expect start of file */
    private final static int
        S_START = 0,
    /** expect facet or endsolid */
    S_FACET = 1,
    /** expect out loop */
    S_OUTER_LOOP = 2,
    /** expect first vertex */
    S_VERTEX_1 = 3,
    /** expect second vetex */
    S_VERTEX_2 = 4,
    /** expect third vertex */
    S_VERTEX_3 = 5,
    /** expect endloop */
    S_ENDLOOP = 6,
    /** expect endfacet */
    S_ENDFACET = 7;
    
    /*
    @NotNull
    @Override
    public STLModel fromStream(InputStream stream) throws IOException {
        LittleDataInputStream dataStream = new LittleDataInputStream(stream);
        STLModel result = fromStream(dataStream);
        return result;
    }
    */
    
    private STLModel result = null;
    private int state = 0;
    private Vertex3f[] buffer = new Vertex3f[4];
    
    @NotNull
    @Override
    public STLModel fromStream(InputStream stream) throws IOException {
        String fiveStr = deserializeASCII(stream, 5);
        
        if (fiveStr.equals(ASCII_STL_MAGIC)) {
            if (stream.skip(1) != 1) throw new IOException(); //one space
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            String solidName = reader.readLine();
            
            result = new STLModel(solidName);
            int lineNo = 2;
            this.state = S_FACET;
            for (String line = reader.readLine(); ; line = reader.readLine(), lineNo++) {
                if (line == null)
                    throw new FileSyntaxException("line " + lineNo + ": unexpected end of file");
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("endsolid")) {
                    if (state != S_FACET)
                        throw new FileSyntaxException("line " + lineNo + ": unexpected endsolid");
                    else break;
                }
                try {
                    interpret(line);
                } catch (IllegalArgumentException ex) {
                    throw new FileSyntaxException("line " + lineNo + ": " + ex.getMessage());
                }
            }
        }
        
        else {
            LittleDataInputStream littleStream = new LittleDataInputStream(stream);
            String header = fiveStr + deserializeASCII(stream, 75);
            result = new STLModel(header);
            
            final int size = littleStream.readLittleInt();
            for (int i = 0; i < size; i++)
                result.add(deserializeTriangle(littleStream));
        }
        
        return result;
    }
    
    private void interpret(String line) throws IllegalArgumentException {
        assert !line.isEmpty();
        String[] split = line.split("[ ]+", 5);
        assert split.length > 0;
        
        switch (state) {
            case S_FACET:
                if (!split[0].equals("facet"))
                    throw new IllegalArgumentException("expected 'facet ...': " + line);
                if (split.length > 2) {
                    if (!split[1].equals("normal") || split.length != 5)
                        throw new IllegalArgumentException("expected 'facet normal <x> <y> <z>': " + line);
                    buffer[3] = parseVertex(split[2], split[3], split[4]); // EITHER use the given normal ...
                }
                else buffer[3] = null; // ... OR calculate the normal from the 3 vertices
                state = S_OUTER_LOOP;
                break;
            
            case S_OUTER_LOOP:
                if (!line.equals("outer loop"))
                    throw new IllegalArgumentException("expected 'outer loop'");
                state = S_VERTEX_1;
                break;
            
            case S_VERTEX_1:
            case S_VERTEX_2:
            case S_VERTEX_3: {
                if (!split[0].equals("vertex") || split.length != 4)
                    throw new IllegalArgumentException("invalid syntax (vertex): " + line);
                buffer[state - S_VERTEX_1] = parseVertex(split[1], split[2], split[3]);
                if (state == S_VERTEX_3) {
                    Vertex3f
                        v1 = buffer[0], v2 = buffer[1], v3 = buffer[2],
                        n = buffer[3] != null? buffer[3] : normal(v1, v2, v3);
                    result.add(new STLTriangle(buffer[3], buffer[0], buffer[1], buffer[2]));
                }
                state++;
                break;
            }
            
            case S_ENDLOOP:
                if (!line.equals("endloop"))
                    throw new IllegalArgumentException("expected 'endloop'");
                state = S_ENDFACET;
                break;
            
            case S_ENDFACET:
                if (!line.equals("endfacet"))
                    throw new IllegalArgumentException("expected 'endfacet'");
                state = S_FACET;
                break;
        }
    }
    
    private static Vertex3f normal(Vertex3f v1, Vertex3f v2, Vertex3f v3) {
        return v2.minus(v1).cross(v3.minus(v1)).normalized();
    }
    
    private static Vertex3f parseVertex(String x, String y, String z) throws IllegalArgumentException {
        return new Vertex3f(
            Float.parseFloat(x),
            Float.parseFloat(y),
            Float.parseFloat(z));
    }
    
    private static String deserializeASCII(InputStream stream, int length) throws IOException {
        byte[] bytes = new byte[length];
        if (stream.read(bytes) < length) throw new IOException();
        return new String(bytes, US_ASCII);
    }
    
    private static STLTriangle deserializeTriangle(LittleDataInputStream stream) throws IOException {
        return new STLTriangle(
            deserializeVertex(stream),
            deserializeVertex(stream),
            deserializeVertex(stream),
            deserializeVertex(stream),
            stream.readShort());
    }
    
    private static Vertex3f deserializeVertex(LittleDataInputStream stream) throws IOException {
        return new Vertex3f(
            stream.readLittleFloat(),
            stream.readLittleFloat(),
            stream.readLittleFloat());
    }
    
}
