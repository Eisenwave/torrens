package net.grian.torrens.img.ico;

final class IconDirEntry implements Comparable<IconDirEntry> {
    
    public int width, height, palette, planes, bitsPerPixel, data, offset;
    
    public IconDirEntry(int width, int height, int palette, int planes, int bitsPerPixel, int data, int offset) {
        this.width = width;
        this.height = height;
        this.palette = palette;
        this.planes = planes;
        this.bitsPerPixel = bitsPerPixel;
        this.data = data;
        this.offset = offset;
    }
    
    @Override
    public int compareTo(IconDirEntry o) {
        return this.offset - o.offset;
    }
    
    @Override
    public String toString() {
        return IconDirEntry.class.getSimpleName()+
            "{dims="+width+"x"+height+
            ", palette="+palette+
            ", planes="+planes+
            ", bpPixel="+bitsPerPixel+
            ", data="+data+
            ", offset="+offset+"}";
    }
}
