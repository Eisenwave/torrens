package net.grian.torrens.object;

public class IntBoundingBox {
    
    private final int minX, minY, minZ, maxX, maxY, maxZ;
    
    public IntBoundingBox(int x0, int y0, int z0, int x1, int y1, int z1) {
        this.minX = Math.min(x0, x1);
        this.minY = Math.min(y0, y1);
        this.minZ = Math.min(z0, z1);
        this.maxX = Math.max(x0, x1);
        this.maxY = Math.max(y0, y1);
        this.maxZ = Math.max(z0, z1);
    }
    
    public int getMinX() {
        return minX;
    }
    
    public int getMinY() {
        return minY;
    }
    
    public int getMinZ() {
        return minZ;
    }
    
    public int getMaxX() {
        return maxX;
    }
    
    public int getMaxY() {
        return maxY;
    }
    
    public int getMaxZ() {
        return maxZ;
    }
    
}
