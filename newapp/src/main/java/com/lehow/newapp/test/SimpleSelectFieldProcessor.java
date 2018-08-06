package com.lehow.newapp.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ArrayRes;
import com.lehow.newapp.SimpleSelectActivity;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/27 14:31
 */
public class SimpleSelectFieldProcessor implements FlexFieldProcessor {
  int selIndex=-1;
  String[] dataSrc;
  int[] values;
  int dataRes;
  int valueRes;

  public SimpleSelectFieldProcessor() {
  }

  public SimpleSelectFieldProcessor(@ArrayRes int dataRes,@ArrayRes int valueRes,int selIndex) {
    this.selIndex = selIndex;
    this.dataRes = dataRes;
    this.valueRes = valueRes;
  }

  @Override public void onFieldClick(Activity activity, FlexField flexField) {
    if (dataSrc == null) {
      dataSrc = activity.getResources().getStringArray(dataRes);
      values = activity.getResources().getIntArray(valueRes);
    }
    Intent intent = new Intent(activity, SimpleSelectActivity.class);
    intent.putExtra("dataSrc", dataSrc);
    intent.putExtra("selIndex", selIndex);
    activity.startActivityForResult(intent,flexField.getAdapterPosition());
  }

  @Override public void onChange(FlexField flexField, Bundle bundle) {
    if (bundle != null) {
      selIndex = bundle.getInt("selIndex", -1);
      flexField.setSummary(selIndex == -1 ? "" : dataSrc[selIndex]);
      flexField.setValue(selIndex == -1 ? 0 : values[selIndex]);
    }else{//级联更新

    }
  }

  public int getSelIndex() {
    return selIndex;
  }
}
