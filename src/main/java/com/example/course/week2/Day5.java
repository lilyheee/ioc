package com.example.course.week2;

import java.util.concurrent.*;

public class Day5 {
}


/**
 *  Dead lock
 *      T1(A)  -> try to get monitor B
 *      T2(B)  -> try to get monitor A
 *
 *      T1 {
 *          synchronized(A) {
 *              synchronized(B) {
 *
 *              }
 *          }
 *      }
 *
 *      T2 {
 *          synchronized(B) {
 *              synchronized(A) {
 *
 *              }
 *          }
 *      }
 *  how to solve dead lock ?
 *  1.  timeout (try lock)
 *  2.  lock in order
 *  3.  look up table
 *          locking table
 *          A : T1
 *          B : T2
 *          tryLock table
 *          T1 : B
 *          T2 : A
 *          *****************
 *          try lock table
 *          A : T2(B)
 *          B : T1(A)
 *
 */


/**
 * Blocking Queue
 *      thread safe queue
 *      queue is empty -> block consumer
 *      queue is full  -> block producer
 * Thread pool(Blocking queue)
 *      why thread pool ?
 *      how to impl it ?
 *          fixed length array ?    -> manage threads
 *          create 1 thread first ? -> default thread size
 *          Thread pool class ?     -> OOP
 *          Thread is avaliable ?   -> current alive threads ?
 *
 *          1. worker Thread (extends Thread, wrap thread reference)
 *              for(;;) / while(true)
 *          2. blocking queue hold messages
 *              delayedWorkQueue -> scheduledThreadPool
 *          3. thread number(core pool size, max pool size)
 *              core pool size = max pool size => fix thread pool
 *              core pool size != max pool size => cached thread pool (1, Integer.MAX_VALUE)
 *
 *              task execution time = waiting time + service time
 *              (?)task -> 90% 80% service time -> Cpu core number + 1
 *              (?)task -> mainly IO based  ->   1 / (service / task execution time)
 *                                       ->   1 / (service / (service + waiting))
 *                                       ->  (service + waiting) / service
 *                                       for single cores
 *                                       ->  1 + waiting / service
 *
 *
 *          types
 *                                          T1
 *              [][][][][][][][][][][][]    T2
 *                                          T3
 *              1. fixed thread pool  (min = max)
 *              2. cached thread pool (min, max, alive time, time unit)
 *              3. scheduled thread pool (delayed priority queue)
 *                              T1 [][][]
 *              [][][][][][]    T2 [][][]
 *                              T3 [][][]
 *              4. forkjoin pool(fork() / join())
 *                  Stealing algorithm
 *
 *         thread pool usage
 *              1. get user info -> get address -> get order info
 *              2. get user info
 *                 get address
 *                 get order info
 *
 *   1. diff ExecutorService and ThreadPoolExecutor
 *   2. why thread pool
 *   3. how does ThreadPoolExecutor work
 *   4. diff ThreadPoolExecutor vs forkJoinPool
 *   5. diff fix thread pool vs cached thread pool
 *   6. Future vs CompletableFuture(java 8)
 *   coding..
 *
 *   how to keep your application thread safe
 *
 */
/**
 *  S -> Single Responsebility
 *  O -> open close
 *  LID
 *
 *  Future.get() -> blocking function
 *
 *  (?)spring webflux(netty) -> event loop
 */
class ThreadPoolConfiguration{
    private static final ExecutorService pool = Executors.newFixedThreadPool(5);
    public static ExecutorService getter() {
        return pool;
    }
}
class ThreadPoolTest {
    private static volatile int v1 = 0;
    private static volatile int v2 = 0;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int val = callableResult(1, 100);
        System.out.println(val);
    }
    //func1(sum 1 - 100) -> future get result -> func2(value * 2) -> future get result -> func3(print console)
     // completableFuture

    /*
        1 - 25 , 26 - 50
     */
    // funct1(get value from 1- 100) -> func2 (val * 2) -> func3(print)
    /*
          1. wrap func1 with callable, submit
          2. future to get all of the result
          3. list into func2
          4. future to get result
          parallelStream ,
     */
    public static int callableResult(int start, int end) throws ExecutionException, InterruptedException {
        ExecutorService pool = ThreadPoolConfiguration.getter();

        //we should wrap functions into callable and get the result
//        int s1 = sumOddOrEven(start, end);
//        int s2 = sumOddOrEven(start + 1, end);
        Callable<Integer> sum1 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                Thread.sleep(5000);
                return sumOddOrEven(start, end);
            }
        };
        Callable<Integer> sum2 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return sumOddOrEven(start + 1, end);
            }
        };
        Future<Integer> f1 = pool.submit(sum1);
        Future<Integer> f2 = pool.submit(sum2);
        return f1.get() + f2.get();
    }

    public static int sumOddOrEven(int start, int end) {
        int ans = 0;
        while(start <= end) {
            ans += start;
            start += 2;
        }
        return ans;
    }

    public static int runnableResult(int start, int end) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            v1 = sumOddOrEven(start, end);
        });
        Thread t2 = new Thread(() -> {
            v2 = sumOddOrEven(start + 1, end);
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        // (1 + 100) * 100 / 2 = 5050
        return v1 + v2;
    }
}
/**
 *              f1  -> node1
 *    [file]    f2  -> node2       reduce
 *              f3  -> node3
 */

/**
 *  teacher gives paper exams to everyone
 *    1 teacher, 50 students(countDownLatch.await())
 *
 *    CountDownLatch latch = new CountDownLatch(50);
 *    latch.countDown()
 *    latch.await();
 */

/**
 * Day6
 * Java 8
 *      functional interface
 *      lambda expression
 *      stream
 *      Optional
 *      parallel stream
 *      completableFuture
 *
 *      Singleton..Builder
 *
 * Day 7 Database
 */