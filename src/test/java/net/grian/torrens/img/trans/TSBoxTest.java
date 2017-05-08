package net.grian.torrens.img.trans;

import net.grian.spatium.util.TestUtil;
import net.grian.torrens.img.Texture;
import org.junit.Test;

import static org.junit.Assert.*;

public class TSBoxTest {
    
    public void apply() throws Exception {
        final int in = 4096, out = 64, tests = 100;
        
        Texture texture = Texture.alloc(in, in);
        assertNotNull(texture);
    
        long consecutive = TestUtil.millisOf(() -> {
            TSBox scale = new TSBox(texture, out, out, 1);
            scale.apply();
        }, tests);
        
        long concurrent = TestUtil.millisOf(() -> {
            TSBox scale = new TSBox(texture, out, out, 4);
            scale.apply();
        }, tests);
        
        System.out.println("consecutive: "+consecutive+"ms");
        System.out.println("concurrent: "+concurrent+"ms");
    }
    
}