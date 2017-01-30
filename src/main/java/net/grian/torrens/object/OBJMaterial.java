package net.grian.torrens.object;

import net.grian.spatium.util.ColorMath;

import javax.annotation.Nullable;

public class OBJMaterial {

    private final OBJMaterialLibrary library;
    private final String name;

    private int
        Ka = ColorMath.SOLID_WHITE,
        Kd = ColorMath.SOLID_WHITE,
        Ks = ColorMath.SOLID_WHITE,
        Tf = ColorMath.SOLID_WHITE;
    private float
        d = 1F;
    private byte
        illum = 0;
    private String
        map_Ka,
        map_Kd,
        map_Ks,
        map_Ns,
        map_d,
        bump,
        disp,
        decal;
    

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
    
    //SIMPLE GETTERS

    public int getAmbientColor() {
        return Ka;
    }

    public int getDiffuseColor() {
        return Kd;
    }

    public int getSpecularColor() {
        return Ks;
    }

    public int getTransmissionFilter() {
        return Tf;
    }
    
    public float getDissolution() {
        return d;
    }
    
    public byte getIlluminationModel() {
        return illum;
    }
    
    //MAP GETTERS

    @Nullable
    public String getAmbientMap() {
        return map_Ka;
    }

    @Nullable
    public String getDiffuseMap() {
        return map_Kd;
    }

    @Nullable
    public String getSpecularColorMap() {
        return map_Ks;
    }

    @Nullable
    public String getSpecularHighlightMap() {
        return map_Ns;
    }

    @Nullable
    public String getDissolutionMap() {
        return map_d;
    }

    @Nullable
    public String getBumpMap() {
        return bump;
    }

    @Nullable
    public String getDisplacementMap() {
        return disp;
    }

    @Nullable
    public String getDecalMap() {
        return decal;
    }

    //SIMPLE SETTERS

    public void setAmbientColor(int rgb) {
        this.Ka = rgb;
    }

    public void setDiffuseColor(int rgb) {
        this.Kd = rgb;
    }

    public void setSpecularColor(int rgb) {
        this.Ks = rgb;
    }
    
    public void setTransmissionFilter(int rgb) {
        this.Tf = rgb;
    }
    
    public void setDissolution(float dissolution) {
        this.d = dissolution;
    }
    
    public void setIlluminationModel(int illum) {
        if (illum < 0) throw new IllegalArgumentException("illum must be positive");
        this.illum = (byte) illum;
    }

    //MAP SETTERS
    
    public void setAmbientMap(@Nullable String ambientMap) {
        this.map_Ka = ambientMap;
    }

    public void setDiffuseMap(@Nullable String diffuseMap) {
        this.map_Kd = diffuseMap;
    }
    
    public void setSpecularColorMap(@Nullable String specularColorMap) {
        this.map_Ks = specularColorMap;
    }

    public void setSpecularHighlightMap(@Nullable String specularHighlightMap) {
        this.map_Ns = specularHighlightMap;
    }
    
    public void setDissolutionMap(@Nullable String alphaMap) {
        this.map_d = alphaMap;
    }

    public void setBumpMap(@Nullable String bumpMap) {
        this.bump = bumpMap;
    }

    public void setDisplacementMap(@Nullable String displacementMap) {
        this.disp = displacementMap;
    }

    public void setDecalMap(@Nullable String decalMap) {
        this.decal = decalMap;
    }
    
    //MISC
    
    @Override
    public String toString() {
        return OBJMaterial.class.getSimpleName()+"{name="+getName()+"}";
    }
}
