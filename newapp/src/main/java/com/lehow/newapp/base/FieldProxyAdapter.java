package com.lehow.newapp.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 15:34
 */
public interface FieldProxyAdapter<K extends ProxyViewHolder,T> {
  @NonNull
  ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent);

  void onBindViewHolder(@NonNull K holder, T entity) ;

}
