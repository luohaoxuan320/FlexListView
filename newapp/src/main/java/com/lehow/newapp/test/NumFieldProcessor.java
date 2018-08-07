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
public class NumFieldProcessor implements FlexFieldProcessor {
  @Override public void onFieldClick(Activity activity, FlexField flexField) {
    //donothing
  }

  @Override public boolean onChange(FlexField flexField, Bundle bundle) {
    Log.i("TAG", "NumFieldProcessor onChange: value=" + flexField.getValue());
    String curInput = null;
    if (bundle == null) {//级联通知的更新
      curInput=String.valueOf(flexField.getValue());
    }else {
       curInput = bundle.getString("value");
    }
    flexField.setSummary(curInput);
    //通知Recyclerview刷新
    flexField.setValue(TextUtils.isEmpty(curInput) ? 0f : Double.parseDouble(curInput));
    return true;
  }
}
