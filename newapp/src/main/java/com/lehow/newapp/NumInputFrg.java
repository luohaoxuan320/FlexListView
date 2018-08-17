package com.lehow.newapp;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 */
public class NumInputFrg extends BottomSheetDialogFragment implements View.OnClickListener {

  private EditText editText;

  public NumInputFrg() {
    // Required empty public constructor
  }

  public static NumInputFrg newInstance(String title, String defValue) {
    Bundle args = new Bundle();
    args.putString("title", title);
    args.putString("defValue", defValue);
    NumInputFrg fragment = new NumInputFrg();
    fragment.setArguments(args);
    return fragment;
  }

  @Override public void onStart() {
    super.onStart();
    Window window = getDialog().getWindow();
    WindowManager.LayoutParams windowParams = window.getAttributes();
    windowParams.dimAmount = 0.0f;

    window.setAttributes(windowParams);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View inflate = inflater.inflate(R.layout.fragment_num_input_frg, container, false);
    return inflate;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    view.findViewById(R.id.btn_0).setOnClickListener(this);
    view.findViewById(R.id.btn_1).setOnClickListener(this);
    view.findViewById(R.id.btn_2).setOnClickListener(this);
    view.findViewById(R.id.btn_3).setOnClickListener(this);
    view.findViewById(R.id.btn_4).setOnClickListener(this);
    view.findViewById(R.id.btn_5).setOnClickListener(this);
    view.findViewById(R.id.btn_6).setOnClickListener(this);
    view.findViewById(R.id.btn_7).setOnClickListener(this);
    view.findViewById(R.id.btn_8).setOnClickListener(this);
    view.findViewById(R.id.btn_9).setOnClickListener(this);
    view.findViewById(R.id.btn_dot).setOnClickListener(this);
    view.findViewById(R.id.btn_c).setOnClickListener(this);
  }

  public void btnClick(View view) {
    switch (view.getId()) {
      case R.id.btn_0:
        processInputNum(0);
        break;
      case R.id.btn_1:
        processInputNum(1);
        break;
      case R.id.btn_2:
        processInputNum(2);
        break;
      case R.id.btn_3:
        processInputNum(3);
        break;
      case R.id.btn_4:
        processInputNum(4);
        break;
      case R.id.btn_5:
        processInputNum(5);
        break;
      case R.id.btn_6:
        processInputNum(6);
        break;
      case R.id.btn_7:
        processInputNum(7);
        break;
      case R.id.btn_8:
        processInputNum(8);
        break;
      case R.id.btn_9:
        processInputNum(9);
        break;
      case R.id.btn_dot:
        editText.getText().insert(editText.getSelectionStart(), ".");
        break;
      case R.id.btn_c:
        if (editText.getText() != null && editText.getText().length() > 0) {
          if (editText.getSelectionStart() > 0) {
            editText.getText()
                .delete(editText.getSelectionStart() - 1, editText.getSelectionStart());
          }
        }
        break;
    }
    //显示输入记录
    //input.setText(inputBuilder.toString() + curInput.toString());
    //只显示当前输入

  }

  boolean processInputNum(int num) {
    editText.getText().insert(editText.getSelectionStart(), num + "");
    return true;
  }

  public void setEditText(EditText editText) {
    this.editText = editText;
  }

  @Override public void onClick(View v) {
    btnClick(v);
  }
}
