package net.grian.torrens.mc;

import net.grian.torrens.img.Texture;

import java.util.*;

/**
 * Object representation of a Minecraft model.
 */
public class MCModel implements Iterable<MCElement> {

    private final Map<String, Texture> textures = new HashMap<>();
    private final Set<MCElement> elements = new HashSet<>();

    /**
     * Returns a texture in the model by its name.
     *
     * @param name the texture name
     * @return the texture
     */
    public Texture getTexture(String name) {
        return textures.get(name);
    }

    /**
     * Returns the combined volume of all elements.
     *
     * @return the combined volume of all elements
     */
    public float getCombinedVolume() {
        float v = 0;
        for (MCElement e : elements)
            v += e.getShape().getVolume();
        return v;
    }

    /**
     * Adds a named texture to the model.
     *
     * @param name the texture name
     * @param texture the texture
     * @return the previous texture with the name or null if there has been none
     */
    public Texture addTexture(String name, Texture texture) {
        return textures.put(name, texture);
    }

    /**
     * Returns a set containing the names of all textures in this model.
     *
     * @return the names of all textures
     */
    public Set<String> getTextures() {
        return textures.keySet();
    }

    /**
     * Returns the size of this model in elements.
     *
     * @return the size of the model in elements
     */
    public int size() {
        return elements.size();
    }

    /**
     * Clears all elements in this model.
     */
    public void clearElements() {
        elements.clear();
    }

    /**
     * Adds an element to the model.
     *
     * @param element the element
     */
    public void addElement(MCElement element) {
        elements.add(element);
    }

    /**
     * Returns a set containing all elements in this model.
     *
     * @return all elements
     */
    public Set<MCElement> getElements() {
        return elements;
    }

    @Override
    public Iterator<MCElement> iterator() {
        return elements.iterator();
    }



}