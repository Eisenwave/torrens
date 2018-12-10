package eisenwave.torrens.schematic.legacy;

import eisenwave.torrens.object.Vertex3i;
import eisenwave.torrens.schematic.BlockKey;
import org.jetbrains.annotations.NotNull;

public class StructureBlock {
    
    private final int x, y, z;
    @NotNull
    private final BlockKey key;
    
    public StructureBlock(int x, int y, int z, @NotNull BlockKey key) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.key = key;
    }
    
    public boolean isAir() {
        return key.getNameSpace().equals("minecraft") && key.getId().equals("air");
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    public Vertex3i getPosition() {
        return new Vertex3i(x, y, z);
    }
    
    @NotNull
    public BlockKey getKey() {
        return key;
    }
    
}
