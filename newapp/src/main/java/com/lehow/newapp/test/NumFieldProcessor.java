package com.lehow.newapp.test;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import com.lehow.newapp.base.FlexField;
import com.lehow.newapp.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/27 13:55
 */
public class NumFieldProcessor implements FlexFieldProcessor {
  @Override public void onFieldClick(Activity activity, FlexField flexField) {
    //donothing
  }

  @Override public void onChange(FlexField flexField, Bundle bundle) {
    String curInput = null;
    if (bundle == null) {//级联通知的更新
      curInput=String.valueOf(flexField.getValue());
    }else {
       curInput = bundle.getString("value");
    }
    flexField.setSummary(curInput);
    //通知Recyclerview刷新
    flexField.setValue(TextUtils.isEmpty(curInput) ? 0f : Float.parseFloat(curInput));
  }
}
