package com.lehow.newapp.test;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.lehow.newapp.NumInputFrg;
import com.lehow.newapp.R;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FieldProxyAdapter;
import com.lehow.flex.base.ProxyViewHolder;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 16:59
 */
public class NumAdapter  implements FieldProxyAdapter<ItemViewHolder,FlexField> {

  NumInputFrg numInputFrg = new NumInputFrg();
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
    holder.etSummary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
          showInput(v);
        }
      }
    });
    holder.etSummary.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showInput(v);
      }
    });
    //holder.etSummary.setInputType(InputType.TYPE_NULL);
    holder.bindFlexField(entity);
  }

  private void showInput(View view) {
    numInputFrg.setEditText((EditText) view);
    if (!numInputFrg.isVisible()) {
      numInputFrg.show(((FragmentActivity) view.getContext()).getSupportFragmentManager(), "input");
    }
  }
}