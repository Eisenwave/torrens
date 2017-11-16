package net.grian.torrens.util;

import org.jetbrains.annotations.Contract;

import java.nio.charset.StandardCharsets;

public final class FileMagic {
    
    private FileMagic() {}
    
    public final static int
        BMP_FILE_HEADER_LENGTH = 14,
        BMP_INFO_HEADER_LENGTH = 40,
        ICO_HEADER_LENGTH = 6,
        ICO_ICONDIRENTRY_LENGTH = 16;
    
    /**
     * Verifies whether a byte array can be a PNG image.
     *
     * @param bytes the bytes
     * @return whether the file can be a PNG
     */
    @Contract(pure = true)
    public static boolean isPNG(byte[] bytes) {
        return
            (bytes[0]&0xFF) == 0x89 && //high bit to detect systems with no 8-bit support
            bytes[1] == 0x50 && // ASCII P
            bytes[2] == 0x4E && // ASCII N
            bytes[3] == 0x47;   // ASCII G
    }
    
    /**
     * Verifies whether a byte array can be a JPEG image.
     *
     * @param bytes the bytes
     * @return whether the file can be a bitmap
     */
    @Contract(pure = true)
    public static boolean isBitmap(byte[] bytes) {
        return
            bytes[0] == 0x42 &&
            bytes[1] == 0x4D;
    }
    
    /**
     * Verifies whether a byte array can be a BMP image.
     *
     * @param bytes the bytes
     * @return whether the file can be a bitmap
     */
    @Contract(pure = true)
    public static boolean isJPEG(byte[] bytes) {
        return
            (bytes[0]&0xFF) == 0xFF &&
            (bytes[1]&0xFF) == 0xD8;
    }
    
    /**
     * Verifies whether a byte array can be a GIF image.
     *
     * @param bytes the bytes
     * @return whether the file can be a gif
     */
    @Contract(pure = true)
    public static boolean isGIF(byte[] bytes) {
        return
            bytes[0] == 0x47 &&
            bytes[1] == 0x49 &&
            bytes[2] == 0x46;
    }
    
    private final static byte[] MAGIC_VOX = "VOX ".getBytes(StandardCharsets.US_ASCII);
    
    /**
     * Verifies whether a byte array can be a VOX (Magica Voxel) file.
     *
     * @param bytes the bytes
     * @return whether the file can be a gif
     */
    @Contract(pure = true)
    public static boolean isVOX(byte[] bytes) {
        for (int i = 0; i < MAGIC_VOX.length; i++)
            if (MAGIC_VOX[i] != bytes[i]) return false;
        return true;
    }
    
}
