package com.lehow.newapp.test;

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
public class FirstAmountFieldProcessor implements FlexFieldProcessor<String> {
  @Override public void onFieldClick(FlexField flexField) {
    //donothing
  }

  @Override public boolean onChange(FlexField<String> flexField, String newValue, boolean isSelf) {
    if (newValue != null && newValue.equals(flexField.getValue())) {
      if (!isSelf && TextUtils.isEmpty(newValue)) {
        return false;//级联来的数据为空，不更新当前数据，主要是阻断与首付成数的联动
      }
    }
    Log.i("TAG", "FirstAmountFieldProcessor onChange: ");
    flexField.setSummary(newValue + "");
    //通知Recyclerview刷新
    flexField.setValue(newValue);
    return true;
  }

  @Override public void onActivityResult(FlexField<String> flexField, Bundle resultData) {

  }
}
