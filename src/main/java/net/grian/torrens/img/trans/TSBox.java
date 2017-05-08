package net.grian.torrens.img.trans;

import net.grian.torrens.img.Texture;

@SuppressWarnings("Duplicates")
public class TSBox extends TextureScale {
    
    private final int threads;
    
    private volatile int X, Y;
    private Texture out;
    
    public TSBox(Texture in, int w1, int h1, int threads) {
        super(in, w1, h1);
        if (threads < 1) throw new IllegalArgumentException("at least one thread required");
        this.threads = threads;
    }
    
    public TSBox(Texture in, int w1, int h1) {
        this(in, w1, h1, 1);
    }
    
    @Override
    public void setIn(Texture in) {
        super.setIn(in);
        this.X = 0;
        this.Y = 0;
    }
    
    @Override
    public Texture getOut() {
        return out;
    }
    
    @Override
    public Texture applyX() {
        if (w0 == w1)
            return out = in.clone();
        
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
            int x;
            while ((x = X++) < w1) {
                final int
                    minX = x * w0 / w1,
                    maxX = (x+1) * w0 / w1 - 1;
                
                for (int y = 0; y < h1; y++) {
                    final int rgb = minX==maxX?
                        in.get(minX, y) :
                        in.averageRGB(minX, y, maxX, y, true);
                    out.set(x, y, rgb);
                }
            }
        }
    }
    
    private class WorkerY extends Thread {
        
        @Override
        public void run() {
            int y;
            while ((y = Y++) < h1){
                final int
                    minY = y * h0 / h1,
                    maxY = (y+1) * h0 / h1 - 1;
        
                for (int x = 0; x < w0; x++) {
                    final int rgb = minY==maxY?
                        in.get(x, minY) :
                        in.averageRGB(x, minY, x, maxY, true);
            
                    out.set(x, y, rgb);
                }
            }
        }
    }
    
}