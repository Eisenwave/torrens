package net.grian.torrens.util.img;

import net.grian.torrens.img.SerializerJPEG;
import net.grian.torrens.img.Texture;
import org.junit.Test;

public class SerializerJPEGTest {
    
    @Test
    public void toBytes() throws Exception {
        Texture texture = Texture.alloc(16, 16);
        byte[] bytes = new SerializerJPEG().toBytes(texture.getImageWrapper());
        Assert.assertTrue(bytes.length > 32);
    }
    
}