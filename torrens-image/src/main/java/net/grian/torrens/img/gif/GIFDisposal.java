package net.grian.torrens.util.img.gif;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public enum GIFDisposal {
    NONE("none"),
    TO_PREVIOUS("restoreToPrevious"),
    TO_BACKGROUND("restoreToBackground");
    
    private final String name;
    
    GIFDisposal(String name) {
        this.name = name;
    }
    
    @NotNull
    public String toString() {
        return name;
    }
    
    @Contract(pure = true)
    @NotNull
    public static GIFDisposal fromName(String name) {
        switch (name) {
            case "restoreToPrevious": return TO_PREVIOUS;
            case "restoreToBackground": return TO_BACKGROUND;
            default: return NONE;
        }
    }
    
}
