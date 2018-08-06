package com.lehow.flex.base;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import io.reactivex.functions.Consumer;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/3 17:13
 */
public abstract class FlexEntity<K> {
  /**
   * 这个是在Recyclerview中显示对的数据集，可能只显示allFieldMap中的部分数据
   */
  private ArrayList<String> showFieldList = new ArrayList<>();
  /**
   * field不管显示不显示，都会提前初始化准备好
   */
  private HashMap<String, FlexField> allFieldMap = new HashMap<>();
  private FlexFieldAdapter flexFieldAdapter = new FlexFieldAdapter();
  private ArrayList<Class<? extends FieldProxyAdapter>> fieldProxyAdapters = new ArrayList<>();
  private SparseArray<FieldProxyAdapter> proxyAdapterCache = new SparseArray<>();
      //多个field 共用一个proxyAdapter
  protected K entity;

  protected FlexEntity(K entity) {
    this.entity = entity;
    creteFieldList();
  }

  public static <T> FlexEntity create(T entity) {
    String className = entity.getClass().getName();
    try {
      Class<? extends FlexEntity> flexEntity =
          (Class<? extends FlexEntity>) Class.forName(className + "$$FlexEntity");
      Constructor<? extends FlexEntity> constructor =
          flexEntity.getDeclaredConstructor(entity.getClass());
      constructor.setAccessible(true);
      return constructor.newInstance(entity);
    } catch (Exception e) {
      throw new RuntimeException("Unable to inject for " + className, e);
    }
  }

  protected abstract void creteFieldList();

  protected <T> void add(FlexField<T> flexField, boolean isShow) {
    flexField.notifyAdapter(flexFieldAdapter);
    allFieldMap.put(flexField.getKey(), flexField);
    if (isShow) showFieldList.add(flexField.getKey());
  }

  /**
   * 根据viewType获取proxyAdapter，多个FlexField可以共用一个proxyAdapter（处理ViewHolder）即指向同一个viewType
   */
  public FieldProxyAdapter getProxyAdapter(int viewType) {
    FieldProxyAdapter fieldProxyAdapter = proxyAdapterCache.get(viewType);
    if (fieldProxyAdapter == null) {
      try {
        fieldProxyAdapter = fieldProxyAdapters.get(viewType).newInstance();//构造一个
        proxyAdapterCache.put(viewType, fieldProxyAdapter);
        return fieldProxyAdapter;
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return fieldProxyAdapter;
  }

  protected int getProxyViewType(Class<? extends FieldProxyAdapter> proxyAdapter) {
    int index = fieldProxyAdapters.indexOf(proxyAdapter);
    if (index == -1) {
      fieldProxyAdapters.add(proxyAdapter);
      return fieldProxyAdapters.size() - 1;
    }
    return index;
  }

  /**
   * 根据Recyclerview中的数据源showFieldList 的位置，获取其真实的Field对象
   */
  private FlexField getTheShowField(int position) {
    return allFieldMap.get(showFieldList.get(position));
  }

  /**
   * 删除时，要一个个的删，因为不知道index，要一个个的notify
   *
   * 移除showFieldList中的字段
   */
  public boolean invisibleField(ArrayList<String> fieldKeys) {
    int index = 0;
    for (String fieldKey : fieldKeys) {
      index = showFieldList.indexOf(fieldKey);
      if (index >= 0 && (showFieldList.remove(fieldKey))) {
        flexFieldAdapter.notifyItemRemoved(index);
      } else {
        continue;
      }
    }
    return true;
  }

  /**
   * 暂时不要对外暴露
   *
   * 将field插入到showFieldList指定的inserPosition处显示
   *
   * @param fieldKeys 要批量插入的field对象
   */
  private boolean visibleFields(int insertPosition, ArrayList<String> fieldKeys) {

    /*for (String fieldKey : fieldKeys) {
      if (findFlexField(fieldKey)==null) {
        throw new IllegalArgumentException("请检查field的key="+fieldKey+"，不能添加一个未提前映射的field");
      }
    }*/
    boolean b = showFieldList.addAll(Math.min(insertPosition, showFieldList.size()), fieldKeys);
    if (b) {
      flexFieldAdapter.notifyItemRangeInserted(insertPosition, fieldKeys.size());
    }
    return b;
  }

  public RecyclerView.Adapter getFlexAdapter() {
    return flexFieldAdapter;
  }

  private class FlexFieldAdapter extends RecyclerView.Adapter<ProxyViewHolder>
      implements Consumer<Integer> {
    @NonNull @Override
    public ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      Log.i("TAG", "onCreateViewHolder: ");
      return getProxyAdapter(viewType).onCreateViewHolder(parent);
    }

    @Override public int getItemViewType(int position) {
      return getTheShowField(position).getProxyViewType();
    }

    @Override public void onBindViewHolder(@NonNull ProxyViewHolder holder, int position) {
      Log.i("TAG", "onBindViewHolder: position=" + position + "  " + holder.itemView.getContext());
      FlexField flexField = getTheShowField(position);
      flexField.setAdapterPosition(position);
      getProxyAdapter(holder.getItemViewType()).onBindViewHolder(holder, flexField);
    }

    @Override public int getItemCount() {
      return showFieldList.size();
    }

    @Override public void onViewRecycled(@NonNull ProxyViewHolder holder) {
      holder.onReset();
      super.onViewRecycled(holder);
    }

    @Override public void accept(Integer integer) throws Exception {
      notifyItemChanged(integer);
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == Activity.RESULT_OK) {
      FlexFieldProcessor fieldProcessor = getTheShowField(requestCode).getFlexFieldProcessor();
      if (fieldProcessor != null) {
        fieldProcessor.onChange(getTheShowField(requestCode), data.getExtras());
      }
    }
  }

  private Consumer<VisibleField> visibleFieldConsumer = new Consumer<VisibleField>() {
    @Override public void accept(VisibleField visibleField) throws Exception {
      if (visibleField.getPosition() != -1) {
        invisibleField(visibleField.getInvisibleKeys());//先删除
        visibleFields(visibleField.getPosition() + 1, visibleField.getVisibleKeys());
      }
    }
  };

  /**
   * 对field点击事件做拦截处理，比如要先从网络获取数据，或者当前页面弹出选择框
   */
  public void setFieldClickListener(String fieldName, FlexFieldProcessor fieldClick) {
    allFieldMap.get(fieldName).setFlexFieldProcessor(fieldClick);
   /* for (FlexField flexField : showFieldList) {
      if (flexField.getKey().equals(fieldName)) {
        flexField.setFlexFieldProcessor(fieldClick);
        return;
      }
    }*/
  }

  /**
   * 根据field名字查找FlexField
   */
  public FlexField findFlexField(String fieldName) {
    return allFieldMap.get(fieldName);
  }

  /**
   * 当前的key对应的字段在showList中的位置。注意这个跟findFlexField 返回的FlexField 中获取的adapterPosition的区别，
   * adapterPosition只有bindViewHolder后，这个adapterPosition才是有意义的
   * 而indexInShow 可以识别没有onBindView的，但是在showList中的field，这个跟准确
   */
  public int indexInShow(String fieldName) {
    return showFieldList.indexOf(fieldName);
  }
}
