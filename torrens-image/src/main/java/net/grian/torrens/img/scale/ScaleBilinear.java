package net.grian.torrens.util.img.scale;

import net.grian.spatium.util.FastMath;
import net.grian.spatium.util.PrimMath;
import net.grian.torrens.util.ConcurrentArrays;
import net.grian.torrens.util.img.Texture;
import net.grian.torrens.util.ColorMath;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("Duplicates")
public class ScaleBilinear extends TextureScale {
    
    private final int threads;
    private final ScaleBox box;
    
    public ScaleBilinear(int threads) {
        this.threads = threads;
        this.box = new ScaleBox(threads);
    }
    
    public ScaleBilinear() {
        this(1);
    }
    
    @Override
    public void applyX(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH) {
    
        final float factor = outW / (float) srcW;
        if (factor < 0.5) {
            int tmpW = (int) (srcW / Math.pow(2, (int) (-FastMath.log2(factor))));
    
            int[] tmp = box.apply(src, srcW, srcH, null, tmpW, srcH);
            applyX(tmp, tmpW, srcH, out, outW, outH);
        }
        
        AtomicInteger index = new AtomicInteger();
        
        ConcurrentArrays.run(() -> new WorkerX(src, srcW, srcH, out, outW, outH, index), threads);
    }
    
    @Override
    public void applyY(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH) {
    
        final float factor = outH / (float) srcH;
        if (factor < 0.5) {
            int tmpH = (int) (srcH / Math.pow(2, (int) (-FastMath.log2(factor))));
    
            int[] tmp = box.apply(src, srcW, srcH, null, srcW, tmpH);
            applyY(tmp, srcW, tmpH, out, outW, outH);
        }
        
        AtomicInteger index = new AtomicInteger();
        
        ConcurrentArrays.run(() -> new WorkerY(src, srcW, srcH, out, outW, outH, index), threads);
    }
    
    private static class WorkerX extends Thread {
    
        private final int[] src, out;
        private final int srcW, srcH, outW, outH;
        private final AtomicInteger index;
    
        public WorkerX(int[] src, int srcW, int srcH, int[] out, int outW, int outH, AtomicInteger index) {
            this.src = src;
            this.out = out;
            this.srcW = srcW;
            this.srcH = srcH;
            this.outW = outW;
            this.outH = outH;
            this.index = index;
        }
        
        @Override
        public void run() {
            Texture srcWrap = Texture.wrap(src, srcW, srcH);
            Texture outWrap = Texture.wrap(out, outW, outH);
            final float factor = outW / (float) srcW;
            
            int y;
            while ((y = index.getAndIncrement()) < srcH) for (int x = 0; x < outW; x++) {
                float point = (x+0.5F) / factor - 0.5F;
                final int rgb;
        
                if (point < 0)
                    rgb = srcWrap.get(0, y);
                else if (point > srcW-1)
                    rgb = srcWrap.get(srcW-1, y);
                else {
                    final int
                        x0 = (int) point,
                        x1 = PrimMath.ceil(point);
                    rgb = x0 == x1?
                        srcWrap.get(x0, y) :
                        interpolate(srcWrap.get(x0, y), srcWrap.get(x1, y), point%1);
                }
    
                outWrap.set(x, y, rgb);
            }
        }
    }
    
    private static class WorkerY extends Thread {
    
        private final int[] src, out;
        private final int srcW, srcH, outW, outH;
        private final AtomicInteger index;
    
        public WorkerY(int[] src, int srcW, int srcH, int[] out, int outW, int outH, AtomicInteger index) {
            this.src = src;
            this.out = out;
            this.srcW = srcW;
            this.srcH = srcH;
            this.outW = outW;
            this.outH = outH;
            this.index = index;
        }
        
        @Override
        public void run() {
            Texture srcWrap = Texture.wrap(src, srcW, srcH);
            Texture outWrap = Texture.wrap(out, outW, outH);
            final float factor = outH / (float) srcH;
            
            int x;
            while ((x = index.getAndIncrement()) < srcW) for (int y = 0; y < outH; y++) {
                float point = (y+0.5F) / factor - 0.5F;
                final int rgb;
        
                if (point < 0)
                    rgb = srcWrap.get(x, 0);
                else if (point > srcH-1)
                    rgb = srcWrap.get(x, srcH-1);
                else {
                    final int
                        y0 = (int) point,
                        y1 = PrimMath.ceil(point);
            
                    rgb = y0 == y1?
                        srcWrap.get(x, y0) :
                        interpolate(srcWrap.get(x, y0), srcWrap.get(x, y1), point%1);
                }
        
                outWrap.set(x, y, rgb);
            }
        }
    }
    
    // UTIL
    
    /**
     * Linearly interpolates between two colors.
     *
     * @param rgb0 the first color
     * @param rgb1 the second color
     * @param p the point in range(0,1)
     * @return the interpolated color
     */
    @Contract(pure = true)
    private static int interpolate(int rgb0, int rgb1, float p) {
        return ColorMath.blend(rgb1, rgb0, p);
    }
    
}
