package com.lehow.newapp.test;

import io.reactivex.functions.BiFunction;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/7 13:59
 */
public class FirstAmountFuc implements BiFunction<Double, Integer, Double> {
  @Override public Double apply(Double aDouble, Integer integer) throws Exception {
    return aDouble * integer / 10;
  }
}
