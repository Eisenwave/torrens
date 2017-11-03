package net.grian.torrens.schematic;

import net.grian.spatium.array.Array3;
import net.grian.spatium.array.LowNibbleArray;
import net.grian.spatium.util.FastMath;
import net.grian.spatium.util.PrimMath;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TreeBlockStructure implements BlockStructure {
    
    private final static int
        MIN_POW = 2,
        MAX_POW = 15,
    
        MIN_RES = 1 << MIN_POW,
        MIN_RES_SQR = MIN_RES * MIN_RES,
        MIN_RES_CUB = MIN_RES_SQR * MIN_RES,
    
        MAX_RES = 1 << MAX_POW;
    
    private final Array3<AbstractNode> array;
    
    private final int res/*, arrSizeX, arrSizeY, arrSizeZ*/, sizeX, sizeY, sizeZ;
    private final byte pow;
    
    public TreeBlockStructure(int x, int y, int z) {
        if (x < 0) throw new NegativeArraySizeException("x: "+x);
        if (y < 0) throw new NegativeArraySizeException("y: "+y);
        if (z < 0) throw new NegativeArraySizeException("z: "+z);
        this.sizeX = x;
        this.sizeY = y;
        this.sizeZ = z;
        
        this.res = Math.min(MAX_RES, FastMath.greaterPow2(PrimMath.max(x, y, z)));
        this.pow = (byte) FastMath.log2(res);
        //System.out.println(res+" "+x+" "+y+" "+z);
        
        this.array = new Array3<>(
            Math.max(1, x / res),
            Math.max(1, y / res),
            Math.max(1, z / res), AbstractNode.class);
        
        //System.out.println(String.format("%d %d %d", array.getSizeX(), array.getSizeY(), array.getSizeZ()));
    }
    
    @Override
    public int getSizeX() {
        return sizeX;
    }
    
    @Override
    public int getSizeY() {
        return sizeY;
    }
    
    @Override
    public int getSizeZ() {
        return sizeZ;
    }
    
    protected int getResolution() {
        return res;
    }
    
    protected int getNodes() {
        int result = 0;
        for (AbstractNode node : array) {
            if (node != null) {
                result += node.getNodes();
            }
        }
        
        return result;
    }
    
    protected int getAllocatedVolume() {
        int result = 0;
        for (AbstractNode node : array)
            if (node != null)
                result += node.getAllocatedVolume();
    
        return result;
    }
    
    @Nullable
    private AbstractNode getOptionalNode(int x, int y, int z) {
        return array.get(x/res, y/res, z/res);
    }
    
    @NotNull
    private AbstractNode getRequiredNode(int x, int y, int z) {
        final int
            ax = x / res,
            ay = y / res,
            az = z / res;
        
        //System.out.println(x+" "+y+" "+z+" -> Array: "+ax+" "+ay+" "+az);
        
        //System.out.println(array.get(ax, ay, az).getClass().getSimpleName());
        AbstractNode node = array.get(ax, ay, az);
        if (node != null)
            return node;
        
        final int
            nx = ax * res,
            ny = ay * res,
            nz = az * res;
        
        if (res <= MIN_RES) {
            BlockCubeNode newNode = new BlockCubeNode(nx, ny, nz);
            //System.out.println(x+" "+y+" "+z+" -> "+newNode);
            array.set(ax, ay, az, newNode);
            return newNode;
        }
        
        NodeCubeNode newNode = new NodeCubeNode(nx, ny, nz, (byte) (pow - 1));
        //System.out.println(x+" "+y+" "+z+" -> "+newNode);
        array.set(ax, ay, az, newNode);
        return newNode.getRequiredNode(x, y, z);
    }
    
    @Override
    public int getId(int x, int y, int z) {
        AbstractNode node = getOptionalNode(x, y, z);
        return node==null? 0 : node.getId(x, y, z);
    }
    
    @Override
    public byte getData(int x, int y, int z) {
        AbstractNode node = getOptionalNode(x, y, z);
        return node==null? 0 : node.getData(x, y, z);
    }
    
    @Override
    public BlockKey getBlock(int x, int y, int z) {
        AbstractNode node = getOptionalNode(x, y, z);
        return node==null? BlockKey.AIR : node.getBlock(x, y, z);
    }
    
    @Override
    public void remove(int x, int y, int z) {
        AbstractNode node = getOptionalNode(x, y, z);
        if (node != null) node.setBlock(x, y, z, 0, (byte) 0);
    }
    
    @Override
    public void setId(int x, int y, int z, int id) {
        if (id == 0) remove(x, y, z);
        else getRequiredNode(x, y, z).setId(x, y, z, id);
    }
    
    @Override
    public void setData(int x, int y, int z, byte data) {
        getRequiredNode(x, y, z).setData(x, y, z, data);
    }
    
    @Override
    public void setBlock(int x, int y, int z, int id, byte data) {
        getRequiredNode(x, y, z).setBlock(x, y, z, id, data);
    }
    
    @Override
    public void clear() {
        array.fill(null);
    }
    
    private abstract static class AbstractNode {
        
        protected final int x, y, z;
    
        /**
         * Constructs a new abstract node.
         *
         * @param x the x-offset from the origin of the block structure
         * @param y the y-offset from the origin of the block structure
         * @param z the z-offset from the origin of the block structure
         */
        public AbstractNode(int x, int y, int z) {
            //System.out.println("constructed "+getClass().getSimpleName());
            this.x = x;
            this.y = y;
            this.z = z;
        }
        
        public abstract int getId(int x, int y, int z);
        
        public abstract byte getData(int x, int y, int z);
    
        public abstract BlockKey getBlock(int x, int y, int z);
    
        public abstract void setId(int x, int y, int z, int id);
    
        public abstract void setData(int x, int y, int z, byte data);
    
        public abstract void setBlock(int x, int y, int z, int id, byte data);
        
        protected abstract int getNodes();
        
        protected abstract int getLeafs();
        
        protected abstract int getAllocatedVolume();
    
        @Override
        public String toString() {
            return String.format("%s{x=%d, y=%d, z=%d}", getClass().getSimpleName(), x, y, z);
        }
        
    }
    
    private static class BlockCubeNode extends AbstractNode {
        
        private final byte[] arrayId = new byte[MIN_RES_CUB];
        private final LowNibbleArray arrayData = new LowNibbleArray(MIN_RES_CUB);
        
        public BlockCubeNode(int x, int y, int z) {
            super(x, y, z);
        }
        
        private int indexOf(int x, int y, int z) {
            return (x-this.x) + (y-this.y) * MIN_RES + (z-this.z) * MIN_RES_SQR;
        }
    
        @Override
        public int getId(int x, int y, int z) {
            return arrayId[indexOf(x, y, z)];
        }
    
        @Override
        public byte getData(int x, int y, int z) {
            return arrayData.get(indexOf(x, y, z));
        }
    
        @Override
        public BlockKey getBlock(int x, int y, int z) {
            int index = indexOf(x, y, z);
            return new BlockKey(arrayId[index], arrayData.get(index));
        }
    
        @Override
        public void setId(int x, int y, int z, int id) {
            arrayId[indexOf(x, y, z)] = (byte) id;
        }
    
        @Override
        public void setData(int x, int y, int z, byte data) {
            arrayData.set(indexOf(x, y, z), data);
        }
    
        @Override
        public void setBlock(int x, int y, int z, int id, byte data) {
            int index = indexOf(x, y, z);
            arrayId[index] = (byte) id;
            arrayData.set(index, data);
        }
    
        @Override
        protected int getNodes() {
            return 1;
        }
    
        @Override
        protected int getLeafs() {
            return 1;
        }
        
        @Override
        protected int getAllocatedVolume() {
            return MIN_RES_CUB;
        }
        
    }
    
    private static class NodeCubeNode extends AbstractNode {
        
        private final static int
            ARR_SIZE = 2,
            ARR_SIZE_SQR = ARR_SIZE * ARR_SIZE,
            ARR_LEN = ARR_SIZE_SQR * ARR_SIZE;
        
        private final AbstractNode[] array;
        private final byte pow;
    
        /**
         * Constructs a new node-cube node.
         *
         * @param x the x-coordinate in structure space
         * @param y the y-coordinate in structure space
         * @param z the z-coordinate in structure space
         * @param pow the binary logarithm of the resolution of this array
         */
        public NodeCubeNode(int x, int y, int z, byte pow) {
            super(x, y, z);
            this.pow = pow;
            this.array = new AbstractNode[ARR_LEN];
        }
        
        private AbstractNode getOptionalNode(int x, int y, int z) {
            final int
                ax = (x - this.x) >> pow,
                ay = (y - this.y) >> pow,
                az = (z - this.z) >> pow,
                index = ax + (ay * ARR_SIZE) + (az * ARR_SIZE_SQR);
            
            return array[index];
        }
        
        private AbstractNode getRequiredNode(int x, int y, int z) {
            final int
                ax = (x - this.x) >> pow,
                ay = (y - this.y) >> pow,
                az = (z - this.z) >> pow,
                index = ax + (ay * ARR_SIZE) + (az * ARR_SIZE_SQR);
            
            //System.out.println(String.format("I: %d %d %d", x, y, z));
            //System.out.println(String.format("A: %d %d %d", ax, ay, az));
            
            AbstractNode node = array[index];
            if (node != null)
                return node;
            
            final int
                nx = this.x + (ax << pow),
                ny = this.y + (ay << pow),
                nz = this.z + (az << pow);
            
            //System.out.println(String.format("N: %d %d %d", nx, ny, nz));
            
            if (pow <= MIN_POW)
                return array[index] = new BlockCubeNode(nx, ny, nz);
            
            NodeCubeNode newNode = new NodeCubeNode(nx, ny, nz, (byte) (pow - 1));
            array[index] = newNode;
            return newNode.getRequiredNode(x, y, z);
        }
    
        @Override
        public int getId(int x, int y, int z) {
            AbstractNode node = getOptionalNode(x, y, z);
            return node==null? 0 : node.getId(x, y, z);
        }
    
        @Override
        public byte getData(int x, int y, int z) {
            AbstractNode node = getOptionalNode(x, y, z);
            return node==null? 0 : node.getData(x, y, z);
        }
    
        @Override
        public BlockKey getBlock(int x, int y, int z) {
            AbstractNode node = getOptionalNode(x, y, z);
            return node==null? BlockKey.AIR : node.getBlock(x, y, z);
        }
    
        @Override
        public void setId(int x, int y, int z, int id) {
            getRequiredNode(x, y, z).setId(x, y, z, id);
        }
    
        @Override
        public void setData(int x, int y, int z, byte data) {
            getRequiredNode(x, y, z).setData(x, y, z, data);
        }
    
        @Override
        public void setBlock(int x, int y, int z, int id, byte data) {
            getRequiredNode(x, y, z).setBlock(x, y, z, id, data);
        }
    
        @Override
        protected int getNodes() {
            return getLeafs() + 1;
        }
    
        @Override
        protected int getLeafs() {
            int result = 0;
            for (AbstractNode node : array)
                if (node != null)
                    result += node.getNodes();
        
            return result;
        }
    
        @Override
        protected int getAllocatedVolume() {
            int result = 0;
            for (AbstractNode node : array)
                if (node != null)
                    result += node.getAllocatedVolume();
    
            return result;
        }
        
    }
    
}
