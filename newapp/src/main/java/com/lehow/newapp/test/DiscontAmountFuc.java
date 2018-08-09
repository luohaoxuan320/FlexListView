package com.lehow.newapp.test;

import android.text.TextUtils;
import android.util.Log;
import io.reactivex.functions.BiFunction;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/7 10:15
 */
public class DiscontAmountFuc implements BiFunction<String, String, String> {
  @Override public String apply(String s, String s2) throws Exception {
    double aamount = 0;
    if (!TextUtils.isEmpty(s)) {
      aamount = Double.parseDouble(s);
    }
    double discont = 0;
    if (!TextUtils.isEmpty(s2)) {
      discont = Double.parseDouble(s2);
    }
    return (aamount + discont) + "";
  }
}
