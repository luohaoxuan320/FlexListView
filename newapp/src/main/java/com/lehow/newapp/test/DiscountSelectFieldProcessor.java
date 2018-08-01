package com.lehow.newapp.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.lehow.newapp.DiscountActivity;
import com.lehow.newapp.base.FlexField;
import com.lehow.newapp.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/27 14:08
 */
public class DiscountSelectFieldProcessor implements FlexFieldProcessor {
  @Override public void onFieldClick(Activity activity, FlexField flexField) {
    Intent intent = new Intent(activity, DiscountActivity.class);
    intent.putExtra("discount", "减2000");
    intent.putExtra("value", "-20000");
    activity.startActivityForResult(intent,flexField.getAdapterPosition());
  }

  @Override public void onChange(FlexField flexField, Bundle bundle) {
    if (bundle != null) {
      flexField.setSummary(bundle.getString("discount"));
      flexField.setValue(bundle.getString("value"));
    }else{//级联更新

    }
  }
}
