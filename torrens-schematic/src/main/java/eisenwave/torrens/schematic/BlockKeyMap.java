package eisenwave.torrens.schematic;

import org.jetbrains.annotations.*;

import java.util.*;

public class BlockKeyMap<T> implements Map<BlockKey, T> {
    
    private final Map<BlockKey, BlockKeyNode<T>> map = new LinkedHashMap<>();
    
    private BlockKeyNode<T> getOrInit(BlockKey stateless) {
        BlockKeyNode<T> result = map.get(stateless);
        if (result == null) {
            result = new BlockKeyNode<>();
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
            return node == null? null : node.getRoot();
        }
    }
    
    @Override
    public T put(@NotNull BlockKey key, @NotNull T value) {
        if (key.hasBlockState()) {
            BlockKey stateless = key.withoutBlockState();
            BlockKeyNode<T> node = getOrInit(stateless);
            
            Map<String, String> blockState = key.getBlockState();
            String[] keyState = new String[blockState.size() * 2];
            int index = 0;
            for (Map.Entry<String, String> entry : blockState.entrySet()) {
                keyState[index++] = entry.getKey();
                keyState[index++] = entry.getValue();
            }
            
            return node.put(keyState, value);
        }
        else return getOrInit(key).putRoot(value);
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
        return new EntrySet();
    }
    
    /* private static interface Node<T> {
        
        public T get(@Nullable Map<String, String> blockState);
        
    }*/
    
    private class EntrySet extends AbstractSet<Entry<BlockKey, T>> {
        
        @Override
        public int size() {
            return BlockKeyMap.this.size();
        }
        
        @Override
        public boolean isEmpty() {
            return BlockKeyMap.this.isEmpty();
        }
        
        @Override
        public void clear() {
            BlockKeyMap.this.clear();
        }
        
        @Override
        public boolean contains(Object o) {
            return BlockKeyMap.this.containsKey(o);
        }
        
        @Override
        public Iterator<Entry<BlockKey, T>> iterator() {
            return new EntrySetIterator(map.entrySet().iterator());
        }
        
    }
    
    private class EntrySetIterator implements Iterator<Entry<BlockKey, T>> {
        
        private final Iterator<Entry<BlockKey, BlockKeyNode<T>>> iterator;
        
        public EntrySetIterator(Iterator<Entry<BlockKey, BlockKeyNode<T>>> iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }
        
        @Override
        public Entry<BlockKey, T> next() {
            // TODO implement
            throw new UnsupportedOperationException();
        }
    }
    
    private static class BlockKeyNode<T> {
        
        // index 0 reserved for the root node, the rest is pairs of keys and values of block states
        private final List<Entry> branches = new ArrayList<>();
        private T rootValue;
        
        public BlockKeyNode() {}
        
        public int size() {
            int branchesSize = branches.size();
            return rootValue != null? branchesSize + 1 : branchesSize;
        }
    
        @Nullable
        public T getRoot() {
            return rootValue;
        }
    
        @Nullable
        public T get(@NotNull Map<String, String> blockState) {
            outer:
            for (Entry pair : branches) {
                String[] entryBlockState = pair.getKey();
                for (int j = 0; j < entryBlockState.length; j++) {
                    String stateKey = entryBlockState[j];
                    String stateValue = entryBlockState[++j];
                    //System.out.println(stateKey);
                    //System.out.println(blockState);
                    if (!blockState.get(stateKey).equals(stateValue))
                        continue outer;
                }
                return pair.getValue();
            }
            
            return null;
        }
        
        public T putRoot(T value) {
            T result = rootValue;
            rootValue = value;
            return result;
        }
        
        public T put(@NotNull String[] blockState, T value) {
            /* if (blockState == null) {
                T result = rootValue;
                rootValue = value;
                return result;
            } */
            
            for (int i = 0; i < branches.size(); i++) {
                Entry pair = branches.get(i);
                String[] key = pair.getKey();
                if (Arrays.equals(blockState, key)) {
                    T previous = pair.getValue();
                    branches.set(i, new Entry(key, value));
                    return previous;
                }
            }
            
            branches.add(new Entry(blockState, value));
            return null;
        }
        
        public boolean contains(Map<String, String> blockState) {
            return get(blockState) != null;
        }
        
        private class Entry {
            
            @NotNull
            private final String[] key;
            private final T value;
            
            public Entry(@NotNull String[] key, T value) {
                this.key = key;
                this.value = value;
            }
    
            public String[] getKey() {
                return key;
            }
    
            public T getValue() {
                return value;
            }
            
        }
        
    }
    
}
