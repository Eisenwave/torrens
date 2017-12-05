package eisenwave.torrens.object;

public class Rectangle4i {
    
    private final int minX, minY, maxX, maxY;
    
    public Rectangle4i(int x0, int y0, int x1, int y1) {
        this.minX = Math.min(x0, x1);
        this.minY = Math.min(y0, y1);
        this.maxX = Math.max(x0, x1);
        this.maxY = Math.max(y0, y1);
    }
    
    // GETTERS
    
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
    
    // PREDICATES
    
    public boolean equals(Rectangle4i rectangle) {
        return this.minX == rectangle.minX && this.minY == rectangle.minY
            && this.maxX == rectangle.maxX && this.maxY == rectangle.maxY;
    }
    
    public boolean isSingularity() {
        return minX == maxX && minY == maxY;
    }
    
    // OPERATIONS
    
    public Rectangle4i translate(int x, int y) {
        return new Rectangle4i(minX+x, minY+y, maxX+x, maxY+y);
    }
    
    // MISC
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Rectangle4i && equals((Rectangle4i) obj);
    }
    
    @Override
    public String toString() {
        return String.format("[%d, %d, %d, %d]", minX, minY, maxX, maxY);
    }
    
}
