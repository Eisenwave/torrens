package net.grian.torrens.object;

import net.grian.torrens.util.TriIntConsumer;

import java.util.function.Consumer;

public class BoundingBox6i {
    
    private final int minX, minY, minZ, maxX, maxY, maxZ;
    
    public BoundingBox6i(int x0, int y0, int z0, int x1, int y1, int z1) {
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
    
    public Vertex3i getMin() {
        return new Vertex3i(minX, minY, minZ);
    }
    
    public Vertex3i getMax() {
        return new Vertex3i(maxX, maxY, maxZ);
    }
    
    public int getSizeX() {
        return maxX - minX + 1;
    }
    
    public int getSizeY() {
        return maxY - minY + 1;
    }
    
    public int getSizeZ() {
        return maxZ - minZ + 1;
    }
    
    public int getVolume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }
    
    // PREDICATES
    
    public boolean equals(BoundingBox6i box) {
        return minX == box.minX && maxX == box.maxX
            && minY == box.minY && maxY == box.maxY
            && minZ == box.minZ && maxZ == box.maxZ;
    }
    
    public boolean contains(int x, int y, int z) {
        return
            x >= minX && x <= maxX &&
            y >= minY && y <= maxY &&
            z >= minZ && z <= maxZ;
    }
    
    public boolean contains(Vertex3i v) {
        return contains(v.getX(), v.getY(), v.getZ());
    }
    
    public boolean contains(BoundingBox6i box) {
        return
            box.minX >= this.minX && box.maxX <= this.maxX &&
            box.minY >= this.minY && box.maxY <= this.maxY &&
            box.minZ >= this.minZ && box.maxZ <= this.maxZ;
    }
    
    public boolean isSingularity() {
        return minX == maxX && minY == maxY && minZ == maxZ;
    }
    
    // OPERATIONS
    
    public BoundingBox6i scale(int x, int y, int z) {
        return new BoundingBox6i(
            minX*x, minY*y, minZ*z,
            maxX*x, maxY*y, maxZ*z);
    }
    
    public BoundingBox6i translate(int x, int y, int z) {
        return new BoundingBox6i(
            minX+x, minY+y, minZ+z,
            maxX+x, maxY+y, maxZ+z);
    }
    
    // ITERATION
    
    public void forEach(TriIntConsumer consumer) {
        for (int x = minX; x <= maxX; x++)
            for (int y = minY; y <= maxY; y++)
                for (int z = minZ; z <= maxZ; z++)
                    consumer.accept(x, y, z);
    }
    
    public void forEach(Consumer<Vertex3i> consumer) {
        forEach((x,y,z) -> consumer.accept(new Vertex3i(x,y,z)));
    }
    
    // MISC
    
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof BoundingBox6i && equals((BoundingBox6i) obj);
    }
    
    @Override
    public String toString() {
        return "["+minX+","+minY+","+minZ+","+maxX+","+maxY+","+maxZ+"]";
    }
    
}
