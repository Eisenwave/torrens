package net.grian.torrens.img.trans;

import net.grian.spatium.util.ColorMath;
import net.grian.spatium.util.FastMath;
import net.grian.spatium.util.PrimMath;
import net.grian.torrens.img.Texture;
import org.jetbrains.annotations.Contract;

@SuppressWarnings("Duplicates")
public class TSBilinear extends TextureScale {
    
    
    private volatile int X, Y;
    
    private final int threads;
    private float factor;
    
    public TSBilinear(Texture in, int w1, int h1, int threads) {
        super(in, w1, h1);
        this.threads = threads;
    }
    
    public TSBilinear(Texture in, int w1, int h1) {
        this(in, w1, h1, 1);
    }
    
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
    
    @Override
    public Texture applyX() {
        if (w0 == w1)
            return out = in.clone();
    
        factor = w1 / (float) w0;
        if (factor < 0.5) {
            int boxDiv = (int) Math.pow(2, (int) (-FastMath.log2(factor)));
            TextureScale box = new TSBox(in, w0 / boxDiv, h0, threads);
            setIn(box.applyX());
            return applyX();
        }
    
        out = Texture.alloc(w1, h1);
        if (threads == 1) {
            new WorkerX().run();
            return out;
        }
    
        WorkerX[] workers = new WorkerX[threads];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new WorkerX();
            workers[i].start();
        }
    
        for (WorkerX worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        
        return out;
    }
    
    @Override
    public Texture applyY() {
        if (h0 == h1)
            return out = in.clone();
    
        factor = h1 / (float) h0;
        if (factor < 0.5) {
            int boxDiv = (int) Math.pow(2, (int) (-FastMath.log2(factor)));
            TextureScale box = new TSBox(in, w0, h0 / boxDiv, threads);
            setIn(box.applyY());
            return applyY();
        }
    
        out = Texture.alloc(w1, h1);
        if (threads == 1) {
            new WorkerY().run();
            return out;
        }
    
        WorkerY[] workers = new WorkerY[threads];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new WorkerY();
            workers[i].start();
        }
    
        for (WorkerY worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    
        return out;
    }
    
    private class WorkerX extends Thread {
        
        @Override
        public void run() {
            int y;
            while ((y = Y++) < h1) for (int x = 0; x < w1; x++) {
                float point = (x+0.5F) / factor - 0.5F;
                final int rgb;
        
                if (point < 0)
                    rgb = in.get(0, y);
                else if (point > w0-1)
                    rgb = in.get(w0-1, y);
                else {
                    final int
                        x0 = (int) point,
                        x1 = PrimMath.ceil(point);
                    rgb = x0 == x1?
                        in.get(x0, y) :
                        interpolate(in.get(x0, y), in.get(x1, y), point%1);
                }
        
                out.set(x, y, rgb);
            }
        }
    }
    
    private class WorkerY extends Thread {
        
        @Override
        public void run() {
            int x;
            while ((x = X++) < w1) for (int y = 0; y < h1; y++) {
                float point = (y+0.5F) / factor - 0.5F;
                final int rgb;
        
                if (point < 0)
                    rgb = in.get(x, 0);
                else if (point > h0-1)
                    rgb = in.get(x, h0-1);
                else {
                    final int
                        y0 = (int) point,
                        y1 = PrimMath.ceil(point);
            
                    rgb = y0 == y1?
                        in.get(x, y0) :
                        interpolate(in.get(x, y0), in.get(x, y1), point%1);
                }
        
                out.set(x, y, rgb);
            }
        }
    }
    
}
