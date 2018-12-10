package eisenwave.torrens.schematic.legacy;

import eisenwave.spatium.util.Incrementer3;

import java.util.Iterator;

public class BlockStructureIterator implements Iterator<LegacyBlockKey> {
    
    private final LegacyBlockStructure struct;
    private final Incrementer3 incr;
    
    public BlockStructureIterator(LegacyBlockStructure struct) {
        this.struct = struct;
        this.incr = new Incrementer3(struct.getSizeX(), struct.getSizeY(), struct.getSizeZ());
        skipToValid();
    }
    
    @Override
    public LegacyBlockKey next() {
        int[] xyz = incr.get();
        skipToValid();
        return get(xyz);
    }
    
    @Override
    public boolean hasNext() {
        return incr.canIncrement();
    }
    
    private void skipToValid() {
        incr.increment();
        //noinspection StatementWithEmptyBody
        while (hasNext() && get(incr.getAndIncrement()).getId() > 0) {}
    }
    
    private LegacyBlockKey get(int[] xyz) {
        return struct.getBlock(xyz[0], xyz[1], xyz[2]);
    }
    
}
