package net.grian.torrens.img;

import net.grian.spatium.util.TestUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;

public class TexturesTest {
    
    private final File DEBUG_FILE = new File("F:/Porn/BilinearScaling.png");
    
    public void scale_performance() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
    
        System.out.println("NEAREST NEIGHBOUR");
        scale(texture, 64, Textures.SCALE_NEAREST_NB, 1);
        scale(texture, 64, Textures.SCALE_NEAREST_NB, 100);
        scale(texture, 2000, Textures.SCALE_NEAREST_NB, 1);
    
        System.out.println("BILINEAR");
        scale(texture, 64, Textures.SCALE_BILINEAR, 1);
        scale(texture, 64, Textures.SCALE_BILINEAR, 100);
        scale(texture, 1500, Textures.SCALE_BILINEAR, 1);
    
        System.out.println("AREA AVERAGE");
        scale(texture, 64, Textures.SCALE_AREA_AVG, 1);
        scale(texture, 64, Textures.SCALE_AREA_AVG, 100);
        scale(texture, 1500, Textures.SCALE_AREA_AVG, 1);
    }
    
    @Test
    public void scale_bilinear() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
        
        Texture result = Textures.scale(texture, 64, 64, Textures.SCALE_AREA_AVG);
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
    
    private static void scale(Texture t, int d, int m, int c) {
        long millis = TestUtil.millisOf(() -> Textures.scale(t, d, d, m), c);
        System.out.println("scaled: "+t+" to "+d+"x "+c+" times in "+ millis+"ms");
    }
    
}