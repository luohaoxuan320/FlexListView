package com.lehow.newapp.test;

import android.app.Activity;
import android.os.Bundle;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/2 11:16
 */
public class CategoryFieldProcessor implements FlexFieldProcessor<Boolean> {
  @Override public void onFieldClick(Activity activity, FlexField<Boolean> flexField) {
    flexField.setValue(!flexField.getValue());
  }

  @Override public boolean onChange(FlexField flexField, Bundle bundle) {
    return true;
  }
}
