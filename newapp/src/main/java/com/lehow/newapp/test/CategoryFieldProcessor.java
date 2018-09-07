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
public class CategoryFieldProcessor extends FlexFieldProcessor<Boolean> {

  @Override public boolean onChange(FlexField flexField, Boolean newValue, boolean isSelf) {
    return true;
  }

  @Override public void onFieldClick(FlexField<Boolean> flexField, int adapterPosition) {
    flexField.setValue(!flexField.getValue(), true);
  }

  @Override public void onActivityResult(FlexField<Boolean> flexField, Bundle resultData) {

  }
}
