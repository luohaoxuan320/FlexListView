package com.lehow.newapp.test;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.lehow.newapp.R;
import com.lehow.newapp.base.FlexField;
import com.lehow.newapp.base.ProxyViewHolder;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 16:59
 */
public class ItemViewHolder extends ProxyViewHolder {
  private int focusIndex=0;
  TextView tvTitle;
  EditText etSummary;
  ImageView ivMore;
  MTextWatcher mTextWatcher = new MTextWatcher();
  public ItemViewHolder(View itemView) {
    super(itemView);
    tvTitle = itemView.findViewById(R.id.tv_title);
    etSummary = itemView.findViewById(R.id.et_summary);
    ivMore = itemView.findViewById(R.id.iv_more);
    Log.i("TAG", "ItemViewHolder: addTextChangedListener");
    etSummary.addTextChangedListener(mTextWatcher);
  }

  @Override protected void onReset() {
    Log.i("TAG", "onReset: ");
    etSummary.setSelection(0);
    mTextWatcher.flexField =null;//保险起见，这里在回收的时候清空，防止由于复用，
    // 导致onBindViewHolder重新刷新Num类型数据时，由于比对到了错误的field的而导致在onBindViewHolder中又请求notifyDataChange而报错

  }

  public void bindFlexField(FlexField flexField) {
    mTextWatcher.flexField = flexField;
    Log.i("TAG", "bindFlexField: focusIndex="+focusIndex);
    etSummary.setSelection(focusIndex);
  }

  class MTextWatcher implements TextWatcher {
    FlexField flexField;
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override public void afterTextChanged(Editable s) {
      if (flexField!=null&&flexField.getFlexFieldProcessor()!=null){

        Log.i("TAG", "afterTextChanged: s="
            + s.toString()
            + " start="
            + etSummary.getSelectionStart()
            + " end="
            + etSummary.getSelectionEnd());

        //数据不一样才通知，防止设置值的时候，这里死循环的执行
        if (!s.toString().equals(flexField.getSummary())){
          focusIndex= etSummary.getSelectionStart();
          Bundle bundle = new Bundle();
          bundle.putString("value", s.toString());
          flexField.getFlexFieldProcessor().onChange(flexField,bundle);
        }
      }
    }
  }

}
