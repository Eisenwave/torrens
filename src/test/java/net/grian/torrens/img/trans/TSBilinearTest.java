package net.grian.torrens.img.trans;

import net.grian.spatium.util.TestUtil;
import net.grian.torrens.img.Texture;
import org.junit.Test;

import static org.junit.Assert.*;

public class TSBilinearTest {
    
    @Test
    public void apply() throws Exception {
        final int in = 2048, out = 50, tests = 10;
    
        Texture texture = Texture.alloc(in, in);
        assertNotNull(texture);
    
        long consecutive = TestUtil.millisOf(() -> {
            TSBilinear scale = new TSBilinear(texture, out, out, 1);
            scale.apply();
        }, tests);
    
        long concurrent = TestUtil.millisOf(() -> {
            TSBilinear scale = new TSBilinear(texture, out, out, 4);
            scale.apply();
        }, tests);
    
        System.out.println("consecutive: "+consecutive+"ms");
        System.out.println("concurrent: "+concurrent+"ms");
    }
    
}