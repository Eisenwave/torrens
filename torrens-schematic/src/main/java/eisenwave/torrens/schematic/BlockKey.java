package eisenwave.torrens.schematic;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockKey {
    
    public final static String DEFAULT_NAMESPACE = "minecraft";
    
    //public final static BlockKey AIR = minecraft("air");
    
    public static BlockKey minecraft(String key) {
        return new BlockKey(DEFAULT_NAMESPACE, key);
    }
    
    private final String nameSpace;
    private final String key;
    private final Map<String, String> blockState;
    
    public BlockKey(@NotNull String nameSpace, @NotNull String id) {
        this.nameSpace = nameSpace;
        this.key = id;
        this.blockState = Collections.emptyMap();
    }
    
    public BlockKey(@NotNull String nameSpace, @NotNull String id, @NotNull Map<String, String> blockState) {
        this.nameSpace = nameSpace;
        this.key = id;
        this.blockState = new HashMap<>(blockState);
    }
    
    public String getNameSpace() {
        return nameSpace;
    }
    
    public String getId() {
        return key;
    }
    
    @Nullable
    public String getBlockState(String key) {
        return blockState.get(key);
    }
    
    // MISC
    
    @Override
    public int hashCode() {
        return nameSpace.hashCode() ^ key.hashCode();
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockKey && equals((BlockKey) obj);
    }
    
    public boolean equals(BlockKey key) {
        return this.nameSpace.equals(key.nameSpace)
            && this.key.equals(key.key)
            && this.blockState.equals(key.blockState);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(nameSpace)
            .append(':')
            .append(key);
        
        if (blockState.isEmpty())
            return builder.toString();
        
        builder.append('[');
        Iterator<Map.Entry<String, String>> iterator = blockState.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.append(next.getKey());
            builder.append('=');
            builder.append(next.getValue());
            
            while (iterator.hasNext()) {
                next = iterator.next();
                builder.append(',');
                builder.append(next.getKey());
                builder.append('=');
                builder.append(next.getValue());
            }
        }
        
        return builder.append(']').toString();
    }
    
    @NotNull
    public static BlockKey parse(String str) {
        int indexOfState = str.indexOf('[');
        
        String nameSpacedKey = indexOfState == -1? str : str.substring(0, indexOfState);
        String[] namespaceAndId = nameSpacedKey.split(":", 2);
        String nameSpace;
        String id;
        if (namespaceAndId.length == 1) {
            nameSpace = DEFAULT_NAMESPACE;
            id = namespaceAndId[0];
        }
        else {
            nameSpace = namespaceAndId[0];
            id = namespaceAndId[1];
        }
        
        if (indexOfState == -1)
            return new BlockKey(nameSpace, id);
        
        // omit square brackets and split at commas
        String[] state = str
            .substring(indexOfState + 1, str.length() - 1)
            .split("[ ]*,[ ]*");
        
        Map<String, String> resultState = new HashMap<>(4);
        for (String keyVal : state) {
            String[] keyAndVal = keyVal.split("=", 2);
            if (keyAndVal.length != 2)
                throw new IllegalArgumentException(str);
            resultState.put(keyAndVal[0], keyAndVal[1]);
        }
        
        return new BlockKey(nameSpace, id, resultState);
    }
    
}
