package eisenwave.torrens.wavefront;

import eisenwave.spatium.array.ArrayIterator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class OBJFace implements Iterable<OBJTriplet> {

    private final OBJTriplet[] shape;

    /**
     * Constructs a new face from an array of {@link OBJTriplet} objects, forming the shape of the face.
     *
     * @param shape the shape of the face
     */
    public OBJFace(OBJTriplet... shape) {
        if (shape.length < 1)throw new IllegalArgumentException("face must have at least one point");
        this.shape = shape;
    }

    public OBJTriplet[] getShape() {
        return shape;
    }
    
    public OBJTriplet getTriplet(int index) {
        return shape[index];
    }

    /**
     * Returns amount of vertices this face is made of.
     *
     * @return the amount of vertices
     */
    public int size() {
        return shape.length;
    }
    
    @NotNull
    @Override
    public Iterator<OBJTriplet> iterator() {
        return new ArrayIterator<>(shape);
    }
    
}
