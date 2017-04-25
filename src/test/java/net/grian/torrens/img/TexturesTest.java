package net.grian.torrens.img;

import org.junit.Test;

import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

public class TexturesTest {
    
    @Test
    public void lightSource() throws Exception {
        BufferedImage img = new DeserializerImage().fromResource(getClass(), "subway.png");
        Texture texture = Texture.wrapOrCopy(img);
        System.out.println(texture.isWrapper());
        System.out.println(Textures.lightSource(texture));
    }
    
}