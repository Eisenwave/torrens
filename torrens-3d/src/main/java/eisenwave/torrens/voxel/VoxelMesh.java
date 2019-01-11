package eisenwave.torrens.voxel;

import eisenwave.torrens.object.BoundingBox6i;
import eisenwave.torrens.object.Vertex3i;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class VoxelMesh implements Serializable, Iterable<VoxelMesh.Element> {

    private final List<Element> list = new ArrayList<>();

    public VoxelMesh() {}

    public VoxelMesh(VoxelMesh copyOf) {
        for (Element e : copyOf)
            list.add(e.clone());
    }
    
    public VoxelMesh(VoxelArray array) {
        list.add(new Element(0, 0, 0, array));
    }
    
    // GETTERS
    
    /**
     * Returns the amount of elements in this mesh.
     *
     * @return the amount of elements
     */
    public int size() {
        return list.size();
    }

    /**
     * Returns the combined volume of all voxel arrays in this mesh.
     *
     * @return the combined volume of all arrays
     */
    public int getCombinedVolume() {
        int v = 0;
        for (Element e : list)
            v += e.getArray().getVolume();
        return v;
    }

    /**
     * Returns the total amount of voxels in this mesh.
     *
     * @return the voxel count
     */
    public int voxelCount() {
        int count = 0;
        for (Element e : list)
            count += e.getArray().size();
        return count;
    }

    public VoxelMesh.Element[] toArray() {
        return list.toArray(new Element[list.size()]);
    }
    
    public BoundingBox6i getBoundaries() {
        if (isEmpty()) throw new IllegalStateException("empty meshes have no boundaries");
        int
            xmin = Integer.MAX_VALUE, ymin = Integer.MAX_VALUE, zmin = Integer.MAX_VALUE,
            xmax = Integer.MIN_VALUE, ymax = Integer.MIN_VALUE, zmax = Integer.MIN_VALUE;

        for (Element element : list) {
            final int
                exmin = element.getMinX(), eymin = element.getMinY(), ezmin = element.getMinZ(),
                exmax = element.getMaxX(), eymax = element.getMaxY(), ezmax = element.getMaxZ();

            if (exmin < xmin) xmin = exmin;
            if (eymin < ymin) ymin = eymin;
            if (ezmin < zmin) zmin = ezmin;
            if (exmax > xmax) xmax = exmax;
            if (eymax > ymax) ymax = eymax;
            if (ezmax > zmax) zmax = ezmax;
        }

        return new BoundingBox6i(xmin, ymin, zmin, xmax, ymax, zmax);
    }
    
    public Vertex3i getMin() {
        if (isEmpty()) throw new IllegalStateException("empty meshes have no minimum point");
        int
            x = Integer.MAX_VALUE,
            y = Integer.MAX_VALUE,
            z = Integer.MAX_VALUE;
    
        for (Element element : list) {
            final int
                ex = element.getMinX(),
                ey = element.getMinY(),
                ez = element.getMinZ();
        
            if (ex < x) x = ex;
            if (ey < y) y = ey;
            if (ez < z) z = ez;
        }
    
        return new Vertex3i(x, y, z);
    }
    
    public Vertex3i getMax() {
        if (isEmpty()) throw new IllegalStateException("empty meshes have no maximum point");
        int
            x = Integer.MIN_VALUE,
            y = Integer.MIN_VALUE,
            z = Integer.MIN_VALUE;
        
        for (Element element : list) {
            final int
                ex = element.getMaxX(),
                ey = element.getMaxY(),
                ez = element.getMaxZ();
            
            if (ex > x) x = ex;
            if (ey > y) y = ey;
            if (ez > z) z = ez;
        }
        
        return new Vertex3i(x, y, z);
    }

    public void add(int x, int y, int z, VoxelArray array) {
        this.list.add(new Element(x, y, z, array));
    }

    // PREDICATES

    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    // ITERATION
    
    @NotNull
    @Override
    public Iterator<Element> iterator() {
        return list.iterator();
    }
    
    // MISC

    @Override
    public String toString() {
        return VoxelMesh.class.getSimpleName() + "{size="+size()+"}";
    }

    @Override
    public VoxelMesh clone() {
        return new VoxelMesh(this);
    }

    // CLASSES
    
    public static class Element {

        private final int minX, minY, minZ, maxX, maxY, maxZ;
        private final VoxelArray array;

        public Element(int x, int y, int z, VoxelArray array) {
            Objects.requireNonNull(array, "array must not be null");
            this.minX = x;
            this.minY = y;
            this.minZ = z;
            this.maxX = x + array.getSizeX()-1;
            this.maxY = y + array.getSizeY()-1;
            this.maxZ = z + array.getSizeZ()-1;
            this.array = array;
        }

        public Element(Element copyOf) {
            this(copyOf.minX, copyOf.minY, copyOf.minZ, copyOf.array);
        }

        public int getMinX() {
            return minX;
        }

        public int getMinY() {
            return minY;
        }

        public int getMinZ() {
            return minZ;
        }

        public int getMaxX() {
            return maxX;
        }

        public int getMaxY() {
            return maxY;
        }

        public int getMaxZ() {
            return maxZ;
        }

        public Vertex3i getPosition() {
            return new Vertex3i(minX, minY, minZ);
        }

        /**
         * Returns the boundaries of the element.
         *
         * @return the boundaries of the element
         */
        public BoundingBox6i getBoundaries() {
            return new BoundingBox6i(minX, minY, minZ, maxX, maxY, maxZ);
        }

        /**
         * Returns the array positioned in this element.
         *
         * @return the array
         */
        public VoxelArray getArray() {
            return array;
        }

        // MISC
        
        @Override
        public Element clone() {
            return new Element(this);
        }
    
        @Override
        public String toString() {
            return Element.class.getSimpleName()+"{bounds="+getBoundaries()+"}";
        }
        
    }

}
