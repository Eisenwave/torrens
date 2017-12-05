package eisenwave.torrens.object;

public class BoundingBox6f {
    
    private final float minX, minY, minZ, maxX, maxY, maxZ;
    
    public BoundingBox6f(float x0, float y0, float z0, float x1, float y1, float z1) {
        this.minX = Math.min(x0, x1);
        this.minY = Math.min(y0, y1);
        this.minZ = Math.min(z0, z1);
        this.maxX = Math.max(x0, x1);
        this.maxY = Math.max(y0, y1);
        this.maxZ = Math.max(z0, z1);
    }
    
    public BoundingBox6f(BoundingBox6i box) {
        this.minX = box.getMinX();
        this.minY = box.getMinY();
        this.minZ = box.getMinZ();
        this.maxX = box.getMaxX()+1;
        this.maxY = box.getMaxY()+1;
        this.maxZ = box.getMaxZ()+1;
    }
    
    // GETTERS
    
    public float getMinX() {
        return minX;
    }
    
    public float getMinY() {
        return minY;
    }
    
    public float getMinZ() {
        return minZ;
    }
    
    public float getMaxX() {
        return maxX;
    }
    
    public float getMaxY() {
        return maxY;
    }
    
    public float getMaxZ() {
        return maxZ;
    }
    
    public Vertex3f getMin() {
        return new Vertex3f(minX, minY, minZ);
    }
    
    public Vertex3f getMax() {
        return new Vertex3f(maxX, maxY, maxZ);
    }
    
    public Vertex3f getCenter() {
        return getMin().midPoint(getMax());
    }
    
    public float getSizeX() {
        return maxX - minX;
    }
    
    public float getSizeY() {
        return maxY - minY;
    }
    
    public float getSizeZ() {
        return maxZ - minZ;
    }
    
    public float getVolume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }
    
    // PREDICATES
    
    public boolean contains(float x, float y, float z) {
        return
            x >= minX && x <= maxX &&
            y >= minY && y <= maxY &&
            z >= minZ && z <= maxZ;
    }
    
    public boolean contains(BoundingBox6f box) {
        return
            box.minX >= minX && box.maxX <= maxX &&
            box.minY >= minY && box.maxY <= maxY &&
            box.minX >= minZ && box.maxZ <= maxZ;
    }
    
    // OPERATIONS
    
    public BoundingBox6f scale(float x, float y, float z) {
        return new BoundingBox6f(
            minX*x, minY*y, minZ*z,
            maxX*x, maxY*y, maxZ*z);
    }
    
    public BoundingBox6f scale(float factor) {
        return scale(factor, factor, factor);
    }
    
    public BoundingBox6f translate(float x, float y, float z) {
        return new BoundingBox6f(
            minX+x, minY+y, minZ+z,
            maxX+x, maxY+y, maxZ+z);
    }
    
    // MISC
    
    @Override
    public String toString() {
        return "["+minX+","+minY+","+minZ+","+maxX+","+maxY+","+maxZ+"]";
    }
    
}
