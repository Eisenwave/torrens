package net.grian.torrens.img.scale;

import net.grian.torrens.img.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

@SuppressWarnings("Duplicates")
public class ScaleNearestNeighbour extends TextureScale {
    
    @Override
    public int[] apply(
        @NotNull  int[] src, int srcW, int srcH,
        @Nullable int[] out, int outW, int outH) {
        
        if (srcW == outW && srcH == outH) {
            if (out == null)
                return Arrays.copyOf(src, outW*outH);
        }
        
        if (out == null)
            out = new int[outW * outH];
    
        Texture srcWrap = Texture.wrap(src, srcW, srcH);
        Texture outWrap = Texture.wrap(out, outW, outH);
        
        for (int x = 0; x < outW; x++) for (int y = 0; y < outH; y++) {
            final int rgb = srcWrap.get(
                x * srcW / outW,
                y * srcH / outH);
            outWrap.set(x, y, rgb);
        }
        
        return out;
    }
    
    @Override
    public void applyX(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH) {
        
        Texture srcWrap = Texture.wrap(src, srcW, srcH);
        Texture outWrap = Texture.wrap(out, outW, outH);
        
        for (int x = 0; x < outW; x++) {
            final int x2 = x * srcW;
            
            for (int y = 0; y < srcH; y++) {
                final int rgb = srcWrap.get(x2 / outW, y);
                outWrap.set(x, y, rgb);
            }
        }
    }
    
    @Override
    public void applyY(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH) {
        
        Texture srcWrap = Texture.wrap(src, srcW, srcH);
        Texture outWrap = Texture.wrap(out, outW, outH);
        
        for (int y = 0; y < outH; y++) {
            final int y2 = y * srcH;
            
            for (int x = 0; x < srcW; x++)  {
                final int rgb = srcWrap.get(x, y2 / outH);
                outWrap.set(x, y, rgb);
            }
        }
    }
}
