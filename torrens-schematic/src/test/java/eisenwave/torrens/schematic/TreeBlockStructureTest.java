package eisenwave.torrens.schematic;

import net.grian.spatium.util.TestUtil;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class TreeBlockStructureTest {
    
    @Test
    public void resolution() throws Exception {
        TreeBlockStructure struct = new TreeBlockStructure(17, 23, 55);
        
        assertEquals(64, struct.getResolution());
    }
    
    @Test
    public void setAndGet() throws Exception {
        TreeBlockStructure struct = new TreeBlockStructure(17, 23, 55);
        Random rng = new Random();
        
        struct.forEachPos((x,y,z) -> {
            int id = rng.nextInt(255) + 1;
            byte data = (byte) rng.nextInt(16);
            BlockKey block = new BlockKey(id, data);
            
            struct.setBlock(x, y, z, id, data);
            assertEquals(block, struct.getBlock(x, y, z));
        });
    }
    
    @Test
    public void allocation() throws Exception {
        TreeBlockStructure struct = new TreeBlockStructure(16, 23, 30);
        
        assertEquals(32, struct.getResolution());
    
        // tree is expected to alloc one 32x node with one 16x node with one 8x node with one 4x leaf
        struct.setId(3, 3, 3, 1);
        assertEquals(4, struct.getNodeCount());
        assertEquals(64, struct.getAllocatedVolume());
    
        // tree is expected to alloc another 4x leaf in the 16x node in the 32x node
        struct.setId(5, 10, 5, 1);
        assertEquals(6, struct.getNodeCount());
        assertEquals(128, struct.getAllocatedVolume());
    
        // tree is expected to alloc one 16x node with one 8x node with one 4x leaf in the 32x node
        struct.setId(0, 0, 16, 1);
        assertEquals(9, struct.getNodeCount());
        assertEquals(192, struct.getAllocatedVolume());
    
        // tree is expected to alloc no new nodes
        struct.setId(1, 1, 17, 1);
        assertEquals(9, struct.getNodeCount());
        assertEquals(192, struct.getAllocatedVolume());
    }
    
    private final static int
        ARRAY = 0,
        TREE = 1,
        MAP = 2,
        GET = 0,
        SET = 1,
        FILL = 2;
    
    @Test
    public void performance() {
        final int times = 1_000_000, lim = 256, which = TREE;
        final long mem = TestUtil.usedMemory(), t = System.currentTimeMillis();
        
        // always: tree is at least 5 times slower than array
        // up to lim = 512: tree is worse than array at 1 million random/get & set memory
        
        //System.out.println("");
        
        BlockStructure struct;
        
        if (which == ARRAY)
        {
            struct = new ArrayBlockStructure(lim, lim, lim);
            System.out.println("array upon creation: "+ profile(struct, times, lim, SET) );
            System.out.println("array next time: "+     profile(struct, times, lim, GET) );
        }
    
        else if (which == TREE)
        {
            struct = new TreeBlockStructure(lim, lim, lim);
            System.out.println("tree upon creation: "+ profile(struct, times, lim, SET) );
            System.out.println("tree next time: "+     profile(struct, times, lim, GET) );
        }
    
        else if (which == MAP)
        {
            struct = new HashMapBlockStructure(lim, lim, lim);
            System.out.println("map upon creation: "+ profile(struct, times, lim, SET) );
            System.out.println("map next time: "+     profile(struct, times, lim, GET) );
        }
    
        System.out.println("---------------------------");
        System.out.println("Results for "+struct.getClass().getSimpleName()+":");
        System.out.println(String.format("Req Time: %d ms", System.currentTimeMillis()-t));
        System.out.println(String.format("Req Memory: %.2f mB", (TestUtil.usedMemory()-mem) / 1_000_000F));
    }
    
    private static long profile(BlockStructure struct, int times, int lim, int method) {
        Random random = new Random();
    
        long t0 = System.currentTimeMillis();
        
        switch (method) {
            case GET: {
                for (int i = 0; i < times; i++)
                    struct.getBlock(random.nextInt(lim), random.nextInt(lim), random.nextInt(lim));
                
                break;
            }
            
            case SET: {
                for (int i = 0; i < times; i++)
                    struct.setBlock(random.nextInt(lim), random.nextInt(lim), random.nextInt(lim), 1, (byte) 1);
                
                break;
            }
            
            case FILL: {
                struct.forEachPos((x, y, z) -> struct.setBlock(x, y, z, 1, (byte) 0));
                break;
            }
        }
    
        return System.currentTimeMillis() - t0;
    }
    
    private static void printMem() {
        System.out.println(String.format("Used Memory: %.2f mB", TestUtil.usedMemory() / 1_000_000F));
    }
    
    private static void sleepyTime() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new AssertionError(e);
        }
    }
    
    private static void freeTime() {
        System.gc();
        System.runFinalization();
    }
    
}
