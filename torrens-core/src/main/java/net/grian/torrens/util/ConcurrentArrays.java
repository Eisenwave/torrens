package net.grian.torrens.util.util;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class ConcurrentArrays {
    
    private static int threads;
    static {
        setMaxThreads( Math.min(1, Runtime.getRuntime().availableProcessors() / 2) );
    }
    
    public static synchronized void setMaxThreads(int threads) {
        if (threads < 0) throw new IllegalArgumentException("at least 1 thread required");
        ConcurrentArrays.threads = threads;
    }
    
    private ConcurrentArrays() {}
    
    public static <T> void forEach(T[] arr, Consumer<T> action) {
        Objects.requireNonNull(arr);
        Objects.requireNonNull(action);
        
        AtomicInteger index = new AtomicInteger();
        
        run(() -> new ConsumerWorker<>(arr, action, index), threads);
    }
    
    public static <A, B> void map(A[] in, B[] out, Function<A, B> func) {
        Objects.requireNonNull(func);
        if (out.length < in.length)
            throw new IllegalArgumentException("output arr too short to hold results of input array");
        
        AtomicInteger index = new AtomicInteger();
        
        run(() -> new FunctionWorker<>(in, out, func, index), threads);
    }
    
    /**
     * Runs several supplied threads.
     *
     * @param supplier the thread supplier
     * @param threads the thread count
     */
    public static void run(Supplier<? extends Thread> supplier, int threads) {
        Thread[] arr = new Thread[threads];
        
        for (int i = 0; i < threads; i++)
            arr[i] = supplier.get();
        
        run(arr);
    }
    
    /**
     * Runs all threads in the array.
     *
     * @param threads the threads
     */
    public static void run(Thread[] threads) {
        if (threads.length == 1)
            threads[0].run();
        
        else {
            for (Thread thread : threads) {
                thread.start();
            }
            
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static class ConsumerWorker<T> extends Thread {
        
        private final T[] arr;
        private final Consumer<T> action;
        private final AtomicInteger index;
        
        public ConsumerWorker(T[] arr, Consumer<T> action, AtomicInteger index) {
            this.arr = arr;
            this.action = action;
            this.index = index;
        }
        
        @Override
        public void run() {
            int i;
            while ((i = index.getAndIncrement()) < arr.length)
                action.accept(arr[i]);
        }
        
    }
    
    private static class FunctionWorker<A, B> extends Thread {
        
        private final A[] in;
        private final B[] out;
        private final Function<A, B> func;
        private final AtomicInteger index;
        
        public FunctionWorker(A[] in, B[] out, Function<A,B> func, AtomicInteger index) {
            this.in = in;
            this.out = out;
            this.func = func;
            this.index = index;
        }
        
        @Override
        public void run() {
            int i;
            while ((i = index.getAndIncrement()) < in.length)
                out[i] = func.apply(in[i]);
        }
        
    }
    
}
