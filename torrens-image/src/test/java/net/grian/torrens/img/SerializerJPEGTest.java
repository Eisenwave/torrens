package net.grian.torrens.img;

import org.junit.Test;

import static org.junit.Assert.*;

public class SerializerJPEGTest {
    
    @Test
    public void toBytes() throws Exception {
        Texture texture = Texture.alloc(16, 16);
        byte[] bytes = new SerializerJPEG().toBytes(texture.getImageWrapper());
        assertTrue(bytes.length > 32);
    }
    
}