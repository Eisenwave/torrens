package net.grian.torrens.img;

import net.grian.spatium.util.TestUtil;
import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class TexturesTest {
    
    public void scale_performance() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
    
        scale(texture, 64, Textures.SCALE_AREA_AVG, 1);
        scale(texture, 64, Textures.SCALE_AREA_AVG, 100);
        scale(texture, 1500, Textures.SCALE_AREA_AVG, 10);
        
        scale(texture, 64, Textures.SCALE_NEAREST_NB, 1);
        scale(texture, 64, Textures.SCALE_NEAREST_NB, 100);
        scale(texture, 2000, Textures.SCALE_NEAREST_NB, 10);
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
        System.out.println("scaled down: "+t+" to "+d+"x "+c+" times in "+ millis+"ms");
    }
    
}