package net.grian.torrens.img.scale;

import net.grian.torrens.img.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.image.BufferedImage;

public abstract class TextureScale {
    
    /**
     * Applies the texture transformation.
     *
     * @param src the input ARGB data
     * @param out the output ARGB data or null
     * @return the output ARGB data
     */
    public int[] apply(
        @NotNull  int[] src, int srcW, int srcH,
        @Nullable int[] out, int outW, int outH) {
    
        if (out == null) out = new int[outW*outH];
        
        if (srcW == outW) {
            if (srcH == outH)
                System.arraycopy(src, 0, out, 0, outW*outH);
            
            applyY(src, srcW, srcH, out, outW, outH);
        }
        
        else if (srcH == outH)
            applyX(src, srcW, srcH, out, outW, outH);
        
        else {
            int[] tmp = new int[outW * srcH];
    
            applyX(src, srcW, srcH, tmp, outW, srcH);
            applyY(tmp, outW, srcH, out, outW, outH);
        }
        
        
        return out;
    }
    
    public Texture apply(@NotNull Texture src, @NotNull Texture out) {
        apply(
            src.getData(), src.getWidth(), src.getHeight(),
            out.getData(), out.getWidth(), out.getHeight());
        
        return out;
    }
    
    public Texture apply(@NotNull Texture src, int w, int h) {
        int[] data = apply(src.getData(), src.getWidth(), src.getHeight(), null, w, h);
        return Texture.wrap(data, w, h);
    }
    
    public Texture apply(@NotNull Texture src, float factor) {
        return apply(src, (int) (src.getWidth() * factor), (int) (src.getHeight() * factor));
    }
    
    public BufferedImage apply(@NotNull BufferedImage src, int w, int h) {
        return apply(Texture.wrapOrCopy(src), w, h).getImageWrapper();
    }
    
    protected abstract void applyX(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH);
    
    protected abstract void applyY(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH);
    
}
