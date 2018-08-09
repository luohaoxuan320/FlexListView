package com.lehow.newapp.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lehow.newapp.DiscountActivity;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/27 14:08
 */
public class DiscountSelectFieldProcessor implements FlexFieldProcessor<String> {
  Activity activity;

  @Override public void onFieldClick(FlexField flexField) {
    Intent intent = new Intent(activity, DiscountActivity.class);
    intent.putExtra("discount", "减2000");
    intent.putExtra("value", "-20000");
    activity.startActivityForResult(intent,flexField.getAdapterPosition());
  }

  @Override public boolean onChange(FlexField flexField, String newValue, boolean isSelf) {
    flexField.setSummary("我也不知道");
    flexField.setValue(newValue);
    return true;
  }

  @Override public void onActivityResult(FlexField<String> flexField, Bundle resultData) {
    if (resultData != null) {
      onChange(flexField, resultData.getString("value"), true);
    }
  }
}
