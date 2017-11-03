package net.grian.torrens.img.ico;

final class ICODirEntry implements Comparable<ICODirEntry> {
    
    public int width, height, palette, planes, bitsPerPixel, data, offset;
    
    /**
     * Constructs a new ICODirEntry.
     *
     * @param width the width of the image
     * @param height the height of the image
     * @param palette the size of the image palette
     * @param planes the amount of planes of the image
     * @param bitsPerPixel the amount of bits per pixel
     * @param data the amount of bytes of image data
     * @param offset the offset in bytes of the image data in the file
     */
    public ICODirEntry(int width, int height, int palette, int planes, int bitsPerPixel, int data, int offset) {
        this.width = width;
        this.height = height;
        this.palette = palette;
        this.planes = planes;
        this.bitsPerPixel = bitsPerPixel;
        this.data = data;
        this.offset = offset;
    }
    
    @Override
    public int compareTo(ICODirEntry o) {
        return this.offset - o.offset;
    }
    
    @Override
    public String toString() {
        return ICODirEntry.class.getSimpleName()+
            "{dims="+width+"x"+height+
            ", palette="+palette+
            ", planes="+planes+
            ", bpPixel="+bitsPerPixel+
            ", data="+data+
            ", offset="+offset+"}";
    }
}
