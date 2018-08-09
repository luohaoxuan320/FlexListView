package com.lehow.newapp.test;

import android.text.TextUtils;
import io.reactivex.functions.BiFunction;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/7 13:59
 */
public class FirstAmountFuc implements BiFunction<String, Integer, String> {
  @Override public String apply(String aDouble, Integer integer) throws Exception {
    double total = 0;
    if (!TextUtils.isEmpty(aDouble)) {
      total = Double.parseDouble(aDouble);
    }
    if (integer == 0) {//自定义时
      return "";//不更新当前值
    }
    return total * integer / 10 + "";
  }
}
