package net.grian.torrens.object;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public class OBJMaterialLibrary implements Iterable<OBJMaterial> {

    private String name;

    private final Map<String, Texture> maps = new HashMap<>();
    private final Map<String, OBJMaterial> materials = new HashMap<>();

    public OBJMaterialLibrary(String name) {
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
    public OBJMaterial getMaterial(String name) {
        return materials.get(name);
    }
    
    public void setName(@Nonnull String name) {
        this.name = Objects.requireNonNull(name);
    }
    
    public void addMap(@Nonnull String name, @Nonnull Texture map) {
        maps.put(Objects.requireNonNull(name), Objects.requireNonNull(map));
    }

    public void addMaterial(@Nonnull OBJMaterial material) {
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
    public Iterator<OBJMaterial> iterator() {
        return materials.values().iterator();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(OBJMaterialLibrary.class.getSimpleName());
        builder
            .append("{name=")
            .append(name)
            .append(",materials=[");
        
        Iterator<OBJMaterial> iter = iterator();
        boolean hasNext = iter.hasNext();
        while (hasNext) {
            builder.append(iter.next().toString());
            if (hasNext = iter.hasNext())
                builder.append(",");
        }
        
        return builder.append("]}").toString();
    }
}
