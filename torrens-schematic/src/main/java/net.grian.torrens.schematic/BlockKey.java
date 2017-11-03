package net.grian.torrens.schematic;

import net.grian.spatium.anno.MinecraftSpecific;

import java.io.Serializable;

@SuppressWarnings("unused")
@MinecraftSpecific
public class BlockKey implements Serializable {

    public final static BlockKey
    AIR = new BlockKey(0, 0),
    STONE = new BlockKey(1, 0);

    private final short id;
    private final byte data;
    
    /**
     * Constructs a new arbitrary block key.
     *
     * @param id the block id (positive)
     * @param data the block data (positive)
     * @throws IllegalArgumentException if id or data are negative
     */
    public BlockKey(short id, byte data) {
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
    public BlockKey(int id, int data) {
        this((short) id, (byte) data);
    }
    
    // GETTERS

    /**
     * Returns the block id.
     *
     * @return the block id
     */
    public short getId() {
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
        return id | data << 16;
    }

    @Override
    public String toString() {
        return id+":"+data;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlockKey && equals((BlockKey) obj);
    }

    public boolean equals(BlockKey key) {
        return this.id == key.id && this.data == key.data;
    }

}
