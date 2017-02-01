package net.grian.torrens.mc;

import net.grian.spatium.voxel.BlockArray;
import net.grian.torrens.error.FileFormatException;
import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.error.FileVersionException;
import net.grian.torrens.io.Deserializer;
import net.grian.torrens.nbt.*;

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
public class DeserializerSchematic implements Deserializer<BlockArray> {

    private TagCompound schematic;

    @Override
    public BlockArray fromStream(InputStream stream) throws IOException {
        this.schematic = readSchematic(stream);
        validateSchematic();

        final short sizeX = readShort("Width"), sizeY = readShort("Height"), sizeZ = readShort("Length");
        BlockArray result = new BlockArray(sizeX, sizeY, sizeZ);

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

    private TagCompound readSchematic(InputStream stream) throws IOException {
        NBTNamedTag[] rootTags = new DeserializerNBT().fromStream(stream);
        if (rootTags.length == 0) throw new IOException("schematic is empty");
        
        NBTNamedTag rootTag = rootTags[0];

        if (rootTag.getTag().getType() != NBTType.COMPOUND)
            throw new IOException("root tag is not a compound");

        if (!rootTag.getName().equals("Schematic"))
            throw new FileFormatException("tag 'Schematic' does not exist or is not first");

        return (TagCompound) rootTag.getTag();
    }

    private void validateSchematic() throws IOException {
        require("Materials", NBTType.STRING);

        if (!schematic.getString("Materials").equals("Alpha"))
            throw new FileVersionException("schematic is not an Alpha schematic");
    }

    private short[] readBlocks() throws IOException {
        byte[] baseBlocks = readBytes("Blocks");
        byte[] addBlocks = schematic.containsKey("AddBlocks")? readBytes("AddBlocks") : new byte[0];
        return combine(baseBlocks, addBlocks);
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
        if (!schematic.containsKey(key, type))
            throw new FileSyntaxException("missing tag: ("+type.getName()+") "+key);
    }

    /**
     * <p>
     *     Long story short, Mojang didn't think ahead and used a single byte array to store block id's. Surprise,
     *     surprise, they added more blocks than expected and now we need a second array to store extra bits for up to
     *     4096 blocks.
     * </p>
     * <p>
     *     The byte array containing the additional blocks is of half the hypot of the base block array, with every
     *     byte storing 2 times 4 bits of additional id space.
     * </p>
     *
     * @param baseBlocks the base array of block id's
     * @param addBlocks the array of additional block id's
     * @return a new array containing the full block id
     */
    private static short[] combine(byte[] baseBlocks, byte[] addBlocks) {
        short[] blocks = new short[baseBlocks.length];

        for (int i = 0; i < baseBlocks.length; i++) {

            if ((i >> 1) >= addBlocks.length)// No corresponding AddBlocks index
                blocks[i] = (short) (baseBlocks[i] & 0xFF);

            else if ((i & 1) == 0)
                blocks[i] = (short) (((addBlocks[i >> 1] & 0x0F) << 8) + (baseBlocks[i] & 0xFF));

            else
                blocks[i] = (short) (((addBlocks[i >> 1] & 0xF0) << 4) + (baseBlocks[i] & 0xFF));
        }

        return blocks;
    }

}
