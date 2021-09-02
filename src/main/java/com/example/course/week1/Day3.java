package com.example.course.week1;

public class Day3 {

}

/**
 *     JVM : heap + stack
 *     Thread(method frame)
 *          Main Thread (stack push(recursion(5)) push(recursion(4) ... push(recursion(1))
 *          Thread1 (stack)
 *          Thread2 (stack)
 *     Heap
 *          before java 8 System.gc()
 *          [ eden ] [survivor0] [survivor1]    young generation   (parallelNewGC / parallelGC(multi-core + large heap)) (STW)
 *          [        old                   ] old generation    (parallelOldGC(mark and compact)(STW) / ConcurrentMarkSweep)
 *          [                               ]  perm generation (method info / class info) (only full gc)
 *
 *          new Object() -> eden (full / after few gc) -> promote s1 / s0 -> promote old(when hit certain age or previous gens are full)
 *
 *          after java 8
 *          remove perm -> metaspace (native heap)(full)
 *    STW -> stop the world pause
 *    GC
 *          GC roots : Threads / classloader....
 *          minor GC : parallelNewGC, parallelGC
 *          major GC :  ConcurrentMarkSweep :
 *                       (?)1. initial mark (STW)
 *                          2. concurrent mark
 *                          3. final mark (STW)
 *                          4. concurrent sweep
 *                     parallelOldGC
 *         full GC  : clean up both young + old generation
 *
 *        after java 9 G1 default GC algorithm / after java 14 CMS is moved
 *      (?)G1 : regions
 *          [][][][][][][]
 *          [][S][][][][][]
 *          [][][][O][][][]
 *          [][][][][][][]
 *          minor gc (whole young gen)
 *          mixed gc (whole young gen + a little bit old generation)
 *          1. inital mark (STW)
 *          2. concurrent mark
 *          3. final mark (STW)
 *          4. find alive region + no-alive region / update .. (STW)
 *          adjust pause time -> to reduce pause time
 *          full gc : single thread -> serial gc
 *
 *
 *        https://www.oracle.com/webfolder/technetwork/tutorials/obe/java/gc01/index.html
 *        java memory model
 *
 *   (?)Strong Reference / Soft Reference / Weak Reference / Phantom Reference
 *   String a = new Object(); -> strong reference
 *
 *   SoftReference : objects will be removed when heap is full
 *   WeakReference :
 *   Phantom Reference + Reference Queue : replaced finalize  / remove largeObj1 then load obj2
 *
 *   software analyze (java mission control / eclipse memory analyzer) -> heap dump(snapshot)
 *   ******************************************************************************************************
 */

class ThreadTest {
    public static void main(String[] args) throws ClassNotFoundException {
//        System.out.println(A.a);
//        Class<?> a = Class.forName("com.example.course.week1.A");
        new A();
    }
}

class A {
    static {
        System.out.println("i'm loaded");
    }
    public static int a = 5;
}


/**
 *  *   Thread
 *  *          1. creating thread : extends / new Thread(impl runnable) / Executors.new..ThreadPool
 *  *
 *  *   Thread lifecycle
 *             1. create thread -> runnable
 *             2. blocked by monitor
 *             3. sleep -> doesn't release lock
 *             4. wait -> notify
 *  *
 */
class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("i 'm new thread1");
    }
}
class Main {
    public static void main(String[] args) {
        Thread t1 = new MyThread();
        Thread t2 = new Thread(() -> System.out.println("i'm new thread 2"));
        t1.start();
        t2.start();
    }
}

/**
 *  http://gee.cs.oswego.edu/dl/jmm/cookbook.html
 *  https://www.cs.umd.edu/users/pugh/java/memoryModel/jsr-133-faq.html#jsr133
 *
 *  volatile
 *      1. prevent reordering from JVM optimizer
 *      2. visibility -> when we change value -> value will be written back to main memory
 *      3. happen-before rule((?)barrier / mfence(storeload))
 *              (?)read write   read write write write
 *  Timeline ->
 *
 *  CPU1            CPU2
 *   L1             L1
 *   L2             L2
 *
 *   T1             T2
 *  while(i + 1)     while(i + 1)
 *      Main memory(heap)   int i = 1;          -> T3 read i = 1
 *
 *
 *  CPU1            CPU2
 *  t1  +1             t2 + 1
 *  read = 1
 *  update = 2
 *                  read = 1
 *  write 2
 *      main memory (i = 1)
 *      1. read 2. write
 *
 */

class VisibilityTest {
    private static volatile Boolean flag = false;
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while(!flag) {
                if(flag) {
                    break;
                }
            }
            System.out.println("i m here, out of while loop");
        });
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            flag = true;
        });
        t1.start();
        t2.start();
        t1.join(); //current thread will wait t1 finish
        t2.join(); //current thread will wait t2 finish
    }
}

class ReorderTest {
    private int a = 1;
    private volatile int b = 1;
    public void m1() {
        a = 2;
        b = 2;
    }

    public void m2() {
        if(b == 2) { //if we can get b == 2, we will get a == 2
            System.out.println(a);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 10000; i++) {
            ReorderTest t = new ReorderTest();
            Thread t1 = new Thread(() -> t.m1());
            Thread t2 = new Thread(() -> t.m2());
            t1.start();
            t2.start();
            t1.join();
            t2.join();
        }
    }
}

/**
 *  synchronized (monitor)
 *   1. thread safety
 *   2. create a critical section (only one thread can access this part)
 *   3. no fair lock
 *   4. synchronized only works for object(object scope)
 *          synchronized non-static block -> this
 *          synchronized(object) -> object
 *          synchronized static block -> class object
 *
 *   T1(10min), T2(5s), T3, T4 -> try to get synchronized monitor
 *
 *   T1(10min), T3, T4 -> T2 holding synchronized monitor
 */
class SyncTest {
    private int i = 0;
    public synchronized void inc(int a) {
        i += a;
    }
    /*
            1. t1 run first, will get "this" monitor
            2. t1 run print1() => "i m non-static block" and start sleeping for 3s
                after 0.5 sec
            3. t2 start running, will get "class object" monitor
            4. t2 run print2() => "i m static block" => then release monitor
                after 2.5 sec
            5. t1 stop sleeping => "sleep end in print1" => then release monitor
     */
    public synchronized void print1() {
        System.out.println("i m non-static block" + "-" + Thread.currentThread());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }
        System.out.println("sleep end in print1");
    }
    public static synchronized void print2() {
        System.out.println("i m static block" + "-" + Thread.currentThread());
    }

    public static void main(String[] args) throws InterruptedException {
        SyncTest s = new SyncTest();
//        Thread t1 = new Thread(() -> s.inc(5));
//        Thread t2 = new Thread(() -> s.inc(5));
//        synchronized (SyncTest.class) { //lock other objects still providing happen before??
//
//            System.out.println("i'm the main method lock");
//        }
        Thread t1 = new Thread(() -> s.print1(), "thread 1");
        Thread t2 = new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            s.print2();
        }, "thread 2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}

/**
 *   (?)in x86 processor(Strong memory model)
 *      read1 write1 read1 write(volatile) storeLoad(mfence / lock prefix => will trigger cache flush => to main memory)  .read
 *
 *   monitorenter(flush local cache -> memory)
 *   synchronized{
 *          (read from memory)
 *
 *          (flush all your change to main memory)
 *          monitorexit
 *   }
 *
 *   synchronized {
 *       read
 *   }
 *
 *  CAS + atomic
 *  ReentrantLock
 *  Blocking queue
 *  ...
 */