package com.lehow.newapp.test;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.lehow.newapp.R;
import com.lehow.newapp.base.FieldProxyAdapter;
import com.lehow.newapp.base.FlexEntity;
import com.lehow.newapp.base.FlexField;
import com.lehow.newapp.base.ProxyViewHolder;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/1 13:44
 */
public class CategoryAdapter implements FieldProxyAdapter<CategoryAdapter.CategoryHolder,FlexField<Boolean>> {


  @NonNull @Override public ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
    return new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
  }

  @Override public void onBindViewHolder(@NonNull CategoryHolder holder, FlexField<Boolean> entity) {
    holder.title.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(holder.itemView.getContext(),entity.getValue() ? R.mipmap.icon_w_up : R.mipmap.icon_w_down), null);
  }

 static class CategoryHolder extends ProxyViewHolder {
    TextView title;
    public CategoryHolder(View itemView) {
      super(itemView);
      title = itemView.findViewById(R.id.tv_title);
    }

    @Override protected void onReset() {

    }
  }
}
