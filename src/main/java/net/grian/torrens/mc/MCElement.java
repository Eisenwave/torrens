package net.grian.torrens.mc;

import net.grian.spatium.enums.Direction;
import net.grian.spatium.geo3.AxisAlignedBB3;

/**
 * A Minecraft model element.
 */
public class MCElement {

    private AxisAlignedBB3 shape;
    private final MCUV[] uv = new MCUV[Direction.values().length];

    /**
     * Constructs a new element with a given shape.
     *
     * @param shape the element shape
     */
    public MCElement(AxisAlignedBB3 shape) {
        setShape(shape);
    }

    /**
     * Returns the uv of the bounding of a certain side.
     *
     * @param d the direction
     * @return the tile or null if the element has no texture on that side
     */
    public MCUV getUV(Direction d) {
        return uv[d.ordinal()];
    }

    /**
     * Returns the shape of the element (without rotation).
     *
     * @return the element shape
     */
    public AxisAlignedBB3 getShape() {
        return shape;
    }

    //CHECKERS

    /**
     * Returns whether the element has any visible side and is thus visible.
     *
     * @return whether the element is visible
     */
    public boolean isVisible() {
        for (MCUV entry : uv)
            if (entry != null) return true;
        return false;
    }

    /**
     * Returns whether the box has uv on a given side.
     *
     * @param side the side of the element
     * @return whether a side of the element is enabled
     */
    public boolean hasUV(Direction side) {
        return uv[side.ordinal()] != null;
    }

    //SETTERS

    /**
     * Changes the shape of this element.
     *
     * @param shape the shape
     */
    public void setShape(AxisAlignedBB3 shape) {
        this.shape = shape.clone();
    }

    /**
     * Removes the UV on one side of the element.
     *
     * @param side the the side to be enabled
     */
    @Deprecated
    public void removeUV(Direction side) {
        uv[side.ordinal()] = null;
    }

    /**
     * Sets the uv of a side of the element.
     *
     * @param side the side
     * @param uv the uv
     */
    public void setUV(Direction side, MCUV uv) {
        this.uv[side.ordinal()] = uv;
    }

}