package eisenwave.torrens.img;

import net.grian.spatium.util.TestUtil;
import eisenwave.torrens.img.scale.ScaleBilinear;
import eisenwave.torrens.img.scale.ScaleBox;
import eisenwave.torrens.img.scale.ScaleNearestNeighbour;
import eisenwave.torrens.img.scale.TextureScale;
import eisenwave.torrens.util.ANSI;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;

public class TexturesTest {
    
    
    private final File DEBUG_FILE = new File("F:/Porn/ScaleTest.png");
    
    public void scale_performance() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        assertNotNull(img);
        Texture texture = Texture.wrapOrCopy(img);
    
        title("NEAREST NEIGHBOUR");
        scale(texture, new ScaleNearestNeighbour(), 64, 1);
        scale(texture, new ScaleNearestNeighbour(), 64, 100);
        scale(texture, new ScaleNearestNeighbour(), 2000, 1);
    
        title("BOX SCALING");
        scale(texture, new ScaleBox(), 64, 1);
        scale(texture, new ScaleBox(), 64, 100);
        scale(texture, new ScaleBox(), 1500, 1);
    
        title("PARALLEL BOX SCALING");
        scale(texture, new ScaleBox(4), 64, 1);
        scale(texture, new ScaleBox(4), 64, 100);
        scale(texture, new ScaleBox(4), 1500, 1);
    
        title("BILINEAR");
        scale(texture, new ScaleBilinear(), 64, 1);
        scale(texture, new ScaleBilinear(), 64, 100);
        scale(texture, new ScaleBilinear(), 1500, 1);
    
        title("PARALLEL BILINEAR SCALING");
        scale(texture, new ScaleBilinear(4), 64, 1);
        scale(texture, new ScaleBilinear(4), 64, 100);
        scale(texture, new ScaleBilinear(4), 1500, 1);
    }
    
    @Test
    public void scale_nearest() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
        
        Texture result = new ScaleNearestNeighbour().apply(texture, 100, 100);
        BufferedImage out = result.toImage(true);
        
        if (DEBUG_FILE.canWrite())
            new SerializerPNG().toFile(out, DEBUG_FILE);
    }
    
    @Test
    public void scale_box() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
        
        Texture result = new ScaleBox().apply(texture, 100, 100);
        BufferedImage out = result.toImage(true);
        
        if (DEBUG_FILE.canWrite())
            new SerializerPNG().toFile(out, DEBUG_FILE);
    }
    
    @Test
    public void scale_bilinear() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
        
        Texture result = new ScaleBilinear().apply(texture, 100, 100);
        BufferedImage out = result.toImage(true);
        
        if (DEBUG_FILE.canWrite())
            new SerializerPNG().toFile(out, DEBUG_FILE);
    }
    
    @Test
    public void lightSource() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
        System.out.println(texture.isWrapper());
        System.out.println(Textures.lightSource(texture));
    }
    
    private static void title(String str) {
        System.out.println("\n"+ ANSI.BOLD+str+ANSI.BOLD_OFF);
    }
    
    private static void scale(Texture t, TextureScale method, int dims, int tests) {
        long millis = TestUtil.millisOf(() -> method.apply(t, dims, dims), tests);
        System.out.println("scaled: "+t+" to "+dims+"x "+tests+" times in "+ ANSI.FG_RED+ millis+"ms"+ANSI.RESET);
    }
    
}
