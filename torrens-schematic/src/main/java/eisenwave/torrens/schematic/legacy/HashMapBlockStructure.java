package eisenwave.torrens.schematic.legacy;

import eisenwave.torrens.object.Vertex3i;

import java.util.HashMap;
import java.util.function.Consumer;

public class HashMapBlockStructure extends HashMap<Vertex3i, LegacyBlockKey> implements LegacyBlockStructure {
    
    private final int x, y, z;
    
    public HashMapBlockStructure(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    @Override
    public int getId(int x, int y, int z) {
        return getBlock(x, y, z).getId();
    }
    
    @Override
    public byte getData(int x, int y, int z) {
        return getBlock(x, y, z).getData();
    }
    
    @Override
    public LegacyBlockKey getBlock(int x, int y, int z) {
        return get(new Vertex3i(x, y, z));
    }
    
    @Override
    public void setId(int x, int y, int z, int id) {
        setBlock(x, y, z, id, getData(x, y, z));
    }
    
    @Override
    public void setData(int x, int y, int z, byte data) {
        setBlock(x, y, z, getId(x, y, z), data);
    }
    
    @Override
    public void setBlock(int x, int y, int z, int id, byte data) {
        put(new Vertex3i(x, y, z), new LegacyBlockKey(id, data));
    }
    
    @Override
    public int getSizeX() {
        return x;
    }
    
    @Override
    public int getSizeY() {
        return y;
    }
    
    @Override
    public int getSizeZ() {
        return z;
    }
    
    @Override
    public void forEach(Consumer<? super LegacyBlockKey> action) {
        values().forEach(action);
    }
    
}
