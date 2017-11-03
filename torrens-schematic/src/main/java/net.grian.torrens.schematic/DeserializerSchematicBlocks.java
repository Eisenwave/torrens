package net.grian.torrens.schematic;

import net.grian.spatium.array.LowNibbleArray;
import net.grian.torrens.error.FileFormatException;
import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.error.FileVersionException;
import net.grian.torrens.io.Deserializer;
import net.grian.torrens.nbt.io.DeserializerNBT;
import net.grian.torrens.nbt.NBTNamedTag;
import net.grian.torrens.nbt.NBTType;
import net.grian.torrens.nbt.NBTCompound;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 *     A parser for <b>Schematic (.schematic)</b> files.
 * </p>
 * <p>
 *     These files use the <b>NBT</b> file structure.
 * </p>
 * <p>
 *     Only alpha version schematics are supported.
 * </p>
 */
public class DeserializerSchematicBlocks implements Deserializer<BlockStructure> {

    private NBTCompound schematic;

    @NotNull
    @Override
    public ArrayBlockStructure fromStream(InputStream stream) throws IOException {
        this.schematic = readSchematic(stream);
        validateSchematic();

        final short sizeX = readShort("Width"), sizeY = readShort("Height"), sizeZ = readShort("Length");
        ArrayBlockStructure result = new ArrayBlockStructure(sizeX, sizeY, sizeZ);

        short[] blocks = readBlocks();
        byte[] data = readBytes("Data");
        if (blocks.length != data.length)
            throw new FileSyntaxException("block and data array lengths do not match");

        for (int x = 0; x < sizeX; ++x) for (int y = 0; y < sizeY; ++y) for (int z = 0; z < sizeZ; ++z) {
            final int index = y*sizeX*sizeZ + z*sizeX + x;
            result.setBlock(x, y, z, blocks[index], data[index]);
        }

        return result;
    }

    private NBTCompound readSchematic(InputStream stream) throws IOException {
        NBTNamedTag rootTag = new DeserializerNBT().fromStream(stream);

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
     * <p>
     *     Long story short, Mojang didn't think ahead and used a single byte array to store block id's. Surprise,
     *     surprise, they added more blocks than expected and now we need a second array to store extra bits for up to
     *     4096 blocks.
     * </p>
     * <p>
     *     "AddBlocks" is a nibble (half-byte) array with even indexes in "Blocks" being in the low nibble and odd
     *     indexes being in the high nibble.
     * </p>
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
