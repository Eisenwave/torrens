package net.grian.torrens.util.object;

public class Rectangle4i {
    
    private final int minX, minY, maxX, maxY;
    
    public Rectangle4i(int x0, int y0, int x1, int y1) {
        this.minX = Math.min(x0, x1);
        this.minY = Math.min(y0, y1);
        this.maxX = Math.max(x0, x1);
        this.maxY = Math.max(y0, y1);
    }
    
    public int getMinX() {
        return minX;
    }
    
    public int getMinY() {
        return minY;
    }
    
    public int getMaxX() {
        return maxX;
    }
    
    public int getMaxY() {
        return maxY;
    }
    
    public int getWidth() {
        return maxX - minX + 1;
    }
    
    public int getHeight() {
        return maxY - minY + 1;
    }
    
}
