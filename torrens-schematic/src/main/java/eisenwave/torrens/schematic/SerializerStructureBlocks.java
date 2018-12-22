package eisenwave.torrens.schematic;

import eisenwave.io.Serializer;
import eisenwave.nbt.*;
import eisenwave.nbt.io.NBTSerializer;
import eisenwave.torrens.object.Vertex3i;
import org.jetbrains.annotations.Nullable;

import java.io.*;

public class SerializerStructureBlocks implements Serializer<BlockStructure> {
    
    private final String author;
    private final NBTSerializer serializer;
    
    public SerializerStructureBlocks(@Nullable String author, boolean compression) {
        this.author = author;
        this.serializer = new NBTSerializer(compression);
    }
    
    public SerializerStructureBlocks() {
        this(null, true);
    }
    
    @Override
    public void toStream(BlockStructure structure, OutputStream stream) throws IOException {
        serializer.toStream(serialize(structure), stream);
    }
    
    @Override
    public void toFile(BlockStructure structure, File file) throws IOException {
        serializer.toFile(serialize(structure), file);
    }
    
    @Override
    public byte[] toBytes(BlockStructure structure, int capacity) throws IOException {
        return serializer.toBytes(serialize(structure), capacity);
    }
    
    @Override
    public byte[] toBytes(BlockStructure structure) throws IOException {
        return serializer.toBytes(serialize(structure));
    }
    
    public String toMSONString(BlockStructure structure) {
        return serialize(structure).getTag().toMSONString();
    }
    
    private NBTNamedTag serialize(BlockStructure structure) {
        NBTCompound root = new NBTCompound();
        
        root.putInt("DataVersion", structure.getDataVersion());
        root.putString("author", author != null? author : "VoxelVert");
        root.put("size", serializeIntTriple(structure.getSize()));
        root.put("palette", serializePalette(structure));
        root.put("blocks", serializeBlocks(structure));
        
        return new NBTNamedTag("", root);
    }
    
    private static NBTList serializePalette(BlockStructure structure) {
        NBTList palette = new NBTList(NBTType.COMPOUND);
        for (BlockKey key : structure.getPalette()) {
            NBTCompound compound = new NBTCompound();
            palette.add(compound);
            compound.putString("Name", key.getNameSpace() + ':' + key.getId());
            
            if (!key.getBlockState().isEmpty()) {
                NBTCompound properties = new NBTCompound();
                compound.put("Properties", properties);
                key.getBlockState().forEach(properties::putString);
            }
        }
        return palette;
    }
    
    private static NBTList serializeBlocks(BlockStructure structure) {
        NBTList blocks = new NBTList(NBTType.COMPOUND);
        structure.forEach((block, index) -> {
            NBTCompound compound = new NBTCompound();
            blocks.add(compound);
            compound.putInt("state", index);
            compound.put("pos", serializeIntTriple(block.getPosition()));
            NBTCompound nbt = block.getNBT();
            if (nbt != null)
                compound.put("nbt", nbt);
        });
        return blocks;
    }
    
    private static NBTList serializeIntTriple(Vertex3i v) {
        return new NBTList(NBTType.INT,
            new NBTInt(v.getX()),
            new NBTInt(v.getY()),
            new NBTInt(v.getZ()));
    }
    
}
