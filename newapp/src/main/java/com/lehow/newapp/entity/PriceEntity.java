package com.lehow.newapp.entity;

import android.support.annotation.Nullable;
import com.lehow.flex.annotations.FlexEntity;
import com.lehow.flex.annotations.FlexField;
import com.lehow.flex.annotations.InjectActivityAndArrayRes;
import com.lehow.flex.annotations.ValueDependence;
import com.lehow.newapp.R;
import com.lehow.newapp.test.CategoryAdapter;
import com.lehow.newapp.test.CategoryFieldProcessor;
import com.lehow.newapp.test.DiscontAmountFuc;
import com.lehow.newapp.test.DiscountSelectFieldProcessor;
import com.lehow.newapp.test.FirstAmountFieldProcessor;
import com.lehow.newapp.test.FirstAmountFuc;
import com.lehow.newapp.test.FirstPercentFunc;
import com.lehow.newapp.test.NumAdapter;
import com.lehow.newapp.test.NumFieldProcessor;
import com.lehow.newapp.test.PercentSelectFieldProcessor;
import com.lehow.newapp.test.SelectAdapter;
import com.lehow.newapp.test.ShowAdapter;
import com.lehow.newapp.test.SimpleSelectFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/1 9:15
 */

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
