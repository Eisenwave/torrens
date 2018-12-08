package eisenwave.torrens.voxel;

import eisenwave.torrens.object.Vertex2i;

public interface BitArray2 {
    
    /**
     * Returns the dimension on the x-axis.
     *
     * @return the dimension on the x-axis
     */
    abstract int getSizeX();
    
    /**
     * Returns the dimension on the y-axis.
     *
     * @return the dimension on the y-axis
     */
    abstract int getSizeY();
    
    /**
     * Returns the area. This is equivalent to the product of all dimensions.
     *
     * @return the area
     */
    default int getArea() {
        return getSizeX() * getSizeY();
    }
    
    /**
     * Returns the dimensions on x, y and z axis.
     *
     * @return the dimensions
     */
    default Vertex2i getDimensions() {
        return new Vertex2i(getSizeX(), getSizeY());
    }
    
    /**
     * Checks whether the field contains an element at the given position.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return whether there is an element
     */
    abstract boolean contains(int x, int y);
    
    /**
     * Checks whether the array contains an element at the given position.
     *
     * @param pos the position
     * @return whether there is an element
     */
    default boolean contains(Vertex2i pos) {
        return contains(pos.getX(), pos.getY());
    }
    
    /**
     * Returns the total amount of elements in this array.
     *
     * @return the total amount of elements in this array
     */
    default int size() {
        final int limX = getSizeX(), limY = getSizeY();
        int count = 0;
        
        for (int x = 0; x < limX; x++)
            for (int y = 0; y < limY; y++)
                if (contains(x, y))
                    count++;
        
        return count;
    }
    
}
