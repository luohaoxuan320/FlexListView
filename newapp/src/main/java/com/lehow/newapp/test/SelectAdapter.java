package com.lehow.newapp.test;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lehow.newapp.R;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FieldProxyAdapter;
import com.lehow.flex.base.ProxyViewHolder;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 15:39
 */
public class SelectAdapter implements FieldProxyAdapter<ItemViewHolder,FlexField> {
  @NonNull @Override
  public ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    return new ItemViewHolder(inflate);
  }

  @Override
  public void onBindViewHolder(@NonNull final ItemViewHolder holder, final FlexField entity) {
    holder.tvTitle.setText(entity.getTitle());
    holder.etSummary.setFocusable(false);
    holder.etSummary.setFocusableInTouchMode(false);
    holder.etSummary.setHint(entity.getHint());
    holder.etSummary.setText(entity.getSummary());
    holder.ivMore.setVisibility(View.VISIBLE);
    holder.etSummary.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (entity.getFlexFieldProcessor() != null) {
          entity.getFlexFieldProcessor().onFieldClick((Activity) holder.itemView.getContext(),entity);
        }
      }
    });
  }


}
