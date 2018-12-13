package eisenwave.torrens.schematic.legacy;

import eisenwave.nbt.*;
import eisenwave.nbt.io.NBTDeserializer;
import eisenwave.torrens.error.FileFormatException;
import eisenwave.torrens.error.FileSyntaxException;
import eisenwave.torrens.error.FileVersionException;
import eisenwave.spatium.array.LowNibbleArray;
import eisenwave.torrens.io.Deserializer;
import eisenwave.torrens.schematic.legacy.ArrayBlockStructure;
import eisenwave.torrens.schematic.legacy.LegacyBlockStructure;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * A parser for <b>Schematic (.schematic)</b> files.
 * <p>
 * These files use the <b>NBT</b> file structure.
 * <p>
 * Only alpha version schematics are supported.
 */
public class DeserializerSchematicBlocks implements Deserializer<LegacyBlockStructure> {
    
    private NBTCompound schematic;
    
    @NotNull
    @Override
    public LegacyBlockStructure fromStream(InputStream stream) throws IOException {
        this.schematic = readSchematic(stream);
        validateSchematic();
        
        final short sizeX = readShort("Width"), sizeY = readShort("Height"), sizeZ = readShort("Length");
        ArrayBlockStructure result = new ArrayBlockStructure(sizeX, sizeY, sizeZ);
        
        short[] blocks = readBlocks();
        byte[] data = readBytes("Data");
        if (blocks.length != data.length)
            throw new FileSyntaxException("block and data array lengths do not match");
        
        for (int x = 0; x < sizeX; ++x)
            for (int y = 0; y < sizeY; ++y)
                for (int z = 0; z < sizeZ; ++z) {
                    final int index = y * sizeX * sizeZ + z * sizeX + x;
                    result.setBlock(x, y, z, blocks[index], data[index]);
                }
        
        return result;
    }
    
    private NBTCompound readSchematic(InputStream stream) throws IOException {
        NBTNamedTag rootTag = new NBTDeserializer().fromStream(stream);
        
        if (rootTag.getTag().getType() != NBTType.COMPOUND)
            throw new IOException("root tag is not a compound");
        
        if (!rootTag.getName().equals("Schematic"))
            throw new FileFormatException("tag 'Schematic' does not exist or is not first");
        
        return (NBTCompound) rootTag.getTag();
    }
    
    private void validateSchematic() throws IOException {
        require("Materials", NBTType.STRING);
        
        if (!schematic.getString("Materials").equals("Alpha"))
            throw new FileVersionException("schematic is not an Alpha schematic");
    }
    
    private short[] readBlocks() throws IOException {
        byte[] baseBlocks = readBytes("Blocks");
        
        return schematic.hasKey("AddBlocks")?
            toBlocks(baseBlocks, readBytes("AddBlocks")) :
            toBlocks(baseBlocks);
    }
    
    private short readShort(String key) throws IOException {
        require(key, NBTType.SHORT);
        
        return schematic.getShort(key);
    }
    
    private byte[] readBytes(String key) throws IOException {
        require(key, NBTType.BYTE_ARRAY);
        
        return schematic.getByteArray(key);
    }
    
    private void require(String key, NBTType type) throws FileSyntaxException {
        if (!schematic.hasKeyOfType(key, type))
            throw new FileSyntaxException(String.format("nbt is missing tag \"%s\" of type %s", key, type));
    }
    
    @Contract(pure = true)
    private static short[] toBlocks(byte[] baseBlocks) {
        short[] result = new short[baseBlocks.length];
        for (int i = 0; i < baseBlocks.length; i++)
            result[i] = (short) (baseBlocks[i] & 0xFF);
        
        return result;
    }
    
    /**
     * Converts the base block array which can encode up to 256 different block ids and a nibble array of additional ids
     * which can encode up to 4096 different blocks (in combination with the first) into a single id array.
     * <p>
     * "AddBlocks" is a nibble (half-byte) array with even indexes in "Blocks" being in the low nibble and odd
     * indexes being in the high nibble.
     *
     * @param baseBlocks the base array of block id's
     * @param addBlocks the array of additional block id's
     * @return a new array containing the full block id
     */
    @NotNull
    @Contract(pure = true)
    private static short[] toBlocks(byte[] baseBlocks, byte[] addBlocks) {
        short[] blocks = new short[baseBlocks.length];
        LowNibbleArray nibbles = new LowNibbleArray(addBlocks.length * 2, addBlocks);
        
        final int lim = Math.min(baseBlocks.length, nibbles.getLength());
        
        for (int i = 0; i < lim; i++)
            blocks[i] = (short) ((nibbles.get(i) << 8) | (baseBlocks[i] & 0xFF));
        
        return blocks;
    }
    
}
