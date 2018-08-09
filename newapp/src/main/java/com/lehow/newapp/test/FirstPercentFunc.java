package com.lehow.newapp.test;

import android.text.TextUtils;
import io.reactivex.functions.BiFunction;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/8 14:22
 */
public class FirstPercentFunc implements BiFunction<String, String, Integer> {
  @Override public Integer apply(String total, String first) throws Exception {
    if (TextUtils.isEmpty(total) || TextUtils.isEmpty(first)) {
      return -1;//不更新值
    }
    double t = Double.parseDouble(total);
    double f = Double.parseDouble(first);
    double percent = f * 10 / t;

    if (percent == (int) percent) {
      return (int) percent;
    }
    return 0;//自定义
  }
}
