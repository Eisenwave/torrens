package net.grian.torrens.util.wavefront;

import net.grian.torrens.img.Texture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * A Wavefront Material Library.
 */
public class MTLLibrary implements Serializable, Iterable<MTLMaterial> {

    private String name;

    private final Map<String, Texture> maps = new HashMap<>();
    private final Map<String, MTLMaterial> materials = new HashMap<>();

    public MTLLibrary(String name) {
        setName(name);
    }
    
    // GETTERS
    
    /**
     * Returns the name of this material library.
     *
     * @return the name of this material library
     */
    @NotNull
    public String getName() {
        return name;
    }
    
    /**
     * Returns the library's texture/map with given name or {@code null} if it has none.
     *
     * @param name the map name
     * @return the map
     */
    @Nullable
    public Texture getMap(String name) {
        return maps.get(name);
    }
    
    /**
     * Returns the library's material with given name or {@code null} if it has none.
     *
     * @param name the material name
     * @return the material
     */
    @Nullable
    public MTLMaterial getMaterial(String name) {
        return materials.get(name);
    }
    
    // CHECKERS
    
    /**
     * Returns whether this library contains no materials.
     *
     * @return whether this library contains no materials
     */
    public boolean isEmpty() {
        return materials.isEmpty();
    }
    
    // SETTERS
    
    /**
     * Sets the name of this library.
     *
     * @param name the library name
     * @see #getName()
     */
    public void setName(@NotNull String name) {
        this.name = Objects.requireNonNull(name);
    }
    
    /**
     * Adds a map to this library which may be referenced by materials.
     *
     * @param name the map name
     * @param map the map texture
     */
    public void addMap(@NotNull String name, @NotNull Texture map) {
        maps.put(Objects.requireNonNull(name), Objects.requireNonNull(map));
    }
    
    public void addMaterial(@NotNull MTLMaterial material) {
        materials.put(material.getName(), material);
    }

    public void removeMap(@NotNull String name) {
        maps.remove(Objects.requireNonNull(name));
    }

    public void removeMaterial(@NotNull String name) {
        materials.remove(Objects.requireNonNull(name));
    }

    // MISC
    
    @NotNull
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
