package com.lehow.flex.base;

import io.reactivex.Observable;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/2 15:56
 */
public abstract class CombineVisibleFuc {
  public FlexEntity flexEntity;

  public CombineVisibleFuc(FlexEntity flexEntity) {
    this.flexEntity = flexEntity;
  }

  public abstract Observable getCombineObservable();

}
