package net.grian.torrens.object;

import net.grian.spatium.util.ColorMath;

import javax.annotation.Nullable;

public class OBJMaterial {

    private final OBJMaterialLibrary library;
    private final String name;

    private int
            ambientColor = ColorMath.SOLID_WHITE,
            diffuseColor = ColorMath.SOLID_WHITE,
            specularColor = ColorMath.SOLID_WHITE,
            transFilter = ColorMath.SOLID_WHITE;

    private String
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

    public OBJMaterial(OBJMaterialLibrary library, String name) {
        this.library = library;
        this.name = name;
    }

    /**
     * Returns the library this material belongs to.
     *
     * @return this material's library
     */
    public OBJMaterialLibrary getLibrary() {
        return library;
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

    @Nullable
    public String getAmbientMap() {
        return ambientMap;
    }

    @Nullable
    public String getDiffuseMap() {
        return diffuseMap;
    }

    @Nullable
    public String getSpecularColorMap() {
        return specularColorMap;
    }

    @Nullable
    public String getSpecularHighlightMap() {
        return specularHighlightMap;
    }

    @Nullable
    public String getAlphaMap() {
        return alphaMap;
    }

    @Nullable
    public String getBumpMap() {
        return bumpMap;
    }

    @Nullable
    public String getDisplacementMap() {
        return displacementMap;
    }

    @Nullable
    public String getDecalMap() {
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

    public void setAmbientMap(@Nullable String ambientMap) {
        this.ambientMap = ambientMap;
    }

    public void setDiffuseMap(@Nullable String diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public void setSpecularColorMap(@Nullable String specularColorMap) {
        this.specularColorMap = specularColorMap;
    }

    public void setSpecularHighlightMap(@Nullable String specularHighlightMap) {
        this.specularHighlightMap = specularHighlightMap;
    }

    public void setAlphaMap(@Nullable String alphaMap) {
        this.alphaMap = alphaMap;
    }

    public void setBumpMap(@Nullable String bumpMap) {
        this.bumpMap = bumpMap;
    }

    public void setDisplacementMap(@Nullable String displacementMap) {
        this.displacementMap = displacementMap;
    }

    public void setDecalMap(@Nullable String decalMap) {
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
