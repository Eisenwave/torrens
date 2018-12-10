package eisenwave.torrens.schematic.legacy;

import eisenwave.spatium.util.Incrementer3;
import eisenwave.torrens.schematic.BlockKey;
import eisenwave.torrens.schematic.BlockStructureStream;

public class LegacyBlockStructureStream implements BlockStructureStream {
    
    private final LegacyBlockStructure structure;
    private final Incrementer3 incrementer;
    
    public LegacyBlockStructureStream(LegacyBlockStructure structure) {
        this.structure = structure;
        this.incrementer = new Incrementer3(structure.getSizeX(), structure.getSizeY(), structure.getSizeZ());
    }
    
    @Override
    public int getVolume() {
        return structure.getVolume();
    }
    
    @Override
    public int getSizeX() {
        return structure.getSizeX();
    }
    
    @Override
    public int getSizeY() {
        return structure.getSizeY();
    }
    
    @Override
    public int getSizeZ() {
        return structure.getSizeZ();
    }
    
    @Override
    public boolean hasNext() {
        return incrementer.canIncrement();
    }
    
    @Override
    public StructureBlock next() {
        int[] next = incrementer.getAndIncrement();
        LegacyBlockKey legacyKey = structure.getBlock(next[0], next[1], next[2]);
        BlockKey key = MicroLegacyUtil.getByLegacyKey(legacyKey);
        if (key == null)
            throw new IllegalStateException("translation to MC 1.13 key failed for " + legacyKey);
        return new StructureBlock(next[0], next[1], next[2], key);
    }
    
    @Override
    public String toString() {
        return LegacyBlockStructureStream.class.getSimpleName() +
            "{dims=" + getSizeX() +
            "x" + getSizeY() +
            "x" + getSizeZ() + "}";
    }
    
}
