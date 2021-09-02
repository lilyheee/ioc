package com.example.course.week1;

import java.util.concurrent.atomic.AtomicInteger;

public class Day4 {
    public static void main(String[] args){

    }
}
/**
 *  volatile : 1.visibility 2. happen-before 3. prevent reordering
 *  synchronized : critical section (only one thread can access)
 *                 lock / monitor scope
 *  Thread
 *          sleep
 *          wait (in synchronized)
 *
 *
 */

class WaitTest {
    public static void main(String[] args) throws InterruptedException {
        Object obj1 = new Object();
        Object obj2 = new Object();
//        obj.wait(); //exp
        synchronized (obj1) { //main thread holds obj1's monitor
            //obj2.wait(); //exp  main thread try to release obj2's monitor
            obj1.wait(); //main thread is waiting under obj1's waiting list
        }
    }
}


/**
 *  drawbacks of the synchronized
 *          1. performance slow -> only thread can access critical section
 *          2. only one waiting list
 *          3. cannot lock from one method , unlock from another method
 *          4. we don't have try lock
 *          5. doesn't have fair lock
 *
 *  increment number i in thread safe environment ?
 *
 *
 *  CAS compare and set instruction -> atomic
 *
 *  compareAndSetInt return boolean  Thread safe
 *              return true : success
 *              return false : failed
 *              (Object o, long offset, int expected, int x);
 *                  Stu         age         10          15
 */
class Test {
//    private volatile int i;
//    public synchronized void increment() {
//        i += 1;
//    }
    private AtomicInteger i;

    public Test(int v) {
        this.i = new AtomicInteger(v);
    }
}

/**
 *
 *      drawbacks of the synchronized
 *  *          1. performance slow -> only thread can access critical section
 *  *          2. only one waiting list
 *  *          3. cannot lock from one method , unlock from another method
 *  *          4. we don't have try lock
 *  *          5. doesn't have fair lock
 *
 *      volatile
 *      cas : atomic operation
 *            return true (if success) or false (if fail)
 *            thread safe
 *
 *
 *      create my lock:
 *      flag = true : has lock
 *      flag = false : no threads holding lock
 *
 *      first way. AtomicBoolean
 *      2nd way  . volatile flag + CAS
 *
 *      not thread safe
 *      Version1
 *      class MyLock {
 *          private volatile boolean flag = false;
 *          public boolean tryLock() {
 *              if(!flag) {
 *                  return flag = true;
 *              } else {
 *                  return false;
 *              }
 *          }
 *      }
 *      thread safe
 *      Version 2
 *      class MyLock {
 *          private volatile boolean flag = false;
 *          public boolean tryLock() {
 *              if(Unsafe.compareAndSet(this, flag, expected = false, true)) {
 *                  flag = true;
 *              } else {
 *                  return false;
 *              }
 *              return true;
 *          }
 *      }
 *
 *      lock multiple times + unlock multiple times
 *      Version 3
 *      class MyLock {
 *          private volatile int status = 0;
 *          private volatile Thread lockHolder = null;
 *          public boolean tryLock() {
 *              if(Unsafe.compareAndSet(this, status, expected = 0, new = 1)) {
 *                  lockHolder = Thread.currentThread();
 *                  return true;
 *              } else {
 *                  return false;
 *              }
 *          }
 *
 *          //fair lock impl
 *          public void lock() {
 *              if(lockHolder == Thread.currentThread()) {
 *                  status += 1;
 *              } else {
 *                  if(wait queue is not empty()) {
 *  *                   add current thread to queue
 *  *               }
 *                  if(Unsafe.compareAndSet(this, status, expected = 0, new = 1)) {
 *  *                  lockHolder = Thread.currentThread();
 *  *               } else {
 *  *                  // put current thread into a waiting queue
 *  *                  //linkedlist
 *  *               }
 *              }
 *          }
 *
 *          public void unlock() {
 *              //check thread owner
 *              //signal / awake / notify the first waiting thread in our waiting queue
 *          }
 *      }
 *      1. lockholder.unlock()
 *      2. lockholder signal first waiting thread + change status
 *      new Thread() try to get lock at same time
 *      3. first waiting thread try to get lock
 *
 *      lock -> 3:00 T1 (fail)
 *              3:10 T2 (fail)
 *              3:20 T3 (fail)
 *      unlock -> T1 -> T2 -> T3
 *      unfair lock :T4 can compete with T1  -> T4 can get lock before T1
 *      fair lock : T1 must get lock , T4 should wait at the end of the waiting queue
 *
 *      Head<Thread> -> Node(Thread) -> Node(Thread) -> Tail(Thread)
 *      CAS -> tail -> append new node to the tail
 *
 *      **
 *          Condition : await + signal
 *          synchronized : wait + notify
 *
 *
 *
 *
 *

 */


/**
 *   how to implement a thread safe queue
 *
 *   1. lock() + unlock()
 *   2. synchronized method + synchronized(queue object)
 *   3. (?) synchronized head + synchronized tail
 *   4. Reentrant lock + condition ====> blocking queue
 *   5. CAS linkedlist queue
 *
 *
 *   Blocking queue
 *   producer ->                               -> consumer
 *   T1                                             T5
 *   T2           [][][][][][][]...[][][][][]       T6
 *   T3                     len = 50                T7
 *   T4
 *
 *   if queue is full,  let producer wait
 *   if queue is empty, let consumer wait
 *   when consumer poll() from queue successfully -> consumer notify producer (queue is not full)
 *   when producer offer(E) into queue successfully -> producer notify consumer (queue is not empty)
 *
 *
 *   class MyBlockingQueue<T> {
 *       private final Object[] queue;
 *       private int size = 0;
 *       private int start = 0, end = 0;
 *       private int capacity = 16;
 *
 *       private final ReentrantLock lock = new ReentrantLock();
 *
 *       private final Condition full = lock.newCondition();
 *       private final Condition empty = lock.newCondition();
 *       public MyBlockingQueue() {
 *           this.queue = new Object[16];
 *       }
 *
 *  *   if queue is full,  let producer wait
 *  *   if queue is empty, let consumer wait
 *  *   when consumer poll() from queue successfully -> consumer notify producer (queue is not full)
 *  *   when producer offer(E) into queue successfully -> producer notify consumer (queue is not empty)
 *
 *       public boolean offer(T e) {
 *           lock.lock(); throw unchecked exception
 *           try {
 *               //queue is full
 *               while(size == queue.length) {
 *                   full.await();
 *               }
 *               queue[end++] = e;
 *               end %= queue.length;
 *               size++;
 *               empty.signal();
 *           } finally {
 *               lock.unlock(); //we don't
 *           }
 *       }
 *       public T poll() {
 *           lock.lock();
 *           try {
 *               while(size == 0) {
 *                   empty.await();
 *               }
 *               queue[start++] = null;
 *               start %= queue.length; //start = start + 1 => start == queue.length => reset start = 0 / if
 *               size--;
 *               full.signal();
 *           } finally {
 *               lock.unlock();
 *           }
 *       }
 *   }
 *
 *   synchronized =>
 *   [] array length = 50
 *   3:00  200 producers
 *   3:01  150 producers waiting in synchronized waiting list + array is full
 *   3:02  200 consumers
 *   3:03  (150 consumers + 100 producers) waiting in synchronized waiting list + 50 producers try to produce + empty array
 *   3:04  notify()
 *
 *   ****************
 *   Where to use ?
 *          Thread pool
 *
 *   Day5: ThreadPool(fix / cache / schedule) + ConcurrentAPI(concurrent hashmap / countdown latch / semaphore / CyclicBarrier)
 *   Day6: java 8(stream api / functional interface)
 *   Day7: database introduction(query on (Oracle live sql))
 *   Day8: transaction + pl/sql
 *
 */




