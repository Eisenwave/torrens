package eisenwave.torrens.img.gif;

import eisenwave.torrens.img.Texture;
import org.jetbrains.annotations.NotNull;

public class GIFFrame {
    
    public final static int MAX_DELAY = 655350;
    
    private final Texture data;
    private final int x, y, w, h, delay;
    private final GIFDisposal disposal;
    
    public GIFFrame(Texture data, int x, int y, int delay, GIFDisposal disposal) {
        if (disposal == null) throw new IllegalArgumentException("disposal method must not be null");
        if (delay < 0 || delay > MAX_DELAY)
            throw new IllegalArgumentException("delay must be in range(0," + MAX_DELAY + ")");
        
        this.x = x;
        this.y = y;
        this.w = data.getWidth();
        this.h = data.getHeight();
        
        this.data = data;
        this.delay = delay;
        this.disposal = disposal;
    }
    
    public GIFFrame(GIFFrame copyOf, Texture data) {
        this(data, copyOf.x, copyOf.y, copyOf.delay, copyOf.disposal);
    }
    
    /**
     * Returns the ARGB data of the frame.
     *
     * @return the ARGB data
     */
    @NotNull
    public Texture getData() {
        return data;
    }
    
    /**
     * Returns the x-coordinate of the frame in the image.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns the y-coordinate of the frame in the image.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }
    
    /**
     * Returns the frame width.
     *
     * @return the frame width
     */
    public int getWidth() {
        return w;
    }
    
    /**
     * Returns the frame height.
     *
     * @return the frame height
     */
    public int getHeight() {
        return h;
    }
    
    /**
     * Returns the frame delay time in milliseconds.
     *
     * @return the frame delay
     */
    public int getDelayMillis() {
        return delay;
    }
    
    /**
     * Returns whether the frame requires user input to continue.
     *
     * @return whether the frame requires user input
     */
    public boolean hasUserInput() {
        return false;
    }
    
    /**
     * Returns whether the frame has transparency.
     *
     * @return whether the frame has transparency
     */
    public boolean hasTransparency() {
        return false;
    }
    
    /**
     * Returns the disposal method of the frame.
     *
     * @return the disposal method
     */
    @NotNull
    public GIFDisposal getDisposalMethod() {
        return disposal;
    }
    
    @Override
    public String toString() {
        return GIFFrame.class.getSimpleName() +
            "{width=" + getWidth() +
            ",height=" + getHeight() +
            ",delay=" + delay +
            ",disposal=" + disposal
            + "}";
    }
}
