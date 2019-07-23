package com.nowcoder;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jiawen.chen on 2019/7/22.
 */
public class MultiThread {
    public static void main(String[] args) {
//        testBlockingQueue();
//        testAtomic();
//        testThreadLocal();
//        testExecutor();

    }





    public static void testExecutor(){
//        ExecutorService  threadpool= Executors.newSingleThreadExecutor();
        ExecutorService  threadpool= Executors.newFixedThreadPool(2);
        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Execute1 " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        threadpool.execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("Execute2 " + i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        threadpool.shutdown();
    }



    private  static ThreadLocal<Integer> threadLocals=new ThreadLocal<>();

    public static void testThreadLocal(){
        for (int i = 0; i < 10; i++) {
            final int finalI=i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        threadLocals.set(finalI);
                        Thread.sleep(new Random().nextInt(1000));
                        System.out.println("ThreadLocal: "+threadLocals.get());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }


    public static void testAtomic(){
        testWithAtomic();
//        testWithoutAtomic();
    }

    static AtomicInteger atomicInteger=new AtomicInteger(0);
    static int count=0;

    private static void testWithAtomic() {
                        for (int i = 0; i <10 ; i++) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    for (int j = 0; j < 10; j++) {
                                        System.out.println(atomicInteger.incrementAndGet());
                                    }
                }
            }).start();
        }
    }

    private static void testWithoutAtomic() {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j <10; j++) {
                        count++;
                        System.out.println(count);
                    }
                }
            }).start();

        }
    }


    public static void testBlockingQueue(){
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        new Thread(new Producer(queue), "生产者1").start();
        new Thread(new Consumer(queue), "消费者1").start();
        new Thread(new Consumer(queue), "消费者2").start();
    }
    static class Producer implements Runnable {
        private BlockingQueue<Integer> queue;

        Producer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    static class Consumer implements Runnable {
        private BlockingQueue<Integer> queue;

        Consumer(BlockingQueue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true)
                System.out.println(Thread.currentThread().getName() + ":" + queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}