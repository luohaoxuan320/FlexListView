package com.lehow.newapp.util;

import android.os.Bundle;
import android.widget.EditText;
import java.lang.reflect.Method;
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

  public static void initSoftInput(EditText editText) {
    int currentVersion = android.os.Build.VERSION.SDK_INT;
    if (currentVersion >= 21) {
      editText.setShowSoftInputOnFocus(false);
    }
    if (currentVersion >= 16) {//调用隐藏API
      // 4.1.2
      try {
        Class<EditText> cls = EditText.class;
        Method setSoftInputShownOnFocus;
        setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
        setSoftInputShownOnFocus.setAccessible(true);
        setSoftInputShownOnFocus.invoke(editText, false);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
