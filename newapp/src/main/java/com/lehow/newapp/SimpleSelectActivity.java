package com.lehow.newapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleSelectActivity extends AppCompatActivity {

  private String[] dataSrc;
  private int selIndex=-1;

  RecyclerView recyclerView;
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dataSrc = getIntent().getStringArrayExtra("dataSrc");
    selIndex = getIntent().getIntExtra("selIndex",-1);
    setContentView(R.layout.activity_simple_select);
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(new MAdapter());
  }

  public void btnClick(View view) {
    Intent intent = new Intent();
    intent.putExtra("selIndex", 1);
    setResult(RESULT_OK, intent);
    finish();
  }

  private class MAdapter extends RecyclerView.Adapter<MHolder> {
    @NonNull @Override public MHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new MHolder(getLayoutInflater().inflate(R.layout.item_simple_select, parent, false));
    }

    @Override public void onBindViewHolder(@NonNull MHolder holder, final int position) {
      holder.title.setText(dataSrc[position]);
      if (position == selIndex) {
        holder.title.setBackgroundResource(R.color.colorPrimary);
      }else{
        holder.title.setBackgroundResource(android.R.color.white);
      }
      holder.title.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          Intent intent = new Intent();
          intent.putExtra("selIndex", position);
          setResult(RESULT_OK, intent);
          finish();
        }
      });
    }

    @Override public int getItemCount() {
      return dataSrc==null?0:dataSrc.length;
    }
  }

  private class MHolder extends RecyclerView.ViewHolder {
    TextView title;
    public MHolder(View itemView) {
      super(itemView);
      title = (TextView) itemView;
    }
  }
}
