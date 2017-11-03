package net.grian.torrens.img.gif;

import net.grian.spatium.util.FastMath;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class GIFColorTable {
    
    private final int[] data;
    private final int backgroundIndex;
    
    public GIFColorTable(int[] data, int backgroundIndex) {
        if (data.length < 2 || data.length > 256)
            throw new IllegalArgumentException("color table length must be in range(2,256)");
        if (!FastMath.isPower2(data.length))
            throw new IllegalArgumentException("table length must be a power of two: "+data.length);
        if (backgroundIndex > data.length)
            throw new IndexOutOfBoundsException(backgroundIndex+" >= "+data.length);
        
        this.data = data;
        this.backgroundIndex = backgroundIndex;
    }
    
    public int size() {
        return data.length;
    }
    
    public int[] getData() {
        return data;
    }
    
    @Nullable
    public Color getBackgroundColor() {
        return backgroundIndex<0? null : new Color(data[backgroundIndex], true);
    }
    
    /**
     * Returns the background index. If the index is negative, the color table has no background color.
     *
     * @return the background color index
     */
    public int getBackgroundIndex() {
        return backgroundIndex;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName()+
            "{size="+size()+
            ",backIndex="+backgroundIndex+ "}";
    }
}
