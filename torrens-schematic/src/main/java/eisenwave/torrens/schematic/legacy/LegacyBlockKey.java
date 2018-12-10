package eisenwave.torrens.schematic.legacy;

import java.io.Serializable;

@SuppressWarnings("unused")
public class LegacyBlockKey implements Serializable {
    
    public final static LegacyBlockKey
        AIR = new LegacyBlockKey(0, 0),
        STONE = new LegacyBlockKey(1, 0);
    
    private final int id;
    private final byte data;
    
    /**
     * Constructs a new arbitrary block key.
     *
     * @param id the block id (positive)
     * @param data the block data (positive)
     * @throws IllegalArgumentException if id or data are negative
     */
    public LegacyBlockKey(int id, byte data) {
        if (id < 0) throw new IllegalArgumentException("id must be positive");
        if (data < 0) throw new IllegalArgumentException("data must be positive");
        
        this.id = id;
        this.data = data;
    }
    
    /**
     * Constructs a new arbitrary block key.
     *
     * @param id the block id (positive)
     * @param data the block data (positive)
     * @throws IllegalArgumentException if id or data are negative
     */
    public LegacyBlockKey(int id, int data) {
        this(id, (byte) data);
    }
    
    /**
     * Constructs a new arbitrary block key.
     *
     * @param id the block id (positive)
     * @throws IllegalArgumentException if id is negative
     */
    public LegacyBlockKey(int id) {
        if (id < 0) throw new IllegalArgumentException("id must be positive");
        
        this.id = id;
        this.data = 0;
    }
    
    // GETTERS
    
    /**
     * Returns the block id.
     *
     * @return the block id
     */
    public int getId() {
        return id;
    }
    
    /**
     * Returns the block data.
     *
     * @return the block data
     */
    public byte getData() {
        return data;
    }
    
    // MISC
    
    @Override
    public int hashCode() {
        return id | data << 12;
    }
    
    @Override
    public String toString() {
        return id + ":" + data;
    }
    
    @Override
    public boolean equals(Object obj) {
        return obj instanceof LegacyBlockKey && equals((LegacyBlockKey) obj);
    }
    
    public boolean equals(LegacyBlockKey key) {
        return this.id == key.id && this.data == key.data;
    }
    
}
