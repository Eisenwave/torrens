package net.grian.torrens.schematic;

import eisenwave.nbt.NBTCompound;
import eisenwave.nbt.NBTNamedTag;
import eisenwave.nbt.io.NBTSerializer;
import net.grian.torrens.error.FileSyntaxException;
import net.grian.torrens.io.Serializer;

import java.io.IOException;
import java.io.OutputStream;

public class SerializerSchematicBlocks implements Serializer<BlockStructure> {
    
    private NBTCompound schematic = new NBTCompound();
    
    @SuppressWarnings("FieldCanBeLocal")
    private final boolean addBlocks; //TODO AddBlocks implementation
    
    public SerializerSchematicBlocks(boolean addBlocks) {
        this.addBlocks = addBlocks;
    }
    
    public SerializerSchematicBlocks() {
        this(false);
    }
    
    @Override
    public void toStream(BlockStructure blocks, OutputStream stream) throws IOException {
        writeDims(blocks);
        schematic.putString("Materials", "Alpha");
        writeBlocks(blocks);
        
        new NBTSerializer().toStream(new NBTNamedTag("Schematic", schematic), stream);
    }
    
    private void writeDims(BlockStructure blocks) throws FileSyntaxException {
        final short
            width = (short) blocks.getSizeX(),
            height = (short) blocks.getSizeY(),
            length = (short) blocks.getSizeZ();
        
        if (width < 0)
            throw new FileSyntaxException("width too large (" + blocks.getSizeX() + ")");
        if (height < 0)
            throw new FileSyntaxException("height too large (" + blocks.getSizeY() + ")");
        if (length < 0)
            throw new FileSyntaxException("length too large (" + blocks.getSizeZ() + ")");
        
        schematic.putShort("Width", width);
        schematic.putShort("Height", height);
        schematic.putShort("Length", length);
    }
    
    private void writeBlocks(BlockStructure blocks) {
        int width = blocks.getSizeX(),
            length = blocks.getSizeZ();
        
        byte[] ids = new byte[blocks.getVolume()];
        byte[] data = new byte[blocks.getVolume()];
        
        blocks.forEachPos((x, y, z) -> {
            int index = (y * length + z) * width + x;
            ids[index] = (byte) blocks.getId(x, y, z);
            data[index] = blocks.getData(x, y, z);
        });
        
        schematic.putByteArray("Blocks", ids);
        schematic.putByteArray("Data", data);
    }
    
}
