package com.lehow.newapp.test;

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
    //initSoftInput(etSummary);
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
            }
          }
        }
      }
    }
  };
  public void bindFlexField(FlexField flexField) {
    this.flexField = flexField;//在这里绑定感觉是有点问题的，afterTextChanged先执行了，不过onReset有清空数据，影响不大
  }

}
