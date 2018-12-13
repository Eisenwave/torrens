package eisenwave.torrens.schematic;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface BlockStructureStream extends Iterable<StructureBlock> {
    
    default int getVolume() {
        return getSizeX() * getSizeY() * getSizeZ();
    }
    
    abstract int getSizeX();
    
    abstract int getSizeY();
    
    abstract int getSizeZ();
    
    abstract boolean hasNext();
    
    /**
     * Returns the next {@link StructureBlock} in the stream. The coordinates of the block are within between 0,0,0 and
     * the dimensions of the boundaries of the stream.
     *
     * @return the next block in the structure
     */
    abstract StructureBlock next();
    
    @NotNull
    @Override
    default Iterator<StructureBlock> iterator() {
        return new Iterator<StructureBlock>() {
            @Override
            public boolean hasNext() {
                return BlockStructureStream.this.hasNext();
            }
            
            @Override
            public StructureBlock next() {
                return BlockStructureStream.this.next();
            }
        };
    }
    
}
