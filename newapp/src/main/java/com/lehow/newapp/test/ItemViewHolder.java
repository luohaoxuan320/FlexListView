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
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.ProxyViewHolder;
import java.lang.reflect.Method;

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
  FlexField flexField;
  public ItemViewHolder(View itemView) {
    super(itemView);
    tvTitle = itemView.findViewById(R.id.tv_title);
    etSummary = itemView.findViewById(R.id.et_summary);
    ivMore = itemView.findViewById(R.id.iv_more);
    Log.i("TAG", "ItemViewHolder: addTextChangedListener");
    etSummary.setOnFocusChangeListener(focusChangeListener);
    initSoftInput();
  }

  @Override public void onReset() {
    Log.i("TAG", "onReset: ");
    etSummary.setSelection(0);
    etSummary.setOnFocusChangeListener(null);
    //mTextWatcher.flexField =null;//保险起见，这里在回收的时候清空，防止由于复用，
    // 导致onBindViewHolder重新刷新Num类型数据时，由于比对到了错误的field的而导致在onBindViewHolder中又请求notifyDataChange而报错

  }

  private View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
    @Override public void onFocusChange(View v, boolean hasFocus) {
      Log.i("TAG", "onFocusChange: hasFocus=" + hasFocus + " v=" + v);
      if (!hasFocus) {
        if (flexField != null && flexField.getFlexFieldProcessor() != null) {

          Log.i("TAG", "afterTextChanged: s="
              + etSummary.getText().toString()
              + " start="
              + etSummary.getSelectionStart()
              + " end="
              + etSummary.getSelectionEnd());

          //数据不一样才通知，防止设置值的时候，这里死循环的执行
          if (!etSummary.getText().toString().equals(flexField.getSummary())) {
            if (flexField.getFlexFieldProcessor()
                .onChange(flexField, etSummary.getText().toString(), true)) {
              focusIndex = etSummary.getSelectionStart();//数据接收后，才刷新这个焦点记录
            }
          }
        } else {//第一次bind FlexEntity初始化
          focusIndex = etSummary.getText().length();
        }
      }
    }
  };
  public void bindFlexField(FlexField flexField) {
    this.flexField = flexField;//在这里绑定感觉是有点问题的，afterTextChanged先执行了，不过onReset有清空数据，影响不大
    Log.i("TAG", "bindFlexField: focusIndex="+focusIndex);
    etSummary.setSelection(Math.min(focusIndex, etSummary.getText().length()));
  }

  private void initSoftInput() {
    int currentVersion = android.os.Build.VERSION.SDK_INT;
    if (currentVersion >= 21) {
      etSummary.setShowSoftInputOnFocus(false);
    }
    if (currentVersion >= 16) {//调用隐藏API
      // 4.1.2
      try {
        Class<EditText> cls = EditText.class;
        Method setSoftInputShownOnFocus;
        setSoftInputShownOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
        setSoftInputShownOnFocus.setAccessible(true);
        setSoftInputShownOnFocus.invoke(etSummary, false);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
 /* class MTextWatcher implements TextWatcher {
    FlexField flexField;
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      s.length();
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
          if (flexField.getFlexFieldProcessor().onChange(flexField, s.toString(), true)) {
            focusIndex = etSummary.getSelectionStart();//数据接收后，才刷新这个焦点记录
          }
        }
      } else {//第一次bind FlexEntity初始化
        focusIndex = s.length();
      }
    }
  }*/

}
