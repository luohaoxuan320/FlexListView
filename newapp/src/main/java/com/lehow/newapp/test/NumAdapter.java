package com.lehow.newapp.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lehow.newapp.R;
import com.lehow.newapp.base.FlexField;
import com.lehow.newapp.base.FieldProxyAdapter;
import com.lehow.newapp.base.ProxyViewHolder;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 16:59
 */
public class NumAdapter  implements FieldProxyAdapter<ItemViewHolder,FlexField> {


  @NonNull @Override public ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    return new ItemViewHolder(inflate);
  }

  @Override public void onBindViewHolder(@NonNull ItemViewHolder holder, FlexField entity) {
    //holder.bindFlexField(entity);//这个要放在setText前，否则会由于服用，导致setText里面比对的Field是回收的field，而不是目标的新的field

    holder.tvTitle.setText(entity.getTitle());
    holder.etSummary.setFocusable(true);
    holder.etSummary.setFocusableInTouchMode(true);
    holder.etSummary.setHint(entity.getHint());
    holder.etSummary.setText(entity.getSummary());
    holder.ivMore.setVisibility(View.INVISIBLE);
    holder.etSummary.setOnClickListener(null);
    holder.bindFlexField(entity);
  }
}