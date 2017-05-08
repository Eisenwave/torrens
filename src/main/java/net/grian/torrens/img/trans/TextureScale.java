package net.grian.torrens.img.trans;

import net.grian.torrens.img.Texture;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class TextureScale {
    
    protected Texture in, out;
    protected int w0, h0, w1, h1;
    
    public TextureScale(Texture in, int w1, int h1) {
        setIn(in);
        this.w1 = w1;
        this.h1 = h1;
    }
    
    /**
     * Returns the input texture.
     *
     * @return the input texture
     */
    @NotNull
    public Texture getIn() {
        return in;
    }
    
    /**
     * Returns the output texture.
     *
     * @return the output texture
     */
    @Nullable
    public Texture getOut() {
        return out;
    }
    
    public void setIn(Texture in) {
        this.w0 = in.getWidth();
        this.h0 = in.getHeight();
        this.in = in;
    }
    
    public void setOut(Texture out) {
        this.w1 = out.getWidth();
        this.h1 = out.getHeight();
        this.out = out;
    }
    
    /**
     * Applies the scaling to the texture in both dimensions.
     *
     * @return the output texture
     */
    public Texture apply() {
        if (w1 < 1) throw new IllegalArgumentException("can not scale to width "+w1);
        if (h1 < 1) throw new IllegalArgumentException("can not scale to height "+h1);
        
        if (w0 == w1) {
            if (h0 == h1) {
                return out = in.clone();
            }
            else
                return applyY();
        }
        
        else if (h0 == h1)
            return applyX();
        
        setIn(applyX());
        return applyY();
    }
    
    public abstract Texture applyX();
    
    public abstract Texture applyY();
    
}
