package com.lehow.newapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.Toast;
import com.lehow.flex.base.FlexEntity;
import com.lehow.flex.base.FlexField;
import com.lehow.flex.base.FlexFieldProcessor;
import com.lehow.newapp.entity.PriceEntity;
import com.lehow.newapp.util.DecimalUtil;

public class MainActivity extends AppCompatActivity {

  RecyclerView recyclerView;
  FlexEntity flexEntity;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    recyclerView = findViewById(R.id.recyclerView);
    LinearLayoutManager linearLayoutManager =
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    recyclerView.setLayoutManager(linearLayoutManager);
    linearLayoutManager.setSmoothScrollbarEnabled(true);
    this.flexEntity = FlexEntity.create(new PriceEntity());
    recyclerView.setAdapter(this.flexEntity.getFlexAdapter());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setItemAnimator(null);//这里要取消动画，否则在notifyItemChanged时，会onCreateViewHolder 生成一个新的ViewHolder来做动画的过度，
    // 如果当前的item中含有EditText，会导致我在Holder中记录的光标位置无效。当然也可以考虑在别的地方记录光标位置，目前放在这

    this.flexEntity.setFieldClickListener("aamount", new FlexFieldProcessor<String>() {
      @Override public void onFieldClick(Activity activity, FlexField flexField) {

      }

      @Override public boolean onChange(FlexField<String> flexField, Bundle bundle) {
        if (bundle != null) {//EditText输入的更新
          String curInput = bundle.getString("value");
          double newValue = DecimalUtil.parse(curInput);
          if (newValue > 10000) {
            Toast.makeText(MainActivity.this, "A价款总价 不能超过总房款价", Toast.LENGTH_SHORT).show();
            //刷新显示
            flexField.setValue(flexField.getValue());//通知刷新当前的输入，比如前面一次有效输入是10000，现在输入是10100，这个是将输入框的显示还原为10000
            flexField.setSummary(flexField.getValue());
            return false;
          } else {
            flexField.setValue(curInput);
            flexField.setSummary(curInput);
            return true;
          }
        } else {//初始化和级联更新
          flexField.setSummary(flexField.getValue());
        }
        return true;
      }
    });
/*
 *观察值，做自更新
 flexEntity.setFieldClickListener("aLoanType", new SimpleSelectFieldProcessor(R.array.loan_type,R.array.loan_type,1){
      @Override public void onChange(FlexField flexField, Bundle bundle) {
        super.onChange(flexField, bundle);
        ArrayList<String> add = new ArrayList<>();
        ArrayList<String> remove = new ArrayList<>();
        switch (getSelIndex()) {
          case 0:
            add.add("aLoan");
            add.add("aLoanAmount");
            add.add("aLoanYear");
            add.add("aLoanRate");
            remove.add("aLoan1");
            remove.add("aLoan1Amount");
            remove.add("aLoan1Year");
            remove.add("aLoan1Rate");
            flexEntity.showTheField(flexField.getAdapterPosition() + 1, add);
            //flexEntity.removeTheShowField(remove);
            break;
          case 1:
            remove.add("aLoan");
            remove.add("aLoanAmount");
            remove.add("aLoanYear");
            remove.add("aLoanRate");
            add.add("aLoan1");
            add.add("aLoan1Amount");
            add.add("aLoan1Year");
            add.add("aLoan1Rate");
            flexEntity.showTheField(flexField.getAdapterPosition() + 1, add);
            flexEntity.removeTheShowField(remove);
            break;
          case 2:
            add.add("aLoan");
            add.add("aLoanAmount");
            add.add("aLoanYear");
            add.add("aLoanRate");
            add.add("aLoan1");
            add.add("aLoan1Amount");
            add.add("aLoan1Year");
            add.add("aLoan1Rate");
            flexEntity.showTheField(flexField.getAdapterPosition() + 1, add);
            flexEntity.removeTheShowField(remove);
            break;
        }
      }
    });*/
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    flexEntity.onActivityResult(requestCode,resultCode,data);
  }
}
