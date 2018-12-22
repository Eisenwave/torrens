package eisenwave.torrens.schematic;

import eisenwave.nbt.*;
import eisenwave.nbt.io.NBTDeserializer;
import eisenwave.torrens.error.FileSyntaxException;
import eisenwave.torrens.error.FileVersionException;
import eisenwave.torrens.io.Deserializer;
import eisenwave.torrens.object.Vertex3i;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class DeserializerStructureBlocks implements Deserializer<BlockStructure> {
    
    @NotNull
    @Override
    public BlockStructure fromStream(InputStream stream) throws IOException {
        NBTCompound root = (NBTCompound) new NBTDeserializer(true).fromStream(stream).getTag();
        
        final int dataVersion = validateVersion(root);
        final NBTList nbtSize = root.getTagList("size");
        final NBTList nbtPalette = root.getTagList("palette");
        final NBTList blocks = root.getTagList("blocks");
        
        if (nbtSize.getElementType() != NBTType.INT)
            throw new FileSyntaxException("size must be a TAG_LIST<TAG_INT>");
        if (nbtPalette.getElementType() != NBTType.COMPOUND)
            throw new FileSyntaxException("palette must be a TAG_LIST<TAG_COMPOUND>");
        if (blocks.getElementType() != NBTType.COMPOUND)
            throw new FileSyntaxException("blocks must be a TAG_LIST<TAG_COMPOUND>");
        
        if (nbtSize.size() != 3)
            throw new FileSyntaxException("size must be a triple of TAG_INT");
        
        Vertex3i size = deserializeIntTriple(nbtSize);
        
        BlockStructure result = new BlockStructure(size.getX(), size.getY(), size.getZ(), dataVersion);
        
        for (int i = 0; i < nbtPalette.size(); i++) {
            NBTCompound compound = (NBTCompound) nbtPalette.get(i);
            String[] namespacedKey = compound.getString("Name").split(":", 2);
            String nameSpace = namespacedKey[0];
            String id = namespacedKey[1];
            
            if (compound.hasKey("Properties")) {
                Map<String, String> blockState = new HashMap<>();
                NBTCompound properties = compound.getCompoundTag("Properties");
                for (String key : properties.getKeys())
                    blockState.put(key, properties.getString(key));
                result.addToPalette(new BlockKey(nameSpace, id, blockState));
            }
            else result.addToPalette(new BlockKey(nameSpace, id));
        }
        
        for (NBTTag nbtBlock : blocks) {
            //System.err.println(nbtBlock.toMSONString());
            NBTCompound compound = (NBTCompound) nbtBlock;
            int paletteIndex = compound.getInt("state");
            NBTList nbtPos = compound.getTagList("pos");
            
            Vertex3i pos = deserializeIntTriple(nbtPos);
            
            NBTCompound nbt = compound.hasKey("nbt")? compound.getCompoundTag("nbt") : null;
            
            result.addBlock(pos, paletteIndex, nbt);
        }
        
        return result;
    }
    
    private static int validateVersion(NBTCompound root) throws FileVersionException {
        if (root.hasKey("version")) {
            String version = "version=" + root.getInt("version");
            throw new FileVersionException(version + "!!! legacy (1.12.2 minus) structures are not supported");
        }
        final int dataVersion = root.getInt("DataVersion");
        if (dataVersion < BlockStructure.LATEST_VERSION) {
            String version = "DataVersion=" + dataVersion;
            throw new FileVersionException(version + "!!! legacy (1.12.2 minus) structures are not supported");
        }
        if (dataVersion > BlockStructure.LATEST_VERSION) {
            String version = "DataVersion=" + dataVersion;
            throw new FileVersionException(version + "!!! at most " + BlockStructure.LATEST_VERSION +
                " (1.13.2) is supported");
        }
        return dataVersion;
    }
    
    private static Vertex3i deserializeIntTriple(NBTList list) {
        return new Vertex3i(
            ((NBTInt) list.get(0)).getIntValue(),
            ((NBTInt) list.get(1)).getIntValue(),
            ((NBTInt) list.get(2)).getIntValue()
        );
    }
    
}
