package com.lehow.flex.base;

import io.reactivex.Observable;

/**
 * desc:CombineVisible 的keys看起来没啥用 暂时先这样定义
 * author: luoh17
 * time: 2018/8/2 15:56
 */
public abstract class CombineFuc {
  public FlexEntity flexEntity;

  public CombineFuc(FlexEntity flexEntity) {
    this.flexEntity = flexEntity;
  }

  public abstract Observable getCombineObservable();

}
