package com.example.course.week1;


import java.sql.Array;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


public class Day1 {
    /**
     * TDD - Test driven development
     * Object
            stack vs heap
            everything is instance
            except primitive type (int / float / double / boolean)
            Object class
                toString();  print / log object
                hashCode();  not unique
                equals(Object obj);
                wait() / notify() synchronized{}
     * static
            method
            variable
            class : inner class
            static block vs non-static block
            cannot call non-static attribute/method from static method
     * pass by value / reference
     * OOP
     * final
     * exception
     * immutable
     * String
     */


    /**
     *  equals()
     */

//    private static class Student {
//        int id;
//        public Student(int id) {
//            this.id = id;
//        }
//
//        @Override
//        public String toString() {
//            return "Student{" +
//                    "id=" + id +
//                    '}';
//        }
//    }


    /**
     *  pass by value
     */
//    public static void get(int b) {
//        int a = b;
//        a = 10;
//        System.out.println(a);
//    }
//
//    //s 0x9999 -> 0x7777
//    public static void get(Student s) {
//        //s 0x9999 -> new Student(2)   0x6666
//        //s = new Student(2);
//        s = null;
//        System.out.println(s);
//    }
//
//    public static void main(String[] args) {
//        //s 0x8888  -> student instance(1) 0x7777
//        Student s = new Student(1);
//        //get(0x7777)
//        get(s);
//        System.out.println(s);
//    }


    /**
     * OOP
     *  Polymorphism :
     *          up casting : Object obj = new Student();
     *          runtime overriding/ compile time overloading
     *  Inheritance :
     *          class extends class / abstract class impl multiple interfaces
     *          interface extends multiple interfaces
     *  Encapsulation :
     *          access modifiers
     *          private , default(same package), protected(subclass from other package), public
     *  Abstraction :
     *          abstract class : mutable value
     *                           constructor / non-static block
     *          interface : functionalities
     *                      final value
     */
//    private static class B {
//        int id;
//
//        public B(int id) {
//            this.id = id;
//        }
//
//        public int getId() {
//            return id;
//        }
//    }
//
//    private static class A extends B{
//        public A(int id) {
//            super(id);
//        }
//    }

    /**
     * final
     *      class -> no extends from other classes
     *      variable -> cannot change it
     *      method -> cannot override
     *
     * immutable
     *      String s1 = "abc";  1. "abc"
     *      String s2 = s1 + "c";  2. "c"  , 3. StringBuilder -> "abcc"
     *      String s3 = s2;
     *      String s4 = new String("abc");
     *      s1 == s4 false
     *      s1.equals(s4) true / false
     *
     *      toCharArray() -> get new char[]
     *
     *      new String(char[])
     *      *  *  String s5 = s3 + s4;
     *
     */

    private static class Building {
        int size;
        String name;

        public Building(int size, String name) {
            this.size = size;
            this.name = name;
        }
    }
//
//    private static final Building building = new Building();
//    private static final int key = 5

    /**
     *   Runtime Exception vs Compile time Exception vs error
     *
     *   NullPointerException extends RuntimeException extends Exception
     *   IOException extends Exception
     *
     *   try {
     *       ...
     *   } catch {
     *       ...
     *   } finally {
     *
     *   }
     *
     *
     *   throws
     *
     *   java 7 try with resource (auto closable)
     *
     *
     *   throw multiple exceptions ???
     */
//    public boolean changeName(Building building){
//        if(building == null) {
//        }
//        if("Tddd".equals(building.name)) {
//            building.name = "XX";
//        }
//        return true;
//    }


    public static void main(String[] args) {
//        int[] arr = new int[]{1, 1, 1, 2, 2, 2};
        int[] arr = new int[400];
        for(int i = 0; i < 400; i++) {
            arr[i] = i / 200;
        }
        //20 * 0, 20 * 1 -> 1
        //200 * 0, 200 * 1 -> 0
//        Solution solution = new Solution(1);
//        System.out.println(solution.topK(arr));
        Integer s1 = 127;
        Integer s2 = 127;
        System.out.println(s1 == s2);
    }

}

/**
 *  top k -> int[] -> number with top k frequency
 *  [1,1,1,2,2] k = 1 -> return 1
 *  [1,1,1,2,2,2] k = 1 -> return 2
 */
//class Solution {
//    private int k;
//    public Solution(int k) {
//        this.k = k;
//    }
//    public List<Integer> topK(int[] arr) {
//        if(arr == null) {
//            throw new RuntimeException("input is not valid");
//        }
//        Map<Integer, Integer> count = new HashMap<>();
//        for(int i = 0; i < arr.length; i++) {
//            count.put(arr[i], count.getOrDefault(arr[i], 0) + 1);
//        }
//        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(
//                (v1, v2) -> {
//                    Integer c1 = count.get(v1);
//                    Integer c2 = count.get(v2);
//                    if(c1.equals(c2)) {
//                        return v2 - v1;
//                    } else {
//                        return c2 - c1;
//                    }
//                });
//        for(int key: count.keySet()) {
//            maxHeap.offer(key);
//        }
//        List<Integer> ans = new ArrayList<>();
//        while(!maxHeap.isEmpty() && k > 0) {
//            ans.add(maxHeap.poll());
//            k--;
//        }
//        return ans;
//    }
//}


/**
 * 1. bug free + 5min -> Leetcode white board
 * 2. review topics
 *
 * day2
 * 1. generic
 * 2. array vs ArrayList
 * 3. LinkedList
 * 4. Map : HashMap vs TreeMap(comparator) vs LinkedHashMap
 * 5. Set : HashSet vs TreeSet
 * 6. PriorityQueue
 * 7. Stack vs Queue
 *
 *
 * 1. (jvm)heap + GC(finalize / reference type) + stack + volatile + synchronized(wait + notify) + Compare And Swap
 * 2. Thread
 * 3. runnable
 * 4. AbstractQueuedSynchronizer ReentrantLock
 * 5. thread-safe collection
 *
 * next week
 * day 5 -> java 8
 * day 6 -> database
 *
 *
 *
 * 1:30 CDT,  3:30 / 4:30
 */