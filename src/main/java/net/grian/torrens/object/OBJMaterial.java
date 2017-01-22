package net.grian.torrens.object;

import net.grian.spatium.util.ColorMath;

public class OBJMaterial {

    private final String name;

    private int
            ambientColor = ColorMath.SOLID_WHITE,
            diffuseColor = ColorMath.SOLID_WHITE,
            specularColor = ColorMath.SOLID_WHITE,
            transFilter = ColorMath.SOLID_WHITE;

    private Texture
    ambientMap,
    diffuseMap,
    specularColorMap,
    specularHighlightMap,
    alphaMap,
    bumpMap,
    displacementMap,
    decalMap;


    private float dissolution = 1F;
    private byte illum = 0;

    public OBJMaterial(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getAmbientColor() {
        return ambientColor;
    }

    public int getDiffuseColor() {
        return diffuseColor;
    }

    public int getSpecularColor() {
        return specularColor;
    }

    public int getTransmissionFilter() {
        return transFilter;
    }

    public Texture getAmbientMap() {
        return ambientMap;
    }

    public Texture getDiffuseMap() {
        return diffuseMap;
    }

    public Texture getSpecularColorMap() {
        return specularColorMap;
    }

    public Texture getSpecularHighlightMap() {
        return specularHighlightMap;
    }

    public Texture getAlphaMap() {
        return alphaMap;
    }

    public Texture getBumpMap() {
        return bumpMap;
    }

    public Texture getDisplacementMap() {
        return displacementMap;
    }

    public Texture getDecalMap() {
        return decalMap;
    }

    public float getDissolution() {
        return dissolution;
    }

    public byte getIlluminationModel() {
        return illum;
    }

    // SETTERS

    public void setAmbientColor(int rgb) {
        this.ambientColor = rgb;
    }

    public void setDiffuseColor(int rgb) {
        this.diffuseColor = rgb;
    }

    public void setSpecularColor(int rgb) {
        this.specularColor = rgb;
    }

    public void setTransmissionFilter(int rgb) {
        this.transFilter = rgb;
    }

    public void setAmbientMap(Texture ambientMap) {
        this.ambientMap = ambientMap;
    }

    public void setDiffuseMap(Texture diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public void setSpecularColorMap(Texture specularColorMap) {
        this.specularColorMap = specularColorMap;
    }

    public void setSpecularHighlightMap(Texture specularHighlightMap) {
        this.specularHighlightMap = specularHighlightMap;
    }

    public void setAlphaMap(Texture alphaMap) {
        this.alphaMap = alphaMap;
    }

    public void setBumpMap(Texture bumpMap) {
        this.bumpMap = bumpMap;
    }

    public void setDisplacementMap(Texture displacementMap) {
        this.displacementMap = displacementMap;
    }

    public void setDecalMap(Texture decalMap) {
        this.decalMap = decalMap;
    }

    public void setDissolution(float dissolution) {
        this.dissolution = dissolution;
    }

    public void setIlluminationModel(int illum) {
        if (illum < 0) throw new IllegalArgumentException("illum must be positive");
        this.illum = (byte) illum;
    }

}
