package com.lehow.newapp.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;

/**
 * desc:让FlexField只有业务的数据，其他附加的操作和属性放在这
 * author: luoh17
 * time: 2018/7/27 9:54
 */
public class FlexField<T> implements Consumer<T> {
  InnerFlexField<T> flexField;

  /**
   * 在Adapter中的position
   */
  int adapterPosition = -1;

  /**
   * viewtype，视图类型
   */
  int proxyViewType;
  /**
   * 处理器
   */
  FlexFieldProcessor flexFieldProcessor;

  BehaviorSubject<T> valueObservable = null;
  PublishSubject<Integer> positionObservable = null;
  public FlexField(String key,T value) {
    this.flexField = new InnerFlexField<>(key,value);
    valueObservable = BehaviorSubject.createDefault(flexField.value);
    positionObservable = PublishSubject.create();
    this.flexFieldProcessor = flexFieldProcessor;
  }

  public FlexField setTitle(String title) {
    flexField.title = title;
    return this;
  }

  public String getTitle(){
    return flexField.title;
  }

  public FlexField setSummary(String summary) {
    flexField.summary = summary;
    return this;
  }

  public String getSummary(){
    return flexField.summary;
  }

  public String getHint() {
    return flexField.hint;
  }

  public FlexField setHint(String hint) {
    flexField.hint = hint;
    return this;
  }


  public FlexField setValue(T value) {
    flexField.value = value;
    //自身的变化，比如选择首付成数，或者改变房价总额,已通知关联方变化
    valueObservable.onNext(value);
    //通知Recyclerview刷新显示
    positionObservable.onNext(adapterPosition);
    return this;
  }

  public T getValue() {
    return flexField.value;
  }

  public FlexFieldProcessor getFlexFieldProcessor() {
    return flexFieldProcessor;
  }

  public FlexField<T> setFlexFieldProcessor(FlexFieldProcessor flexFieldProcessor) {
    this.flexFieldProcessor = flexFieldProcessor;
    return this;
  }

  public FlexField setProxyViewType(int proxyViewType) {
    this.proxyViewType = proxyViewType;
    return this;
  }

  public int getProxyViewType() {
    return proxyViewType;
  }

  public int getAdapterPosition() {
    return adapterPosition;
  }

  public void setAdapterPosition(int adapterPosition) {
    this.adapterPosition = adapterPosition;
  }

  /**
   * 数据变化时，通过这个分发当前的position给外部的adapter，以调用notifyItemChanged(position) 刷新在Recyclerview中的显示
   * @param consumer
   * @return
   */
  public FlexField notifyAdapter(Consumer<Integer> consumer) {
    positionObservable.subscribe(consumer);
    return this;
  }

  @Override public void accept(T t) throws Exception {//由关联数据变化触发的，比如首付金额，由于首付成数或者房价总额变化而触发更新
    flexField.value = t;
    if (flexFieldProcessor!=null)flexFieldProcessor.onChange(this,null);
  }

  public String getKey() {
    return flexField.key;
  }

 /* public void notifySelfChange() {//自身的变化，比如选择首付成数，或者改变房价总额,已通知关联方变化
    valueObservable.onNext(flexField.value);
  }*/


}
