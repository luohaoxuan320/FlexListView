package com.lehow.flex.base;

import android.os.Bundle;

/**
 * desc:对字段进行辅助处理1.点击事件的处理，2.数据变化的处理
 * author: luoh17
 * time: 2018/7/26 20:06
 */
public abstract class FlexFieldProcessor<T> {
  /**
   * 处理Field被点击的事件
   */
  public void onFieldClick(FlexField<T> flexField, int adapterPosition) {
  }

  ;

  /**
   * 注意要在这里主动 setValue更新数据，
   * 处理返回的数据，如value到summary的映射
   * 通知变化目前的来源有三个
   * 1.EditText输入 主动触发（bundle不为null）
   * 2.onActivityResult返回bundle不为null）
   * 3.其他值变化，级联通知的更新，这个时候value直接在内部赋值了，此时bundle为null。这里之所以直接内部对value赋值，是因为value是泛型，不好在bundle中注入
   *
   * @param isSelf 是否是自己主动的变化，true，对应1,2。false 对应3
   * @return true：改动的数据被接纳，false 改动的 数据被丢弃，主要方便EditText的焦点处理
   */
  public abstract boolean onChange(FlexField<T> flexField, T newValue, boolean isSelf);


  /**
   * 处理onActivityResult的返回结果，需要在这里主动回调onChange更新
   */
  protected void onActivityResult(FlexField<T> flexField, Bundle resultData) {
  }
}
