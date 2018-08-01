package com.lehow.newapp;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() {

    int j = 1;
    int i = j + 2;
    System.out.println("i=" + i);
    j = 3;
    System.out.println("i=" + i);

    BehaviorSubject<Integer> behaviorSubject = BehaviorSubject.createDefault(1);

    Consumer<Integer> integerConsumer=new Consumer<Integer>() {
      @Override public void accept(Integer integer) throws Exception {
        System.out.println("accept=" + integer);
      }
    };
    //behaviorSubject.onNext(1);
    behaviorSubject.subscribe(integerConsumer);
    //behaviorSubject.subscribe(integerConsumer);
    //
    //behaviorSubject.onNext(2);


    //assertEquals(4, 2 + 2);
  }
}