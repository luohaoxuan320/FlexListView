package com.lehow.flex.base;

import android.view.ViewGroup;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 15:34
 */
public interface FieldProxyAdapter<K extends ProxyViewHolder, T> {
  ProxyViewHolder onCreateViewHolder(ViewGroup parent);

  void onBindViewHolder(K holder, T entity);
}
