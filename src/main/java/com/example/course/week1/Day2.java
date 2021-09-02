//package com.example.course.week1;
//
//
//import java.util.*;
//
//public class Day2 {
//
//}
//
///**
// *  array
// *      int[] arr = new int[Integer.MAX_VALUE];
// *      fix size
// */
//
///**
// *   list / set / map
// *   list
// *   question: implement a list
// *         1. interface / abstract class
// *         2. get(int idx) / add(Object obj) ..
// *         3. thread safe or not
// *         4. generic type
// *         5. array(resize()) / Node(double / single)
// */
//class MyLinkedList<T> {
//    private int size = 0;
//    private Node first;
//    private Node tail;
//
//    public MyLinkedList() {
//        first = new Node();
//        tail = new Node();
//        first.next = tail;
//        tail.pre = first;
//    }
//
//    public T get(int idx) {
//        return null;
//    }
//
//    public void add(T val) {
//    }
//
//    private static class Node<T> {
//        Node next;
//        Node pre;
//        T val;
//    }
////    public static void main(String[] args) {
////        List<Object> myList = new ArrayList<>();
////        myList.add("SASDF");
////        myList.add(133);
//////        String s1 = myList.get(1);
////    }
//}
//
//class MyArrayList<T> {
//    Object[] values;
//    public MyArrayList(int size) {
//        values = new Object[size];
//
//    }
//}
//
///**
// *   iterator + modCount + concurrentModificationException
// */
//
////class IteratorTest {
////    public static void main(String[] args) {
////        List<Integer> list = new ArrayList<>();
////        list.add(5);
////        list.add(10);
////        list.add(15);
//////        //iterator -> modCount + 1 + fail fast
//////        for(int val: list) {
//////            list.add(0);
//////        }
////        //modCount = 6; expectedCount = 6;
////        Iterator itr = list.iterator();
////
////        while(itr.hasNext()) {
////            //modCount == expectedCount ?
////            System.out.println(itr.next());
////            //modCount++;
////            itr.remove();
////        }
////    }
////}
//// [5][4][4][5][x][x][x][x] -> remove(idx 0 ) -> [4][4][5][x][x][x][x][x]
//
///**
// *  diff between linkedlist and arraylist
// *  arraylist
// *          1. array
// *          2. access by index O(1) time
// *          3. resize -> O(N) time
// *          4. insert / delete first element -> O(N)
// *          5. continuous space
// *  linkedlist
// *          1. node + pointer
// *          2. random space
// *          3. insert ./ delete first element -> O(1)
// *          4. look up by index -> O(N)
// *          5. remove -> O(N) to find element + O(1) change pointers
// */
//
//
//
///**
// *  what is hashmap / do you know hashmap / have you used hashmap / ..
// *  = how does hashmap work / what is the internal flow of hashmap
// *  hashing(key) ->
// *
// *                  key value pairs
// *  [1][1][1][1][1][1][1][1]
// *  [][][][][100][][][]  O(N)
// *
// *
// *  HashMap
// *         key, value
// *         1. based on array(max length Integer.MAX_VALUE) -> need to resize in some situations
// *         2. in bucket -> linkedlist / redblacktree(balanced tree)
// *         3. hashcode
// *         4. equals
// *
// *         [][][O][][][][O][] Node<Key, Value>  Entry(key, value)
// *
// *         flow
// *         1. putVal(hash(key), key, value, false, true);
// *              hash(key) -> hash(key.hashcode)
// *         2. putVal(hashing value, key, value, false, true);
// *         3. get index from [i = (length of array - 1) & hash]
// *         4. if table[index] == null first node is null
// *            else
// *                  compare first node's key and our new key value
// *                  == || equals(==)
// *                  if first node is treenode -> look up on red black tree
// *                  else look up on linkedlist
// *                  if linkedlist size + 1 > tree threshold => resize linkedlist to redblacktree
// *         5. size + 1 > threshold (resize)
// *            resize()
// *
// *
// *      hashCode() -> 13 * id + 11;
// *      equals(Object)
// */
//
//
//class Student {
//    private int id;
//
//    public Student(int id) {
//        this.id = id;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id);
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(obj == this) {
//            return true;
//        }
//        if(obj == null || obj.getClass() != this.getClass()) {
//            return false;
//        }
//        Student s = (Student) obj;
//        return s.id == this.id;
//    }
//
//    public static void main(String[] args) {
//        Student s1 = new Student(1);
//        Student s2 = new Student(2);
//        Map<Student, Integer> map = new HashMap<>();
//        map.put(s1, 5);
//        //same object => same hashcode => same hashing value => same index
//        //same bucket => == || equals(==)  => s1 == s1
//        //System.out.println(map.get(s1)); // 5
//
//        //System.out.println(map.get(s2)); //null
//
////        Student s3 = new Student(1);
////        System.out.println(map.get(s3));
//        s1.id = 10;
//        System.out.println(map.get(s1));
//        s1.id = 1;
//        System.out.println(map.get(s1));
//
//        //hashcode always return 10;
//        Student s5 = new Student(1);
//        System.out.println(map.get(s5));
//    }
//}
//
//
///**
// *      TreeMap<Integer, Integer> map = new TreeMap<>();
// *      log(N)
// */
//
//class TreeMapTest {
//    public static void main(String[] args) {
//        TreeMap<Integer, Integer> map = new TreeMap<>();
//        map.put(10, 1);
//        map.put(1, 1);
//        map.put(5, 1);
//        System.out.println(map);
////        TreeMap<Student, Integer> stuMap = new TreeMap<>();
////        stuMap.put(new Student(1), 5);
////        System.out.println(stuMap);
//
//    }
//}
//
//
///**
// * HashSet(HashMap) vs TreeSet(TreeMap)
// *
// *
// * Stack interface  : first in last out
// *      implementation :
// *          LinkedList
// *          array
// *
// *  Stack bottom push(1), push(3), push(2) top
// *        pop() -> 2
// *
// *  queue : FIFO first in first out
// *       head offer (1), offer(3), offer(2) tail
// *        poll() -> 1
// *
// *
// *
// *  PriorityQueue<Integer> heap = PriorityQueue<>();
// *     poll -> max / min value in heap
// *
// *
// */
//
//class HeapTest {
//    public static void main(String[] args) {
//        PriorityQueue<Map.Entry<Integer, Integer>> heap = new PriorityQueue<>(
//                (e1, e2) -> e1.getValue() - e2.getValue()
//        );
//        Map<Integer, Integer> map = new HashMap<>();
//        map.put(1, 2);
//        map.put(2, 1);
//        for(Map.Entry<Integer, Integer> e: map.entrySet()) {
//            heap.offer(e);
//        }
//        //[{2=1}, {1,2]]
//        System.out.println(heap.peek()); //{2=1}
//        map.put(2, 3); //pq: [{2=3}, {1,2]]
//        System.out.println(map);
//        System.out.println(heap.poll());//{2=3}
//    }
//}
//
//
//class A {
//    public static class B {
//
//    }
//}
//
//
///**
// * 1. (jvm)heap + GC(finalize / reference type) + stack + volatile + synchronized(wait + notify) + Compare And Swap
// * 2. Thread
// * 3. runnable
//
//
//
// * 4. AbstractQueuedSynchronizer ReentrantLock
// * 5. thread-safe collection
// */
//
//class MyArrayList2<T> {
//    private final Object[] arr = new Object[5];
//    public T get(int idx) {
//        return (T)arr[idx];
//    }
//}