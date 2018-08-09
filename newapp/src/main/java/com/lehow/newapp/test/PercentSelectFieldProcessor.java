package com.lehow.newapp.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import android.util.Log;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FlexFieldProcessor;
import com.lehow.newapp.SimpleSelectActivity;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/27 14:31
 */
public class PercentSelectFieldProcessor implements FlexFieldProcessor<Integer> {
  int selIndex = -1;
  String[] dataSrc;
  int[] values;
  private Activity activity;

  public PercentSelectFieldProcessor(Activity context, @ArrayRes int dataRes,
      @ArrayRes int valueRes) {
    activity = (Activity) context;
    dataSrc = activity.getResources().getStringArray(dataRes);
    values = activity.getResources().getIntArray(valueRes);
  }

  @Override public void onFieldClick(FlexField<Integer> flexField) {
    Intent intent = new Intent(activity, SimpleSelectActivity.class);
    intent.putExtra("dataSrc", dataSrc);
    intent.putExtra("selIndex", selIndex);
    activity.startActivityForResult(intent, flexField.getAdapterPosition());
  }

  @Override
  public boolean onChange(FlexField<Integer> flexField, Integer newValue, boolean isSelf) {
    if (flexField.getValue().intValue() == newValue.intValue()) return false;
    if (newValue == -1) return false;
    if (isSelf) {

      if (selIndex == -1) {//初始化数据的时候，会主动回调这个，来处理summary显示的问题，这里也顺带处理一下selIndex
        for (int i = 0; i < values.length; i++) {
          if (newValue.intValue() == values[i]) {
            selIndex = i;
            break;
          }
        }
      }
    } else {//首付金额改变，引起的
      for (int i = 0; i < values.length; i++) {
        if (newValue.intValue() == values[i]) {
          selIndex = i;
          break;
        }
      }
    }
    Log.i("TAG", "PercentSelectFieldProcessor onChange: ");
    flexField.setValue(newValue);
    flexField.setSummary(selIndex == -1 ? "" : dataSrc[selIndex]);

    return true;
  }

  @Override public void onActivityResult(FlexField<Integer> flexField, Bundle resultData) {
    if (resultData != null) {
      selIndex = resultData.getInt("selIndex", -1);
      onChange(flexField, values[selIndex], true);
    }
  }
}
