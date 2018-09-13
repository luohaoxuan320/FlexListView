package com.lehow.newapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.lehow.flex.base.FlexEntity;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FlexFieldProcessor;
import com.lehow.newapp.entity.PriceEntity;
import com.lehow.newapp.test.SimpleSelectFieldProcessor;
import com.lehow.newapp.util.DecimalUtil;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
    implements KeyboardWatcher.OnKeyboardToggleListener {


  RecyclerView recyclerView;
  FlexEntity flexEntity;
  KeyboardWatcher keyboardWatcher;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    recyclerView = findViewById(R.id.recyclerView);
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(linearLayoutManager);
    linearLayoutManager.setSmoothScrollbarEnabled(true);
    this.flexEntity = FlexEntity.create(this, new PriceEntity());
    recyclerView.setAdapter(this.flexEntity.getFlexAdapter());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setItemAnimator(null);//这里要取消动画，否则在notifyItemChanged时，会onCreateViewHolder 生成一个新的ViewHolder来做动画的过度，
    // 如果当前的item中含有EditText，会导致我在Holder中记录的光标位置无效。当然也可以考虑在别的地方记录光标位置，目前放在这

    keyboardWatcher = new KeyboardWatcher(this);
    keyboardWatcher.setListener(this);

    this.flexEntity.setFieldClickListener("aamount", new FlexFieldProcessor<String>() {

      @Override public void onFieldClick(FlexField<String> flexField, int adapterPosition) {

      }

      @Override
      public boolean onChange(FlexField<String> flexField, String newValue, boolean isSelf) {
        double value = DecimalUtil.parse(newValue);
        if (value > 10000) {
          Toast.makeText(MainActivity.this, "A价款总价 不能超过总房款价", Toast.LENGTH_SHORT).show();
          //刷新显示
          flexField.setValue(flexField.getValue(),
              false);//通知刷新当前的输入，比如前面一次有效输入是10000，现在输入是10100，这个是将输入框的显示还原为10000
          flexField.setSummary(flexField.getValue());
          return false;
        } else {
          flexField.setValue(newValue, true);
          flexField.setSummary(newValue);
          return true;
        }
      }

      @Override public void onActivityResult(FlexField<String> flexField, Bundle resultData) {

      }
    });

    //观察值，做自更新
    flexEntity.setFieldClickListener("aLoanType",
        new SimpleSelectFieldProcessor(this, R.array.loan_type, R.array.loan_type_value) {
          @Override
          public boolean onChange(FlexField<Integer> flexField, Integer newValue, boolean isSelf) {
            super.onChange(flexField, newValue, isSelf);
        ArrayList<String> add = new ArrayList<>();
        ArrayList<String> remove = new ArrayList<>();
            switch (newValue) {
              case 1:
            add.add("aLoan");
            add.add("aLoanAmount");
            add.add("aLoanYear");
            add.add("aLoanRate");
                remove.add("aLoan");
                remove.add("aLoanAmount");
                remove.add("aLoanYear");
                remove.add("aLoanRate");
            remove.add("aLoan1");
            remove.add("aLoan1Amount");
            remove.add("aLoan1Year");
            remove.add("aLoan1Rate");
                flexEntity.invisibleField(remove);
                flexEntity.visibleFields(flexEntity.indexInShow("aLoanType") + 1, add);
            break;
              case 2:
            remove.add("aLoan");
            remove.add("aLoanAmount");
            remove.add("aLoanYear");
            remove.add("aLoanRate");
                remove.add("aLoan1");
                remove.add("aLoan1Amount");
                remove.add("aLoan1Year");
                remove.add("aLoan1Rate");
            add.add("aLoan1");
            add.add("aLoan1Amount");
            add.add("aLoan1Year");
            add.add("aLoan1Rate");
                flexEntity.invisibleField(remove);
                flexEntity.visibleFields(flexEntity.indexInShow("aLoanType") + 1, add);
            break;
              case 3:
                remove.add("aLoan");
                remove.add("aLoanAmount");
                remove.add("aLoanYear");
                remove.add("aLoanRate");
                remove.add("aLoan1");
                remove.add("aLoan1Amount");
                remove.add("aLoan1Year");
                remove.add("aLoan1Rate");

            add.add("aLoan");
            add.add("aLoanAmount");
            add.add("aLoanYear");
            add.add("aLoanRate");
            add.add("aLoan1");
            add.add("aLoan1Amount");
            add.add("aLoan1Year");
            add.add("aLoan1Rate");
                flexEntity.invisibleField(remove);
                flexEntity.visibleFields(flexEntity.indexInShow("aLoanType") + 1, add);
            break;
        }
            return true;
      }
        });

    findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        Log.i("TAG", "onTouch: ");
        return false;
      }
    });
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    flexEntity.onActivityResult(requestCode,resultCode,data);
  }

  @Override protected void onDestroy() {
    keyboardWatcher.destroy();
    super.onDestroy();
  }

  @Override public void onKeyboardShown(int keyboardSize) {
    Log.i("TAG", "onKeyboardShown: ");
  }

  @Override public void onKeyboardClosed() {
    Log.i("TAG", "onKeyboardClosed: ");
    View currentFocus = getCurrentFocus();
    currentFocus.clearFocus();//清除当前焦点
  }

  @Override public boolean dispatchTouchEvent(MotionEvent ev) {
    if (ev.getAction() == MotionEvent.ACTION_DOWN) {
      View v = getCurrentFocus();
      if (isShouldHideInput(v, ev)) {
        if (hideInputMethod(this, v)) {
          return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
        }
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  public static boolean isShouldHideInput(View v, MotionEvent event) {
    if (v != null && (v instanceof EditText)) {
      int[] leftTop = { 0, 0 };
      v.getLocationInWindow(leftTop);
      int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right =
          left + v.getWidth();
      if (event.getX() > left
          && event.getX() < right
          && event.getY() > top
          && event.getY() < bottom) {
        // 保留点击EditText的事件
        return false;
      } else {
        return true;
      }
    }
    return false;
  }

  public static Boolean hideInputMethod(Context context, View v) {
    InputMethodManager imm =
        (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    if (imm != null) {
      return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }
    return false;
  }

}
