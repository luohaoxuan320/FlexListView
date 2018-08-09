package com.lehow.flex.base;

import io.reactivex.Observable;
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
   * 在Adapter中的position,用于notifyItemChange快速刷新
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
    if (flexField.value != null) {
      valueObservable = BehaviorSubject.createDefault(flexField.value);
    } else {
      valueObservable = BehaviorSubject.create();
    }
    positionObservable = PublishSubject.create();
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

  public FlexFieldProcessor<T> getFlexFieldProcessor() {
    return flexFieldProcessor;
  }

  public FlexField<T> setFlexFieldProcessor(FlexFieldProcessor<T> flexFieldProcessor) {
    this.flexFieldProcessor = flexFieldProcessor;
    //设置了处理器后，先来处理下默认值的显示
    if (flexFieldProcessor != null && getValue() != null) {
      flexFieldProcessor.onChange(this, getValue(), true);
    }
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
    //flexField.value = t;
    if (flexFieldProcessor != null) flexFieldProcessor.onChange(this, t, false);
  }

  public String getKey() {
    return flexField.key;
  }

 /* public void notifySelfChange() {//自身的变化，比如选择首付成数，或者改变房价总额,已通知关联方变化
    valueObservable.onNext(flexField.value);
  }*/

  public Observable<T> getValueObservable() {
    return valueObservable;
  }

}
