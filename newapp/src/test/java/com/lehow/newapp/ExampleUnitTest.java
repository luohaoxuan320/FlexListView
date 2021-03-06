package com.lehow.newapp;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
  @Test public void addition_isCorrect() {
    // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
    BigDecimal bg = new BigDecimal(2.275).setScale(2, RoundingMode.UP);

    System.out.println("2.275=" + bg.toString() + " " + bg.doubleValue());

    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    System.out.println(decimalFormat.format(1234.50));
    String format1 = decimalFormat.format(12345.678);
    System.out.println(format1);
    try {
      System.out.println("format1=" + decimalFormat.parse(format1).doubleValue());
    } catch (ParseException e) {
      e.printStackTrace();
    }
    String format = DecimalFormat.getNumberInstance().format(12345.678);
    System.out.println("format=" + format);
    try {
      System.out.println(DecimalFormat.getNumberInstance().parse(format));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }
}