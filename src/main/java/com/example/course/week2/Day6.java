package com.example.course.week2;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Day6{
}

/**
 *  Java 8
 *      changed concurrenthashmap / hashmap
 *      remove perm gen -> meta space
 *      functional interface  -> contains 1 abstract method
 *          1. comparator : int compare(T t1, T t2);
 *          2. runnable : void run();
 *          3. callable : T call(R r) throw Exception;
 *          4. consumer : void accept(T t);
 *          5. function : R apply(T t);
 *          6. supplier : T get();
 *          7. predicate: boolean test(T t);
 *
 *
 *
 */
@FunctionalInterface
interface MyFunctionalInterface {
    void get();
}
class LambdaTest {
    public static void main(String[] args) {
        Comparator<Integer> cpt = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };
        Comparator<Integer> cpt2 = (o1, o2) -> {
            return o1 - o2;
        };
        Comparator<Integer> cpt3 = (o1, o2) -> o1 - o2;
        Runnable run1 = () -> System.out.println("this is ");
        Supplier<Object> sp1 = () -> new Object();
    }
}

/**
 *  Functional programming
 *      1. func1 -> get result -> func2 -> get result -> func3
 *      2. func1().func2().func3();

 *  for loop :
 *  list<Integer> -> * 2 -> get new List
 *  List<Integer> list = new ArrayList<>();
 *  for(...) {
 *      list.add(..);
 *  }
 *  return list
 *
 *  stream api :
 *      map(function)
 *      filter(predicate)
 *      forEach(consumer)
 *      collect(supplier, accumulator, combiner)
 *      reduce(result value, bifunction)
 *      Optional.ofNullable().orElse()
 *
 *
 *      return list.stream().map(v -> v * 2).collect(Collectors.toList());
 *
 *  collect() :
 *          <R> R collect(Supplier<R> supplier () -> return obj
 *                   BiConsumer<R, ? super T> accumulator (v1, v2) -> no return
 *                   BiConsumer<R, R> combiner (v1, v2) -> no return
 *           <R, A> R collect(Collector<? super T, A, R> collector);
 */
class StreamApiTest {
    public static List<Integer> mutiplyTwoVersion1(List<Integer> list) {
        //list.stream() -> copy objects into jvm
        return list.stream()
                //for every object, we will call v -> v * 2
                .map(v -> v * 2)
                //collect
                .collect(Collectors.toList());
    }

    public static List<Integer> mutiplyTwoVersion2(List<Integer> list) {
        return list.stream().map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(Integer input) {
                return input * 2;
            }
        }).collect(Collectors.toList());
    }

    public static List<Integer> mutiplyTwoVersion3(List<Integer> list) {
        //list.stream() -> copy objects into jvm
        return list.stream()
                //for every object, we will call v -> v * 2
                .map(v -> v * 2)
                //collect with supplier , biconsumer, biconsumer
                .collect(() -> new ArrayList<>(), (res, val) -> res.add(val), (l1, l2) -> l1.addAll(l2));
    }

    public static Map<Integer, Long> countFreqVersion1(List<Integer> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public static Map<Integer, Long> countFreqVersion2(List<Integer> list) {
        return list.stream()
                .collect(() -> new HashMap<>()
                        , (map, val) -> map.put(val, map.getOrDefault(val, 0l) + 1)
                        , (m1, m2) -> m1.putAll(m2));
    }

    public static void main(String[] args) {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 1, 2, 2, 4);
//        List<Integer> list2 = mutiplyTwoVersion1(list1);
//        System.out.println(list2 == list1);
        System.out.println(countFreqVersion2(list1));
    }
}
/**
 *   class stu {firstname, lastname, age}
 *   write stream -> filter age > 30
 *   input list
 *
 *   firstname / lastname null ? no
 *   age invalid < 0 ? no
 *   null list ? yes
 *   return List
 */
class StudentStreamDay6{
    public static void main(String[] args) {

    }
    public static List<StudentDay6> filterStudentByAge(List<StudentDay6> students) {
//        if(students == null) {
//            throw new IllegalArgumentException("...");
//        }
        return Optional.ofNullable(students).orElse(new ArrayList<>()).stream().filter(s -> s.getAge() > 30).collect(Collectors.toList());
    }

    public static int sumOfStudentAge(List<StudentDay6> students) {
        //..
        return students.stream().map(s -> s.getAge()).reduce(0, (res, val) -> res + val);
    }
}

class StudentDay6 {
    private String firstName;
    private String lastName;
    private int age;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}

/**
 *  List<Integer> list -> List<List<Integer>>  idx 0 even list,  idx 1 odd list
 */

class EvenOddListSolutionDay6 {
    private static List<List<Integer>> version1(List<Integer> list) {
        //32     000000....00001    2: 10    3: 11
        List<Integer> even = list.stream().filter(x -> (x & 1) == 0).collect(Collectors.toList());
        List<Integer> odd = list.stream().filter(x -> (x & 1) == 1).collect(Collectors.toList());
        return Arrays.asList(even, odd);
    }
    //reduce ???
    private static List<List<Integer>> version2(List<Integer> list) {
        //all Integers -> List<List<
        return null;
    }

    //collect
    private static List<List<Integer>> version3(List<Integer> list) {
        return list.stream()
                .collect(() -> Arrays.asList(new ArrayList<>(), new ArrayList<>()),
                        (res, val) -> res.get(val % 2).add(val),
                        (l1, l2) -> {
                            l1.get(0).addAll(l2.get(0));
                            l1.get(1).addAll(l2.get(1));
                        }
                );
    }

    public static void main(String[] args) {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6 ,7, 8);
//        System.out.println(version1(input));
//        System.out.println(version3(input));
        System.out.println(input);
        input.forEach(x -> System.out.print(x));
        input.forEach(System.out::print);
        input.stream().forEach(System.out::print);
    }
}
/**
 *   parallel stream(combiner) + completablefuture
 *                               [1,2] T1 -> list1[2, 4]
 *   [1,2,3,4,5] -> forkjoinpool [3,4] T2 -> list2[6, 8]    combiner.. -> [2, 4, 6, 8, 10]
 *                               [5]   T3 -> list3[10]
 */

class ParallelStreamTest {
    public static void main(String[] args) {
        List<Integer> input = Arrays.asList(1, 2, 3, 4, 5, 6 ,7, 8);
        input.parallelStream()
                .map(x -> {
                    System.out.println("step 1 " + x);
                    return x * 2;
                })
                .filter(x -> {
                    System.out.println("step 2 " + x);
                    return x > 5;
                }).collect(Collectors.toList());
    }
}

/**
 *  CompletableFuture
 */
class CompletableFutureTest {
    public static void main(String[] args) {
            //step 1 provide a value 5
//        int res = CompletableFuture.supplyAsync(() -> 5)
//                .thenApply(x -> x * 2)
//                .join();
//        System.out.println(res);
        CompletableFuture.supplyAsync(() -> {
            return "abc";
        }).orTimeout(5, TimeUnit.SECONDS);
        System.out.println(allOfTest(Arrays.asList("a", "b", "c", "d")));
    }

    /**
     *      getUserDetail(int id)
     *          read from
     *          address(int id) -> return address String of this user id  2s
     *          userInfo(int id) -> return user information string of this user id  2s
     *          return combined string to user
     *
     *       CompletableFuture<String> userAddress = CompletableFuture.supplyAsync(() -> address(id));
     *       CompletableFuture<String> userInfo = CompletableFuture.supplyAsync(() -> userInfo(id));
     *       String res = userAddress.join() + userInfo.join();
     *
     *
     */

    public static String allOfTest(List<String> idList) {
        List<CompletableFuture<String>> completableFutureList = new ArrayList<>();
        for(int i = 0; i < idList.size(); i++) {
            String id = idList.get(i);
            final int idx = i;
            completableFutureList.add(CompletableFuture.supplyAsync(() -> id + "--" + idx + "--" + Thread.currentThread() + "\n"));
        }
        CompletableFuture f = CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0]));
        return (String)f.thenApply(Void -> {
                    StringBuilder ans = new StringBuilder();
                    for(CompletableFuture future: completableFutureList) {
                        ans.append(future.join());
                    }
                    return ans.toString();
                }).join();
    }


    //
}

/**
 *      comparable
 *              compareTo(Object obj) : compare this with obj
 *
 *      comparator(collections)A
 *              compare(Object obj1, Object obj2) :
 *
 *             \\s+
 *
 *
 *
 *     Day 7
 *          select, from, where, group by , having, order by
 *          inner join, outer join, cross join
 *          rank(), dense_rank()
 *
 *
 */

class TestDay6{
    public void sort(List<String> list) {

    }
}
