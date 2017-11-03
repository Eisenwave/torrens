package net.grian.torrens.schematic;

import net.grian.spatium.util.Incrementer3;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BlockStructureIterator implements Iterator<BlockKey> {
    
    private final BlockStructure struct;
    private final Incrementer3 incr;
    
    public BlockStructureIterator(BlockStructure struct) {
        this.struct = struct;
        this.incr = new Incrementer3(struct.getSizeX(), struct.getSizeY(), struct.getSizeZ());
        skipToValid();
    }
    
    @Override
    public BlockKey next() {
        int[] xyz = incr.incrementAndGet();
        skipToValid();
        return struct.getBlock(xyz[0], xyz[1], xyz[2]);
    }
    
    @Override
    public boolean hasNext() {
        return incr.canIncrement();
    }
    
    @Override
    public void remove() {
        int[] xyz = incr.get();
        struct.setBlock(xyz[0], xyz[1], xyz[2], 0, (byte) 0);
    }
    
    private void skipToValid() {
        while (hasNext()) {
            if (peek().getId() > 0) break; // next block is air
            else incr.increment();
        }
    }
    
    private BlockKey peek() throws NoSuchElementException {
        int[] xyz = incr.peek();
        return struct.getBlock(xyz[0], xyz[1], xyz[2]);
    }
    
}
