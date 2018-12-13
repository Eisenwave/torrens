package eisenwave.torrens.schematic;

import eisenwave.nbt.NBTCompound;
import eisenwave.torrens.object.Vertex3i;
import eisenwave.torrens.schematic.BlockKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StructureBlock {
    
    private final int x, y, z;
    @NotNull
    private final BlockKey key;
    @Nullable
    private final NBTCompound nbt;
    
    public StructureBlock(int x, int y, int z, @NotNull BlockKey key, @Nullable NBTCompound nbt) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.key = key;
        this.nbt = nbt;
    }
    
    public StructureBlock(Vertex3i pos, @NotNull BlockKey key, @Nullable NBTCompound nbt) {
        this(pos.getX(), pos.getY(), pos.getZ(), key, nbt);
    }
    
    public StructureBlock(int x, int y, int z, @NotNull BlockKey key) {
        this(x, y, z, key, null);
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
    
    @NotNull
    public Vertex3i getPosition() {
        return new Vertex3i(x, y, z);
    }
    
    @NotNull
    public BlockKey getKey() {
        return key;
    }
    
    @Nullable
    public NBTCompound getNBT() {
        return nbt;
    }
    
}
