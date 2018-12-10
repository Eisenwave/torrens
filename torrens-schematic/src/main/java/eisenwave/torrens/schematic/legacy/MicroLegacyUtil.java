package eisenwave.torrens.schematic.legacy;

import eisenwave.torrens.schematic.BlockKey;
import org.apache.commons.csv.*;
import org.jetbrains.annotations.Nullable;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

public final class MicroLegacyUtil {
    
    private final static MicroLegacyUtil INSTANCE = new MicroLegacyUtil("blocktable.csv");
    
    private final Map<BlockKey, LegacyBlockKey> byKey = new HashMap<>();
    private final Map<LegacyBlockKey, BlockKey> byLegacyKey = new HashMap<>();
    
    private MicroLegacyUtil(String blockTableResource) {
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(blockTableResource)) {
            CSVParser parser = CSVParser.parse(stream, Charset.defaultCharset(), CSVFormat.DEFAULT);
            for (CSVRecord record : parser.getRecords()) {
                int id = Integer.parseInt(record.get(0));
                byte data = Byte.parseByte(record.get(1));
                
                try {
                    add(new LegacyBlockKey(id, data), BlockKey.parse(record.get(3)));
                } catch (IllegalArgumentException ex) {
                    throw new IOException("error when parsing: " + record.toString(), ex);
                }
            }
        } catch (IOException ex) {
            throw new IOError(ex);
        }
    }
    
    private void add(LegacyBlockKey legacyKey, BlockKey key) {
        byKey.put(key, legacyKey);
        byLegacyKey.put(legacyKey, key);
    }
    
    @Nullable
    public static BlockKey getByLegacyKey(int id, byte data) {
        return getByLegacyKey(new LegacyBlockKey(id, data));
    }
    
    @Nullable
    public static BlockKey getByLegacyKey(LegacyBlockKey key) {
        //noinspection deprecation
        BlockKey result = INSTANCE.byLegacyKey.get(key);
        
        return result != null? result : INSTANCE.byLegacyKey.get(new LegacyBlockKey(key.getId()));
    }
    
    @Nullable
    public static LegacyBlockKey getByMinecraftKey13(String key) {
        return INSTANCE.byKey.get(BlockKey.minecraft(key));
    }
    
    @Nullable
    public static LegacyBlockKey getByMinecraftKey13(BlockKey key) {
        return INSTANCE.byKey.get(key);
    }
    
}
