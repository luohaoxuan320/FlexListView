package com.lehow.newapp.base;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;
import com.lehow.newapp.R;
import com.lehow.newapp.test.CategoryAdapter;
import com.lehow.newapp.test.CategoryFieldProcessor;
import com.lehow.newapp.test.DiscountSelectFieldProcessor;
import com.lehow.newapp.test.FoldCombineFuc;
import com.lehow.newapp.test.NumAdapter;
import com.lehow.newapp.test.NumFieldProcessor;
import com.lehow.newapp.test.SelectAdapter;
import com.lehow.newapp.test.ShowAdapter;
import com.lehow.newapp.test.SimpleSelectFieldProcessor;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function3;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * desc: 要维持这个类的简单和通用性，避免针对特别类型的特殊处理，后面用编译时注解对FlexField初始化注入
 * author: luoh17
 * time: 2018/7/25 9:09
 */
public class FlexEntity {
  //private ArrayList<FlexField> flexFieldArrayList;
  private ArrayList<Class<? extends FieldProxyAdapter>> fieldProxyAdapters;
  private FlexFieldAdapter flexFieldAdapter = new FlexFieldAdapter();
  private SparseArray<FieldProxyAdapter> proxyAdapterCache = new SparseArray<>();//多个field 共用一个proxyAdapter

  /**
   * 这个是在Recyclerview中显示对的数据集，可能只显示allFieldMap中的部分数据
   */
  private ArrayList<String> showFieldList = new ArrayList<>();
  /**
   * field不管显示不显示，都会提前初始化准备好
   */
  private HashMap<String, FlexField> allFieldMap = new HashMap<>();

  public static FlexEntity create(Class<?> flexEntity){
    return new FlexEntity();
  }

  private FlexEntity(){
    //flexFieldArrayList = new ArrayList<>();
    fieldProxyAdapters = new ArrayList<>();

    fieldProxyAdapters.add(CategoryAdapter.class);
    fieldProxyAdapters.add(NumAdapter.class);
    fieldProxyAdapters.add(ShowAdapter.class);
    fieldProxyAdapters.add(SelectAdapter.class);

    final FlexField<Boolean> aCategory = new FlexField("aCategory", true).setSummary("A价款选项")
        .setProxyViewType(getProxyViewType(CategoryAdapter.class))
        .setFlexFieldProcessor(new CategoryFieldProcessor());
    addAndPutField(aCategory);
    final FlexField<Float> aamount = new FlexField("aamount", 1000f).setTitle("A价款总价")
        .setSummary("1000")
        .setProxyViewType(getProxyViewType(NumAdapter.class))
        .setFlexFieldProcessor(new NumFieldProcessor())
        .notifyAdapter(flexFieldAdapter);

    addToMap(aamount);

    FlexField<String> adiscont = new FlexField<String>("adicont", "0").setTitle("折扣信息")
        .setHint("请选择折扣")
        .setProxyViewType(getProxyViewType(SelectAdapter.class))
        .setFlexFieldProcessor(new DiscountSelectFieldProcessor())
        .notifyAdapter(flexFieldAdapter);
    addToMap(adiscont);

    FlexField<Float> adiscontAmount = new FlexField("adiscontAmount", 0f).setTitle("折后总价")
        .setSummary("")
        .setProxyViewType(getProxyViewType(ShowAdapter.class))
        .setFlexFieldProcessor(new NumFieldProcessor()).notifyAdapter(flexFieldAdapter);

    Observable.combineLatest(aamount.valueObservable, adiscont.valueObservable, new BiFunction<Float, String, Float>() {
      @Override public Float apply(Float aFloat, String s) throws Exception {
        float parseFloat = Float.parseFloat(s);
        Log.i("TAG", "apply: 折后总价="+(aFloat+parseFloat));
        return aFloat+parseFloat;
      }
    }).subscribe(adiscontAmount);
    addToMap(adiscontAmount);

    FlexField<Integer> aFirstPercent = new FlexField("aFirstPercent", 3).setTitle("首付成数")
        .setHint("请选择")
        .setSummary("三成")
        .setProxyViewType(getProxyViewType(SelectAdapter.class))
        .setFlexFieldProcessor(new SimpleSelectFieldProcessor(R.array.pay_percent_2,R.array.pay_integer_2,3 - 1))
        .notifyAdapter(flexFieldAdapter);

    addToMap(aFirstPercent);

    FlexField<Float> aFirstAmount = new FlexField("aFirstAmount", 0f).setTitle("首付金额")
        .setHint("请输入")
        .setProxyViewType(getProxyViewType(NumAdapter.class))
        .setFlexFieldProcessor(new NumFieldProcessor())
        .notifyAdapter(flexFieldAdapter);

    addToMap(aFirstAmount);

    Observable.combineLatest(adiscontAmount.valueObservable, aFirstPercent.valueObservable,
        new BiFunction<Float, Integer, Float>() {
          @Override public Float apply(Float aFloat, Integer aFloat2) throws Exception {
            return aFloat*aFloat2*0.1f;
          }
        }).subscribe(aFirstAmount);

    final FlexField<Integer> aLoanType = new FlexField("aLoanType","商业贷款")
        .setTitle("贷款类别")
        .setHint("请选择")
        .setValue(2)
        .setSummary("商业贷款")
        .setProxyViewType(getProxyViewType(SelectAdapter.class)).notifyAdapter(flexFieldAdapter).setFlexFieldProcessor(new SimpleSelectFieldProcessor(R.array.loan_type,R.array.loan_type_value,1));

    addToMap(aLoanType);


    FlexField<String> aLoan = new FlexField("aLoan", "公积金贷款").setTitle("贷款方式")
        .setHint("公积金贷款")
        .setProxyViewType(getProxyViewType(ShowAdapter.class))
        ;
    aLoanType.valueObservable.subscribe();
    addToMap(aLoan);

    FlexField<Float> aLoanAmount =new FlexField("aLoanAmount",0f)
        .setTitle("贷款金额")
        .setHint("请输入")
        .setProxyViewType(getProxyViewType(NumAdapter.class)).notifyAdapter(flexFieldAdapter).setFlexFieldProcessor(new NumFieldProcessor());
    addToMap(aLoanAmount);

    FlexField<Float> aLoanYear = new FlexField("aLoanYear", 0f).setTitle("贷款年数")
        .setHint("请选择")
        .setProxyViewType(getProxyViewType(SelectAdapter.class))
        .notifyAdapter(flexFieldAdapter)
        .setFlexFieldProcessor(
            new SimpleSelectFieldProcessor(R.array.loan_year, R.array.loan_year_value, 30 - 1));

    addToMap(aLoanYear);

    FlexField<Float> aLoanRate =new  FlexField("aLoanRate",0f)
        .setTitle("贷款利率")
        .setHint("请选择")
        .setProxyViewType(getProxyViewType(SelectAdapter.class)).notifyAdapter(flexFieldAdapter);

    addToMap(aLoanRate);


    FlexField<String> aLoan1 =new FlexField("aLoan1","商业贷款")
        .setTitle("贷款方式")
        .setSummary("商业贷款")
        .setProxyViewType(getProxyViewType(ShowAdapter.class));

    addToMap(aLoan1);

    FlexField<Float> aLoan1Amount =new FlexField("aLoan1Amount",0f)
        .setTitle("贷款金额")
        .setHint("请输入")
        .setProxyViewType(getProxyViewType(NumAdapter.class)).notifyAdapter(flexFieldAdapter).setFlexFieldProcessor(new NumFieldProcessor());

    addToMap(aLoan1Amount);


    FlexField<Float> aLoan1Year =new FlexField("aLoan1Year",0f)
        .setTitle("贷款年数")
        .setHint("请选择")
        .setProxyViewType(getProxyViewType(SelectAdapter.class)).notifyAdapter(flexFieldAdapter).setFlexFieldProcessor(
            new SimpleSelectFieldProcessor(R.array.loan_year, R.array.loan_year_value, 30 - 1));

    addToMap(aLoan1Year);

    FlexField<Float> aLoan1Rate = new FlexField("aLoan1Rate",0f)
        .setTitle("贷款利率")
        .setHint("请选择")
        .setProxyViewType(getProxyViewType(SelectAdapter.class)).notifyAdapter(flexFieldAdapter);

    addToMap(aLoan1Rate);

    new FoldCombineFuc(this).getCombineObeserver().subscribe(visibleFieldConsumer);

   /* aLoanType.valueObservable.flatMap(new Function<Integer, ObservableSource<VisibleField>>() {
      @Override public ObservableSource<VisibleField> apply(Integer s) throws Exception {
        //aLoanType.getAdapterPosition() 不用这个是因为 初始化默认值的时候，这个position是默认值-1
        int position = showFieldList.indexOf(aLoanType.getKey());
        if (s==1) {
          return Observable.just(
              new VisibleField(position)
                  .invisible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
                  .invisible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate")
                  .visible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate"));
        } else if (s==2) {
          return Observable.just(
              new VisibleField(position)
                  .invisible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
                  .invisible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate")
                  .visible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate"));
        } else if (s==3) {
          return Observable.just(
              new VisibleField(position).invisible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
                  .invisible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate")
                  .visible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
                  .visible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate"));
        }
        return null;
      }
    }).subscribe(visibleFieldConsumer);*/

    Observable.combineLatest(aamount.valueObservable, aFirstAmount.valueObservable, aLoanType.valueObservable,
        new Function3<Float, Float, Integer, Float>() {
          @Override public Float apply(Float aFloat, Float aFloat2, Integer type)
              throws Exception {
            if (type == 2) {//当前贷款类型为商业贷，才自动计算。组合贷不计算
              return aFloat - aFloat2;
            }else{
              return 0f;
            }
          }
        }).subscribe(aLoan1Amount);

    Observable.combineLatest(aamount.valueObservable, aFirstAmount.valueObservable, aLoanType.valueObservable,
        new Function3<Float, Float, Integer, Float>() {
          @Override public Float apply(Float aFloat, Float aFloat2, Integer type)
              throws Exception {
            if (type == 1) {//当前贷款为公积金，才自动计算
              return aFloat - aFloat2;
            }else{
              return 0f;
            }
          }
        }).subscribe(aLoanAmount);


  }

  /**
   * 初始化字段
   * @param flexField
   */
  private void addAndPutField(FlexField flexField) {
    allFieldMap.put(flexField.getKey(), flexField);
    showFieldList.add(flexField.getKey());
  }

  private void addToMap(FlexField flexField) {
    allFieldMap.put(flexField.getKey(), flexField);
  }

  /**
   * 根据Recyclerview中的数据源showFieldList 的位置，获取其真实的Field对象
   * @param position
   * @return
   */
  private FlexField getTheShowField(int position) {
    return allFieldMap.get(showFieldList.get(position));
  }

  /**
   *
   * 删除时，要一个个的删，因为不知道index，要一个个的notify
   *
   * 移除showFieldList中的字段
   * @param fieldKeys
   * @return
   */
  public boolean invisibleField(ArrayList<String> fieldKeys) {
    int index = 0;
    for (String fieldKey : fieldKeys) {
      index=showFieldList.indexOf(fieldKey);
      if (index >= 0 && (showFieldList.remove(fieldKey))) {
        flexFieldAdapter.notifyItemRemoved(index);
      }else {
       continue;
      }
    }
    return true;
  }

  /**
   * 暂时不要对外暴露
   *
   * 将field插入到showFieldList指定的inserPosition处显示
   * @param insertPosition
   * @param fieldKeys 要批量插入的field对象
   * @return
   */
  private boolean visibleFields(int insertPosition,ArrayList<String> fieldKeys) {

    /*for (String fieldKey : fieldKeys) {
      if (findFlexField(fieldKey)==null) {
        throw new IllegalArgumentException("请检查field的key="+fieldKey+"，不能添加一个未提前映射的field");
      }
    }*/
    boolean b = showFieldList.addAll(Math.min(insertPosition,showFieldList.size()), fieldKeys);
    if (b) {
      flexFieldAdapter.notifyItemRangeInserted(insertPosition,fieldKeys.size());
    }
    return b;
  }

  /**
   * 根据viewType获取proxyAdapter，多个FlexField可以共用一个proxyAdapter（处理ViewHolder）即指向同一个viewType
   * @param viewType
   * @return
   */
  private FieldProxyAdapter getProxyAdapter(int viewType) {
    FieldProxyAdapter fieldProxyAdapter = proxyAdapterCache.get(viewType);
    if (fieldProxyAdapter == null) {
      try {
         fieldProxyAdapter =fieldProxyAdapters.get(viewType).newInstance();//构造一个
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

  private int getProxyViewType(Class<? extends FieldProxyAdapter> proxyAdapter) {
    return fieldProxyAdapters.indexOf(proxyAdapter);
  }
  public RecyclerView.Adapter getFlexAdapter(){
    return flexFieldAdapter;
  }
  private class FlexFieldAdapter extends RecyclerView.Adapter<ProxyViewHolder> implements Consumer<Integer>{
    @NonNull @Override
    public ProxyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      Log.i("TAG", "onCreateViewHolder: ");
      return getProxyAdapter(viewType).onCreateViewHolder(parent);
    }

    @Override public int getItemViewType(int position) {
      return getTheShowField(position).getProxyViewType();
    }

    @Override public void onBindViewHolder(@NonNull ProxyViewHolder holder, int position) {
      Log.i("TAG", "onBindViewHolder: position="+position+"  "+holder.itemView.getContext());
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
      FlexFieldProcessor
          fieldProcessor =getTheShowField(requestCode).getFlexFieldProcessor();
      if (fieldProcessor != null) {
        fieldProcessor.onChange( getTheShowField(requestCode),data.getExtras());
      }
    }
  }


  private Consumer<VisibleField> visibleFieldConsumer=new Consumer<VisibleField>() {
    @Override public void accept(VisibleField visibleField) throws Exception {
     if (visibleField.getPosition()!=-1){
       invisibleField(visibleField.getInvisibleKeys());//先删除
       visibleFields(visibleField.getPosition() + 1, visibleField.getVisibleKeys());
     }
    }
  };
  /**
   * 对field点击事件做拦截处理，比如要先从网络获取数据，或者当前页面弹出选择框
   * @param fieldName
   * @param fieldClick
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
   * @param fieldName
   * @return
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
