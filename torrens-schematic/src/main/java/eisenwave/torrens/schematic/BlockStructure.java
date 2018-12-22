package eisenwave.torrens.schematic;

import eisenwave.nbt.NBTCompound;
import eisenwave.torrens.object.Vertex3i;
import org.jetbrains.annotations.*;

import java.util.*;

public class BlockStructure implements Iterable<StructureBlock> {
    
    public final static int LATEST_VERSION = 1457;
    
    private final int x, y, z, dataVersion;
    
    //private final List<BlockKey> paletteByIndex = new ArrayList<>();
    private final Map<BlockKey, Integer> palette = new HashMap<>();
    private final List<BlockKey> paletteByIndex = new ArrayList<>();
    
    private final Map<Vertex3i, Entry> blocks = new LinkedHashMap<>();
    
    public BlockStructure(int x, int y, int z, int dataVersion) {
        if (x < 1 || y < 1 || z < 1) {
            String error = String.format("Structure dimensions (%d,%d,%d) must be >= 1 on each axis", x, y, z);
            throw new IllegalArgumentException(error);
        }
        if (dataVersion < 0)
            throw new IllegalArgumentException("dataVersion must be positive");
        
        this.x = x;
        this.y = y;
        this.z = z;
        this.dataVersion = dataVersion;
    }
    
    public BlockStructure(int x, int y, int z) {
        this(x, y, z, LATEST_VERSION);
    }
    
    // METADATA GETTERS
    
    public int getSizeX() {
        return x;
    }
    
    public int getSizeY() {
        return y;
    }
    
    public int getSizeZ() {
        return z;
    }
    
    public int getPaletteSize() {
        return paletteByIndex.size();
    }
    
    public int getDataVersion() {
        return dataVersion;
    }
    
    public Vertex3i getSize() {
        return new Vertex3i(x, y, z);
    }
    
    public int size() {
        return blocks.size();
    }
    
    // GETTERS
    
    public List<BlockKey> getPalette() {
        return Collections.unmodifiableList(paletteByIndex);
    }
    
    public StructureBlock getBlock(Vertex3i pos) {
        Entry entry = blocks.get(pos);
        if (entry == null) return null;
        
        BlockKey key = paletteByIndex.get(entry.index);
        return new StructureBlock(pos.getX(), pos.getY(), pos.getZ(), key, entry.nbt);
    }
    
    // MUTATORS
    
    public int addToPalette(BlockKey key) {
        int index = palette.size();
        palette.put(key, index);
        paletteByIndex.add(key);
        return index;
    }
    
    public void addBlock(Vertex3i pos, int paletteIndex, @Nullable NBTCompound nbt) {
        if (paletteIndex < 0 || paletteIndex > paletteByIndex.size())
            throw new IndexOutOfBoundsException(Integer.toString(paletteIndex));
        blocks.put(pos, new Entry(paletteIndex, nbt));
    }
    
    public void addBlock(int x, int y, int z, int paletteIndex, @Nullable NBTCompound nbt) {
        addBlock(new Vertex3i(x, y, z), paletteIndex, nbt);
    }
    
    public void addBlock(Vertex3i pos, @NotNull BlockKey block, @Nullable NBTCompound nbt) {
        Integer index = palette.get(block);
        if (index == null) {
            index = palette.size();
            palette.put(block, index);
            paletteByIndex.add(block);
        }
        
        blocks.put(pos, new Entry(index, nbt));
    }
    
    public void addBlock(int x, int y, int z, @NotNull BlockKey block, @Nullable NBTCompound nbt) {
        addBlock(new Vertex3i(x, y, z), block, nbt);
    }
    
    public void addBlock(StructureBlock block) {
        addBlock(block.getPosition(), block.getKey(), block.getNBT());
    }
    
    public void clear() {
        palette.clear();
        paletteByIndex.clear();
        blocks.clear();
    }
    
    // MISC
    
    @NotNull
    @Override
    public Iterator<StructureBlock> iterator() {
        return new StructureStream();
    }
    
    public BlockStructureStream openStream() {
        return new StructureStream();
    }
    
    public void forEach(StructureConsumer consumer) {
        blocks.forEach((pos, entry) ->
            consumer.accept(new StructureBlock(pos, paletteByIndex.get(entry.index), entry.nbt), entry.index));
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockStructure && equals((BlockStructure) obj);
    }
    
    public boolean equals(BlockStructure structure) {
        if (!structure.getSize().equals(this.getSize()))
            return false;
        
        for (Map.Entry<Vertex3i, Entry> entry : blocks.entrySet()) {
            Entry thisEntry = this.blocks.get(entry.getKey());
            if (thisEntry == null)
                return false;
            
            Entry otherEntry = entry.getValue();
            
            if (!paletteByIndex.get(thisEntry.index).equals(structure.paletteByIndex.get(otherEntry.index)))
                return false;
            
            if (!Objects.equals(thisEntry.nbt, otherEntry.nbt))
                return false;
        }
        
        return true;
    }
    
    // SUBCLASSES
    
    private static class Entry {
        
        private final int /*x, y, z,*/ index;
        @Nullable
        private final NBTCompound nbt;
        
        public Entry(/*int x, int y, int z,*/ int index, @Nullable NBTCompound nbt) {
            // this.x = x;
            // this.y = y;
            // this.z = z;
            this.index = index;
            this.nbt = nbt;
        }
        
    }
    
    private class StructureStream implements BlockStructureStream, Iterator<StructureBlock> {
    
        private final Iterator<Map.Entry<Vertex3i, Entry>> iterator = BlockStructure.this.blocks.entrySet().iterator();
        
        @Override
        public int getSizeX() {
            return BlockStructure.this.x;
        }
        
        @Override
        public int getSizeY() {
            return BlockStructure.this.y;
        }
        
        @Override
        public int getSizeZ() {
            return BlockStructure.this.z;
        }
    
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
    
        @Override
        public StructureBlock next() {
            Map.Entry<Vertex3i, Entry> next = iterator.next();
            Entry value = next.getValue();
            BlockKey block = BlockStructure.this.paletteByIndex.get(value.index);
            return new StructureBlock(next.getKey(), block, value.nbt);
        }
        
    }
    
}
