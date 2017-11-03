package net.grian.torrens.util.img.scale;

import net.grian.spatium.util.TestUtil;
import net.grian.torrens.util.img.Texture;

public class TSBoxTest {
    
    public void apply() throws Exception {
        final int in = 4096, out = 64, tests = 100;
        
        Texture texture = Texture.alloc(in, in);
        Assert.assertNotNull(texture);
    
        long consecutive = TestUtil.millisOf(() -> {
            ScaleBox scale = new ScaleBox(1);
            scale.apply(texture, out, out);
        }, tests);
        
        long concurrent = TestUtil.millisOf(() -> {
            ScaleBox scale = new ScaleBox(4);
            scale.apply(texture, out, out);
        }, tests);
        
        System.out.println("consecutive: "+consecutive+"ms");
        System.out.println("concurrent: "+concurrent+"ms");
    }
    
}