package com.lehow.flex.base;

import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;

/**
 * desc: 只含有基本的业务数据
 * author: luoh17
 * time: 2018/7/24 11:42
 */
public class InnerFlexField<T> {

  String key;
  String title;
  String hint;
  String summary;
  T value;

  public InnerFlexField(String key, T value) {
    this.key = key;
    this.value = value;
  }
}
