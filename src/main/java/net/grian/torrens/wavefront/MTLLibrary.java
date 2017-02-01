package net.grian.torrens.wavefront;

import net.grian.torrens.img.Texture;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class MTLLibrary implements Iterable<MTLMaterial> {

    private String name;

    private final Map<String, Texture> maps = new HashMap<>();
    private final Map<String, MTLMaterial> materials = new HashMap<>();

    public MTLLibrary(String name) {
        setName(name);
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nullable
    public Texture getMap(String name) {
        return maps.get(name);
    }

    @Nullable
    public MTLMaterial getMaterial(String name) {
        return materials.get(name);
    }
    
    public void setName(@Nonnull String name) {
        this.name = Objects.requireNonNull(name);
    }
    
    public void addMap(@Nonnull String name, @Nonnull Texture map) {
        maps.put(Objects.requireNonNull(name), Objects.requireNonNull(map));
    }

    public void addMaterial(@Nonnull MTLMaterial material) {
        materials.put(material.getName(), material);
    }
    
    public boolean isEmpty() {
        return materials.isEmpty();
    }

    public void removeMap(@Nonnull String name) {
        maps.remove(Objects.requireNonNull(name));
    }

    public void removeMaterial(@Nonnull String name) {
        materials.remove(Objects.requireNonNull(name));
    }

    @Override
    public Iterator<MTLMaterial> iterator() {
        return materials.values().iterator();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(MTLLibrary.class.getSimpleName());
        builder
            .append("{name=\"")
            .append(name)
            .append('\"');
        
        {
            builder.append(",materials=[");
            Iterator<MTLMaterial> iter = iterator();
            boolean hasNext = iter.hasNext();
            while (hasNext) {
                builder.append(iter.next());
                if (hasNext = iter.hasNext())
                    builder.append(",");
            }
            builder.append("]");
        }
        {
            builder.append(",maps=[");
            Iterator<String> iter = maps.keySet().iterator();
            boolean hasNext = iter.hasNext();
            while (hasNext) {
                builder
                    .append('\"')
                    .append(iter.next())
                    .append('\"');
                if (hasNext = iter.hasNext())
                    builder.append(",");
            }
            builder.append("]");
        }
        
        
        return builder.append("}").toString();
    }
    
}
