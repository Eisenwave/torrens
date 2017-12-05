package eisenwave.torrens.util;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TriIntConsumer {
    
    abstract void accept(int x, int y, int z);
    
    default TriIntConsumer andThen(@NotNull TriIntConsumer action) {
        return (x, y, z) -> {this.accept(x, y, z); action.accept(x, y, z);};
    }
    
    
}
