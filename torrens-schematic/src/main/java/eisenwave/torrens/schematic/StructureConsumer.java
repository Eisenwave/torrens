package eisenwave.torrens.schematic;

@FunctionalInterface
public interface StructureConsumer {
    
    abstract void accept(StructureBlock block, int paletteIndex);
    
}
