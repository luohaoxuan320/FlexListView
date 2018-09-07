package com.lehow.flex.base;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/30 22:02
 */
public class VisibleField {

  private int position;
  private ArrayList<String> visibleKeys;
  private ArrayList<String> invisibleKeys;

  public VisibleField(int position) {
    this.position = position;
    this.visibleKeys = new ArrayList<>();
    this.invisibleKeys = new ArrayList<>();
  }

  public VisibleField visible(String... fieldKeys) {
    visibleKeys.addAll(Arrays.asList(fieldKeys));
    return this;
  }

  public VisibleField invisible(String... fieldKeys) {
    invisibleKeys.addAll(Arrays.asList(fieldKeys));
    return this;
  }

  public int getPosition() {
    return position;
  }

  public ArrayList<String> getVisibleKeys() {
    return visibleKeys;
  }

  public ArrayList<String> getInvisibleKeys() {
    return invisibleKeys;
  }
}
