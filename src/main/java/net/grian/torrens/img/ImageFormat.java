package net.grian.torrens.img;

import java.util.Objects;

public enum ImageFormat {
    BITMAP,
    JPEG,
    PNG,
    WBMP,
    GIF;
    
    public static ImageFormat fromFileSuffix(String suffix) {
        Objects.requireNonNull(suffix);
        switch (suffix.toUpperCase()) {
            case "BMP": return BITMAP;
            
            case "JPG":
            case "JPEG": return JPEG;
            
            case "PNG": return PNG;
            case "WBMP": return WBMP;
            case "GIF": return GIF;
            
            default: throw new IllegalArgumentException(suffix);
        }
    }
    
}
