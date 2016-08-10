package com.compass.loco.homelibrary.http;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by esaabbh on 8/9/2016.
 */
public class NativeThreadPool {
    private NativeThreadPool() {}

    private static int CORE_POOL_SIZE = 5;
    private static int MAX_POOL_SIZE = 100;
    private static int KEEP_ALIVE_TIME = 10000;

    private static BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(10);

    public static int getWorkQueue() {
        return workQueue.size();
    }

    private static ThreadFactory threadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            System.out.println("create new thread");
            return new Thread(r, "myThreadPool thread:"
                    + integer.getAndIncrement());
        }
    };

    // Create Thread Pool
    private static ThreadPoolExecutor threadPool;
    static {
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS, workQueue, threadFactory);
    }

    // run the target thread
    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }
}
