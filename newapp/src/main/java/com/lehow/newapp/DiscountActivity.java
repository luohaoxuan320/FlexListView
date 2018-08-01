package com.lehow.newapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DiscountActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_discount);
  }

  public void btnClick(View view) {
    Intent intent = new Intent();
    intent.putExtra("discount", "减2000");
    intent.putExtra("value", "-2000");
    setResult(RESULT_OK, intent);
    finish();
  }
}
