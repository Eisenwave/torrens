package eisenwave.torrens.wavefront;

import eisenwave.torrens.util.ColorMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Wavefront material.
 */
public class MTLMaterial implements Serializable {

    private final MTLLibrary library;
    private final String name;

    private int
        Ka = ColorMath.SOLID_WHITE,
        Kd = ColorMath.SOLID_WHITE,
        Ks = ColorMath.SOLID_WHITE,
        Tf = ColorMath.SOLID_WHITE;
    private float
        d = 1,
        Ns = -1,
        sharpness = 60,
        Ni = 1;
    private byte
        illum = 0;
    
    @Nullable
    private String
        map_Ka,
        map_Kd,
        map_Ks,
        map_Ns,
        map_d,
        bump,
        disp,
        decal;
    

    public MTLMaterial(@NotNull MTLLibrary library, String name) {
        this.library = Objects.requireNonNull(library);
        this.name = name;
    }

    /**
     * Returns the library this material belongs to.
     *
     * @return this material's library
     */
    @NotNull
    public MTLLibrary getLibrary() {
        return library;
    }
    
    /**
     * Returns the name of this material.
     *
     * @return the material name
     */
    @NotNull
    public String getName() {
        return name;
    }
    
    //SIMPLE GETTERS
    
    /**
     * Returns an ARGB integer representing the ambient color of this material.
     *
     * @return the ambient color
     */
    public int getAmbientColor() {
        return Ka;
    }
    
    /**
     * Returns an ARGB integer representing the diffuse color of this material.
     *
     * @return the diffuse color
     */
    public int getDiffuseColor() {
        return Kd;
    }
    
    /**
     * Returns an ARGB integer representing the specular color of this material.
     *
     * @return the specular color
     */
    public int getSpecularColor() {
        return Ks;
    }
    
    /**
     * <p>
     *     Returns an ARGB integer representing the transmission filter color of this material.
     * </p>
     * <p>
     *     From the mtl documentation: <blockquote><i>
     *         Any light passing through the object is filtered by the transmission
     *         filter, which only allows the specifiec colors to pass through. For
     *         example, Tf 0 1 0 allows all the green to pass through and filters out
     *         all the red and blue.
     *     </i></blockquote>
     * </p>
     *
     * @return the transmission filter
     */
    public int getTransmissionFilter() {
        return Tf;
    }
    
    /**
     * <p>
     *     Returns the material dissolution factor.
     * </p>
     * <p>
     *     From the mtl documentation: <blockquote><i>
     *         [dissolution] is the amount this material dissolves into the background. A
     *         factor of 1.0 is fully opaque.  This is the default when a new material is created.
     *         A factor of 0.0 is fully dissolved (completely transparent).
     *     </i></blockquote>
     * </p>
     *
     * @return the material dissolution
     */
    public float getDissolution() {
        return d;
    }
    
    /**
     * <p>
     *     Returns the specular exponent of this material. This defines the focus of the specular highlight.
     * </p>
     * <p>
     *     If the returned specular exponent is negative, the material has no specular exponent.
     * </p>
     * <p>
     *     From the mtl documentation: <blockquote><i>
     *          "exponent" is the value for the specular exponent. A high exponent
     *          results in a tight, concentrated highlight. Ns values normally range from 0 to 1000.
     *     </i></blockquote>
     * </p>
     *
     * @return the specular exponent
     */
    public float getSpecularExponent() {
        return Ns;
    }
    
    /**
     * <p>
     *     Returns sharpness value of this material.
     * </p>
     * <p>
     *     The value may be in a range from <code>0</code> to <code>1000</code>; if the value is <code>60</code>
     *     (its default value), sharpness is not being serialized.
     * </p>
     * <p>
     *     From the mtl documentation: <blockquote><i>
     *          Specifies the sharpness of the reflections from the local reflection map. [...]
     *          A high value results in a clear reflection of objects in the reflection map.
     *     </i></blockquote>
     * </p>
     *
     * @return the specular exponent
     */
    public float getSharpness() {
        return sharpness;
    }
    
    /**
     * <p>
     *     Returns optical density of this material.
     * </p>
     * <p>
     *     The value may be in a range from <code>0.001</code> to <code>10</code>; if the value is <code>1</code>
     *     (its default value), optical density is not being serialized.
     * </p>
     * <p>
     *     From the mtl documentation: <blockquote><i>
     *           The values can range from 0.001 to 10. A value of 1.0 means that light does not bend as it passes
     *           through an object.
     *           Increasing the optical_density increases the amount of bending.
     *           Glass has an index of refraction of about 1.5.  Values of less than 1.0 produce bizarre results and
     *           are not recommended.
     *     </i></blockquote>
     * </p>
     *
     * @return the specular exponent
     */
    public float getOpticalDensity() {
        return Ni;
    }
    
    /**
     * Returns the illumination model of this material.
     *
     * @return the illumination model
     */
    public int getIlluminationModel() {
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

    // SIMPLE SETTERS

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
    
    public void setSpecularExponent(float exponent) {
        this.Ns = exponent;
    }
    
    public void setSharpness(float sharpness) {
        this.sharpness = sharpness;
    }
    
    public void setOpticalDensity(float opticalDensity) {
        this.Ni = opticalDensity;
    }
    
    public void setIlluminationModel(int illum) {
        if (illum < 0) throw new IllegalArgumentException("illum must be positive");
        this.illum = (byte) illum;
    }

    // MAP SETTERS
    
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
        return MTLMaterial.class.getSimpleName()+"{name="+getName()+"}";
    }
    
}
