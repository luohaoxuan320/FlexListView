package com.lehow.newapp.test;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lehow.newapp.R;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FieldProxyAdapter;
import com.lehow.flex.base.ProxyViewHolder;
import java.util.List;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 16:59
 */
public class ShowAdapter extends FieldProxyAdapter<ItemViewHolder, FlexField> {
  @NonNull @Override public ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    return new ItemViewHolder(inflate);
  }

  @Override public void onBindViewHolder(@NonNull ItemViewHolder holder, FlexField entity) {
    holder.tvTitle.setText(entity.getTitle());
    holder.etSummary.setFocusable(false);
    holder.etSummary.setFocusableInTouchMode(false);
    holder.etSummary.setHint(entity.getHint());
    holder.etSummary.setText(entity.getSummary());
    holder.ivMore.setVisibility(View.INVISIBLE);
    holder.etSummary.setOnClickListener(null);
  }
}