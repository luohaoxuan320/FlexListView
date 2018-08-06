package com.lehow.flex.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/26 14:41
 */
public abstract class ProxyViewHolder extends RecyclerView.ViewHolder{
  public ProxyViewHolder(View itemView) {
    super(itemView);
  }

  public abstract void onReset();
}
