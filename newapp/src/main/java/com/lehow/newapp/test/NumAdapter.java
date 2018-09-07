package com.lehow.newapp.test;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.lehow.newapp.NumInputFrg;
import com.lehow.newapp.R;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FieldProxyAdapter;
import com.lehow.flex.base.ProxyViewHolder;
import com.lehow.newapp.util.DecimalUtil;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 16:59
 */
public class NumAdapter extends FieldProxyAdapter<ItemViewHolder, FlexField> {

  @NonNull @Override public ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
    return new ItemViewHolder(inflate);
  }

  @Override public void onBindViewHolder(@NonNull final ItemViewHolder holder, FlexField entity) {
    //holder.bindFlexField(entity);//这个要放在setText前，否则会由于服用，导致setText里面比对的Field是回收的field，而不是目标的新的field

    holder.tvTitle.setText(entity.getTitle());
    holder.etSummary.setFocusable(true);
    holder.etSummary.setFocusableInTouchMode(true);
    holder.etSummary.setHint(entity.getHint());
    holder.etSummary.setText(entity.getSummary());
    holder.ivMore.setVisibility(View.INVISIBLE);

    holder.bindFlexField(entity);
  }

}