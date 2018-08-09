package com.lehow.newapp.util;

import android.os.Bundle;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/6 16:55
 */
public class DecimalUtil {

  public static String format(double value) {
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    return decimalFormat.format(value);
  }

  public static double parse(String value) {
    DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    try {
      return decimalFormat.parse(value).doubleValue();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return 0;
  }
}
