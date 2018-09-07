package com.lehow.flex.base;

import android.view.ViewGroup;
import java.util.List;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 15:34
 */
public abstract class FieldProxyAdapter<K extends ProxyViewHolder, T> {
  public abstract ProxyViewHolder onCreateViewHolder(ViewGroup parent);

  public abstract void onBindViewHolder(K holder, T entity);

  protected void onBindViewHolder(K holder, T entity, List<Object> payloads) {
    onBindViewHolder(holder, entity);
  }
}
