package eisenwave.torrens.schematic;

import javafx.util.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BlockKeyMap<T> implements Map<BlockKey, T> {
    
    private final Map<BlockKey, BlockKeyNode<T>> map = new LinkedHashMap<>();
    
    private BlockKeyNode<T> getOrInit(BlockKey stateless) {
        BlockKeyNode<T> result = map.get(stateless);
        if (result == null) {
            result = new BlockKeyNode<>(null);
            map.put(stateless, result);
        }
        return result;
    }
    
    @Override
    public int size() {
        int size = 0;
        for (BlockKeyNode node : map.values())
            size += node.size();
        return size;
    }
    
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }
    
    @Override
    public boolean containsValue(Object value) {
        return get(value) != null;
    }
    
    @Override
    public T get(Object key) {
        return (key instanceof BlockKey)? get((BlockKey) key) : null;
    }
    
    public T get(BlockKey key) {
        if (key.hasBlockState()) {
            BlockKey stateless = key.withoutBlockState();
            BlockKeyNode<T> node = map.get(stateless);
            return node == null? null : node.get(key.getBlockState());
        }
        else {
            BlockKeyNode<T> node = map.get(key);
            return node == null? null : node.get(null);
        }
    }
    
    @SuppressWarnings("ConstantConditions")
    @Override
    public T put(@NotNull BlockKey key, @NotNull T value) {
        if (key.hasBlockState()) {
            BlockKey stateless = key.withoutBlockState();
            BlockKeyNode<T> node = getOrInit(stateless);
            
            Map<String, String> blockState = key.getBlockState();
            String[] keyState = new String[blockState.size() * 2 + 1];
            int index = 0;
            for (Map.Entry<String, String> entry : blockState.entrySet()) {
                keyState[++index] = entry.getKey();
                keyState[++index] = entry.getValue();
            }
            
            return node.put(keyState, value);
        }
        else return getOrInit(key).put(null, value);
    }
    
    @Override
    public T remove(Object key) {
        return null;
    }
    
    @Override
    public void putAll(@NotNull Map<? extends BlockKey, ? extends T> m) {
    
    }
    
    @Override
    public void clear() {
        this.map.clear();
    }
    
    @NotNull
    @Override
    public Set<BlockKey> keySet() {
        return this.map.keySet();
    }
    
    @NotNull
    @Override
    public Collection<T> values() {
        throw new UnsupportedOperationException();
    }
    
    @NotNull
    @Override
    public Set<Entry<BlockKey, T>> entrySet() {
        throw new UnsupportedOperationException();
    }
    
    /* private static interface Node<T> {
        
        public T get(@Nullable Map<String, String> blockState);
        
    }*/
    
    private static class BlockKeyNode<T> {
        
        // index 0 reserved for the root node, the rest is pairs of keys and values of block states
        private final List<Pair<String[], T>> branches;
        
        public BlockKeyNode(List<Pair<String[], T>> branches) {
            this.branches = branches;
        }
        
        public int size() {
            int branchesSize = branches.size();
            if (branches.get(0).getValue() == null)
                branchesSize -= 1;
            return branchesSize;
        }
        
        public T get(@Nullable Map<String, String> blockState) {
            if (blockState == null)
                return branches.get(0).getValue();
            
            outer:
            for (int i = 1; i < branches.size(); i++) {
                Pair<String[], T> pair = branches.get(i);
                String[] key = pair.getKey();
                for (int j = 0; j < key.length; j++) {
                    if (!blockState.get(key[j]).equals(key[++j]))
                        continue outer;
                }
                return pair.getValue();
            }
            
            return null;
        }
        
        @SuppressWarnings("ConstantConditions")
        public T put(@Nullable String[] blockState, T value) {
            if (blockState == null)
                return branches.get(0).getValue();
            
            for (int i = 1; i < branches.size(); i++) {
                Pair<String[], T> pair = branches.get(i);
                String[] key = pair.getKey();
                if (Arrays.equals(blockState, key)) {
                    T previous = pair.getValue();
                    branches.set(i, new Pair<>(key, value));
                    return previous;
                }
            }
            
            branches.add(new Pair<>(blockState, value));
            return null;
        }
        
        public boolean contains(Map<String, String> blockState) {
            return get(blockState) != null;
        }
        
    }
    
}
