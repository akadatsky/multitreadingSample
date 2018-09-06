package com.akadatsky;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final long NUMBER = 99990001L;
    private static int THREAD_COUNT = 4;

    private static ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
    private static AtomicInteger finishedThreadsCount = new AtomicInteger(0);
    private static AtomicBoolean atomicResult = new AtomicBoolean(true);

    private static long time;

    public static void main(String[] args) {
        startOneThread();
        System.out.println("==========================");
        startFewThreads();
    }

    private static void startOneThread() {
        time = System.currentTimeMillis();
        boolean result = isPrime(2, NUMBER - 1);
        System.out.println("Result one thread: " + result);
        double tmp = (System.currentTimeMillis() - time) / 1000.0;
        System.out.println("Time: " + tmp);
    }

    private static void startFewThreads() {
        time = System.currentTimeMillis();

        long range = NUMBER - 2 - 1;
        long part = range / THREAD_COUNT;
        long remainder = range % THREAD_COUNT;

        long a = 2;
        long b = a + part + remainder;
        for (long i = 0; i < THREAD_COUNT; i++) {
            addThread(a, b);
            a = b + 1;
            b += part;
        }
    }

    private static void addThread(long a, long b) {
        System.out.println("Range: " + a + ".." + b);
        executor.execute(() -> {
            boolean result = isPrime(a, b);
            if (!result) {
                atomicResult.set(false);
            }
            if (THREAD_COUNT == finishedThreadsCount.incrementAndGet()) {
                System.out.println("Result few threads: " + atomicResult.get());
                double tmp = (System.currentTimeMillis() - time) / 1000.0;
                System.out.println("Time: " + tmp);
            }
        });
    }

    private static boolean isPrime(long a, long b) {
        boolean result = true;
        for (long i = a; i <= b; i++) {
            if (NUMBER % i == 0) {
                System.out.println("divisor: " + i);
                result = false;
            }
        }
        return result;
    }

}
