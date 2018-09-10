# FlexListView

Recyclerview+Rxjava+编译时注解 快速实现如下列表样式的布局
![Screenshot_2018-09-10-10-00-45.jpg](https://upload-images.jianshu.io/upload_images/1760078-ecd64b15710842b0.jpg?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

由于服务器返回的每个Item对应一个字段属性，这样的布局如果用ScrollView加一行行的view去实现，findView很累，activity中处理view的代码很臃肿，而且后期维护成本很高。

对实体类如下注解

~~~
@FlexEntity()
public class PriceEntity {

  @FlexField(title = "A价款选项", proxyAdapter = CategoryAdapter.class, fieldProcessor = CategoryFieldProcessor.class)
  transient boolean aCategory = true;
  @FlexField(title = "A价款总价", proxyAdapter = NumAdapter.class, fieldProcessor = NumFieldProcessor.class)
  String aamount = "1000";
      //将这个定义为String，方便数据的处理，不然会觉得莫名的多了一个.0
  @FlexField(title = "折扣信息", hint = "请选择折扣", proxyAdapter = SelectAdapter.class, fieldProcessor = DiscountSelectFieldProcessor.class)
  @InjectActivityAndArrayRes()
      String adicont = "0";
  @FlexField(title = "折后总价", hint = "", proxyAdapter = ShowAdapter.class, fieldProcessor = NumFieldProcessor.class)
  @ValueDependence(dependenOn = { "aamount", "adicont" }, func = DiscontAmountFuc.class) String
      adiscontAmount;
  @FlexField(title = "首付成数", hint = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = PercentSelectFieldProcessor.class)
  @InjectActivityAndArrayRes({ R.array.pay_percent_2, R.array.pay_integer_2 })
  @ValueDependence(dependenOn = { "adiscontAmount", "aFirstAmount" }, func = FirstPercentFunc.class)
  int aFirstPercent = 3;
  @FlexField(title = "首付金额", hint = "请选择", proxyAdapter = NumAdapter.class, fieldProcessor = FirstAmountFieldProcessor.class)
  @ValueDependence(dependenOn = { "adiscontAmount", "aFirstPercent" }, func = FirstAmountFuc.class)
  String aFirstAmount;
  @FlexField(title = "贷款类型", hint = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  @InjectActivityAndArrayRes({ R.array.loan_type, R.array.loan_type_value })
  int aLoanType;
  @FlexField(title = "公积金还款方式", summary = "等额本息", proxyAdapter = ShowAdapter.class) String aLoan =
      "公积金贷款";
  @FlexField(title = "公积金贷款金额", summary = "请输入", proxyAdapter = NumAdapter.class, fieldProcessor = NumFieldProcessor.class)
  String aLoanAmount;
  @InjectActivityAndArrayRes({ R.array.loan_year, R.array.loan_year_value })
  @FlexField(title = "贷款年数", summary = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  int aLoanYear;
  @FlexField(title = "利率", summary = "请选择", proxyAdapter = SelectAdapter.class) double aLoanRate;

  @FlexField(title = "商业贷还款方式", summary = "等额本息", proxyAdapter = ShowAdapter.class) String aLoan1;
  @FlexField(title = "商业贷款金额", summary = "请输入", proxyAdapter = NumAdapter.class, fieldProcessor = NumFieldProcessor.class)
  String aLoan1Amount;
  @InjectActivityAndArrayRes({ R.array.loan_year, R.array.loan_year_value })
  @FlexField(title = "贷款年数", summary = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  int aLoan1Year;
  @FlexField(title = "利率", summary = "请选择", proxyAdapter = SelectAdapter.class) double aLoan1Rate;

}
~~~

Activity中将FlexEntity绑定到指定的Recyclerview
~~~
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
    this.flexEntity = FlexEntity.create(this, new PriceEntity());
    recyclerView.setAdapter(this.flexEntity.getFlexAdapter());
    recyclerView.setNestedScrollingEnabled(false);
    recyclerView.setItemAnimator(null);//这里要取消动画，否则在notifyItemChanged时，会onCreateViewHolder 生成一个新的ViewHolder来做动画的过度，
    // 如果当前的item中含有EditText，会导致我在Holder中记录的光标位置无效。当然也可以考虑在别的地方记录光标位置，目前放在这

    
    }
~~~

## 注解说明

- @FlexEntity ：注解一个Entity，编译时会生成对应的XX$$FlexEntity类，聚合了要列表展示的FlexField

-  @FlexField 对应Recyclerview中的一个Item
    - title 左边的标题显示
    - hint 右边的提示
    - proxyAdapter 处理该item field的显示和数据绑定（onCreateViewHolder+onBindViewHolder），如果要做一个临时提示，比如提交数据时，首付+贷款+分期与折后总价不等，
    要临时红色高亮提示，可以通过重写 onBindViewHolder(K holder, T entity, List<Object> payloads)来处理
      ~~~
        @Override
        public void onBindViewHolder(ItemViewHolder holder, FlexField entity, List<Object> payloads) {
          onBindViewHolder(holder, entity);
          if (payloads != null && !payloads.isEmpty()) {
            SpannableStringBuilder stringBuilder = new SpannableStringBuilder(entity.getTitle() + "\n");
            SpannableString spannableString = new SpannableString(payloads.get(0).toString());
            spannableString.setSpan(new AbsoluteSizeSpan(12, true), 0, spannableString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, spannableString.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder.append(spannableString);
            holder.tvTitle.setText(stringBuilder);
          } else {
            holder.tvTitle.setText(entity.getTitle());
          }
        }
      ~~~
      Activity刷新
      ~~~
      flexEntity.getFlexAdapter()
          .notifyItemChanged(flexEntity.indexInShow("aloanAmount"), "首付金额+贷款金额 不等于折后总价");
      
      ~~~
     - fieldProcessor 处理item flex的点击事件和数据加工处理（比如将value 1，转换成 一成 显示）


- InjectActivityAndArrayRes 给fieldProcessor 注入Activity和res资源，比如点击item要跳转到一个显示list选择列表的Activity，
    注意FlexFieldProcessor必须有一个含有含有(Activity activity, int... valueRes)这样的构造器
      ~~~
      public class SimpleSelectFieldProcessor extends FlexFieldProcessor<Integer> {
      int selIndex=-1;
      String[] dataSrc;
      int[] values;
      private Activity activity;

      public SimpleSelectFieldProcessor(Activity context, @ArrayRes int dataRes,
          @ArrayRes int valueRes) {
        activity = context;
        dataSrc = activity.getResources().getStringArray(dataRes);
        values = activity.getResources().getIntArray(valueRes);
      }

      @Override public void onFieldClick(FlexField<Integer> flexField, int adapterPosition) {
        Intent intent = new Intent(activity, SimpleSelectActivity.class);
        intent.putExtra("dataSrc", dataSrc);
        intent.putExtra("selIndex", selIndex);
        activity.startActivityForResult(intent, adapterPosition);
      }
     }
      ~~~
      
      

- @ValueDependence Rxjava 的combineLatest，实现对其他数据的观察监听，更改自己的值
  - dependenOn 要监听的值的字段名
  - func 处理方法，要和combineLatest 入参相匹配


## 建议

### 复杂的数据级联处理
如果要处理复杂的数据联动，比如折后总价变化，首付成数变化，首付金额自动计算，而输入首付金额后，又要自动匹配首付成数。或者如果首付金额，贷款，分期只有一个没有值，就自动推导
那么就不要用ValueDependence去指定了，否则写的想死。RXjava比较适合单向的数据流动，这种彼此通知变化的，可以在代码中，手动设置关联，
~~~
 this.flexEntity.setFieldClickListener("aamount", new FlexFieldProcessor<String>() {});
~~~

然后通过flexEntity.findFlexField 来读取当前的值，和修改值

比如
~~~
 //A首付金额（校验数据不能超过该项价款折后价）
    flexEntity.setFieldClickListener("adownPayAmount", new FlexFieldProcessor<String>() {
      @Override public void onFieldClick(FlexField flexField, int adapterPosition) {
        //donothing
      }

      @Override
      public boolean onChange(FlexField<String> flexField, String newValue, boolean isSelf) {

        if (isSelf) {//自己变化

          //折后总价
          double adiscountTotalPrice = OtherUtils.strTodouble(
              flexEntity.findFlexField("adiscountTotalPrice").getValue().toString());
          double curFirstAmount = OtherUtils.strTodouble(newValue);
          if (curFirstAmount > adiscountTotalPrice) {
            Toast.makeText(NewPriceListDetailAct.this, "首付金额不能超过款项的折后总价", Toast.LENGTH_SHORT)
                .show();
            //刷新显示
            flexField.setValue(
                flexField.getValue());//通知刷新当前的输入，比如前面一次有效输入是10000，现在输入是10100，这个是将输入框的显示还原为10000
            flexField.setSummary(TextUtils.isEmpty(flexField.getValue()) ? ""
                : OtherUtils.doubleTwDecimalStr(OtherUtils.strTodouble(flexField.getValue())));
            return false;
          }
          //计算 首付成数
          FlexField<String> adownPayment = flexEntity.findFlexField("adownPayment");

          if (curFirstAmount == 0) updateFieldValue(adownPayment, "自定义");//首付金额为0，自定义 首付成数
          updateFieldValue(adownPayment, findFirstPercent(adiscountTotalPrice, curFirstAmount));
          //判断贷款类型

          FlexField<String> aloanCategory = flexEntity.findFlexField("aloanCategory");
          FlexField<String> aloanAmount = flexEntity.findFlexField("aloanAmount");
          FlexField<String> aloanAmount1 = flexEntity.findFlexField("aloanAmount1");
          if ("公积金贷款".equals(aloanCategory.getValue())) {
            double v = adiscountTotalPrice - curFirstAmount;
            updateFieldValue(aloanAmount, "" + (v < 0 ? 0 : v));
          } else if ("商业贷款".equals(aloanCategory.getValue())) {
            double v = adiscountTotalPrice - curFirstAmount;
            updateFieldValue(aloanAmount1, "" + (v < 0 ? 0 : v));
          } else if ("组合贷款".equals(aloanCategory.getValue())) {
            double curaloanAmount = OtherUtils.strTodouble(aloanAmount.getValue());
            double curaloanAmount1 = OtherUtils.strTodouble(aloanAmount1.getValue());
            if (!isEmpty(aloanAmount) && !isEmpty(aloanAmount1)) {//当前组合贷都有值，那么清空
              updateFieldValue(aloanAmount, "");
              updateFieldValue(aloanAmount1, "");
            } else if (isEmpty(aloanAmount) && isEmpty(aloanAmount1)) {//都为0，无法推导，不处理
              updateFieldValue(aloanAmount, "");
              updateFieldValue(aloanAmount1, "");
            } else if (isEmpty(aloanAmount)) {//只有一个为0，推导另一个
              curaloanAmount = adiscountTotalPrice - curFirstAmount - curaloanAmount1;
              updateFieldValue(aloanAmount, "" + Math.max(curaloanAmount, 0));
              updateFieldValue(aloanAmount1, aloanAmount1.getValue());//刷新，去掉红色提示
            } else if (isEmpty(aloanAmount1)) {
              curaloanAmount1 = adiscountTotalPrice - curFirstAmount - curaloanAmount;
              updateFieldValue(aloanAmount1, "" + Math.max(curaloanAmount1, 0));
              updateFieldValue(aloanAmount, aloanAmount.getValue());//刷新，去掉红色提示
            }
          }
        }

        flexField.setSummary(TextUtils.isEmpty(newValue) ? ""
            : OtherUtils.doubleTwDecimalStr(OtherUtils.strTodouble(newValue)));
        //通知Recyclerview刷新
        flexField.setValue(newValue);
        return true;
      }

      @Override public void onActivityResult(FlexField<String> flexField, Bundle resultData) {

      }
    });

~~~

### 选项折叠处理
有用注解集成进去过，但是感觉不太适用，还是写在代码里。
这里也有一个坑，当Recyclerview外层嵌套有ScrollView的时候，折叠时Recyclerview的高度显示会有问题，比如一进去的时候只显示4个，展开后显示8个，后面的4个看不到。

== 此时需要在折叠操作后，调用Recyclerview 的 requestLayout，重新measure和layout

~~~

 //观察值，做自更新
 flexEntity.setFieldClickListener("aLoanType", new SimpleSelectFieldProcessor(this,R.array.loan_type,R.array.loan_type_value){
      @Override public boolean onChange(FlexField<Integer> flexField, Integer newValue, boolean isSelf) {
        super.onChange(flexField, newValue,isSelf);
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
            flexEntity.visibleFields(flexEntity.indexInShow("aLoanType")+1, add);
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
            flexEntity.visibleFields(flexEntity.indexInShow("aLoanType")+1, add);
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
            flexEntity.visibleFields(flexEntity.indexInShow("aLoanType")+1, add);
            break;
        }
        return true;
      }
    });
~~~

