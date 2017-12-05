package eisenwave.torrens.img.gif;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

@Deprecated
public class GIFAnimation extends ArrayList<GIFFrame> {
    
    private final GIFHeader header;
    private boolean loop;
    private GIFColorTable table;
    
    public GIFAnimation(GIFHeader header) {
        super(1);
        this.header = Objects.requireNonNull(header);
        setLoop(true);
        setColorTable(table);
    }
    
    public GIFAnimation(GIFHeader header, Collection<GIFFrame> frames) {
        super(frames);
        this.header = Objects.requireNonNull(header);
        setLoop(true);
        setColorTable(table);
    }
    
    public GIFAnimation(int width, int height) {
        this(new GIFHeader(width, height, null));
    }
    
    public GIFAnimation(GIFAnimation copyOf, int width, int height) {
        this.header = copyOf.header;
        setLoop(copyOf.loop);
        setColorTable(copyOf.table);
    }
    
    public GIFAnimation(GIFAnimation copyOf) {
        this(copyOf, copyOf.getWidth(), copyOf.getHeight());
    }
    
    // GETTERS
    
    @Override
    public boolean add(GIFFrame gifFrame) {
        return
            gifFrame.getWidth() <= getWidth() &&
            gifFrame.getHeight() <= getHeight() &&
            super.add(gifFrame);
    }
    
    public int getWidth() {
        return header.getWidth();
    }
    
    public int getHeight() {
        return header.getHeight();
    }
    
    public boolean isLoop() {
        return loop;
    }
    
    @Nullable
    public GIFColorTable getColorTable() {
        return table;
    }
    
    // SETTERS
    
    public void setLoop(boolean loop) {
        this.loop = loop;
    }
    
    public void setColorTable(@Nullable GIFColorTable table) {
        this.table = table;
    }
    
    // MISC
    
    @Override
    public GIFFrame[] toArray() {
        return toArray(new GIFFrame[size()]);
    }
    
    @SuppressWarnings("StringBufferReplaceableByString")
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName());
        builder
            .append("{width=")
            .append(getWidth())
            .append(" ,height=")
            .append(getHeight())
            .append(", table=")
            .append(table)
            .append(", frames=")
            .append(Arrays.toString(toArray()));
        
        return builder.toString();
    }
}
