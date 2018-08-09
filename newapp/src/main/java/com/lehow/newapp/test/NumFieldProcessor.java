package com.lehow.newapp.test;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/27 13:55
 */
public class NumFieldProcessor implements FlexFieldProcessor<String> {
  @Override public void onFieldClick(FlexField flexField) {
    //donothing
  }

  @Override public boolean onChange(FlexField<String> flexField, String newValue, boolean isSelf) {
    Log.i("TAG", "NumFieldProcessor onChange: value=" + flexField.getValue());
    flexField.setSummary(newValue + "");
    //通知Recyclerview刷新
    flexField.setValue(newValue);
    return true;
  }

  @Override public void onActivityResult(FlexField<String> flexField, Bundle resultData) {

  }
}
