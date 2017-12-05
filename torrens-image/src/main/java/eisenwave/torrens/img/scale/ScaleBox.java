package eisenwave.torrens.img.scale;

import eisenwave.torrens.util.ConcurrentArrays;
import eisenwave.torrens.img.Texture;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("Duplicates")
public class ScaleBox extends TextureScale {
    
    private final int threads;
    
    private final static TextureScale NNB = new ScaleNearestNeighbour();
    
    public ScaleBox(int threads) {
        if (threads < 1) throw new IllegalArgumentException("at least one thread required");
        this.threads = threads;
    }
    
    public ScaleBox() {
        this(1);
    }
    
    @Override
    public void applyX(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH) {
        
        if (outW > srcW)
            NNB.apply(src, srcW, srcH, out, outW, outH);
    
        else {
            AtomicInteger index = new AtomicInteger();
            ConcurrentArrays.run(() -> new WorkerX(src, srcW, srcH, out, outW, outH, index), threads);
        }
    }
    
    @Override
    public void applyY(
        @NotNull int[] src, int srcW, int srcH,
        @NotNull int[] out, int outW, int outH) {
    
        if (outH > srcH)
            NNB.apply(src, srcW, srcH, out, outW, outH);
        
        else {
            AtomicInteger index = new AtomicInteger();
            ConcurrentArrays.run(() -> new WorkerY(src, srcW, srcH, out, outW, outH, index), threads);
        }
        
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
            
            int x;
            while ((x = index.getAndIncrement()) < outW) {
                final int
                    minX =     x * srcW / outW,
                    maxX = (x+1) * srcW / outW - 1;
                
                for (int y = 0; y < srcH; y++) {
                    final int rgb = minX==maxX?
                        srcWrap.get(minX, y) :
                        srcWrap.averageRGB(minX, y, maxX, y, true);
                    outWrap.set(x, y, rgb);
                }
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
            
            int y;
            while ((y = index.getAndIncrement()) < outH){
                final int
                    minY =     y * srcH / outH,
                    maxY = (y+1) * srcH / outH - 1;
        
                for (int x = 0; x < srcW; x++) {
                    final int rgb = minY==maxY?
                        srcWrap.get(x, minY) :
                        srcWrap.averageRGB(x, minY, x, maxY, true);
    
                    outWrap.set(x, y, rgb);
                }
            }
        }
    }
    
}
