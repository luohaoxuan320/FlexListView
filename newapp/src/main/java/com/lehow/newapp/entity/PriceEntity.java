package com.lehow.newapp.entity;

import com.lehow.flex.annotations.FlexEntity;
import com.lehow.flex.annotations.FlexField;
import com.lehow.flex.annotations.CombineVisible;
import com.lehow.newapp.test.CategoryAdapter;
import com.lehow.newapp.test.CategoryFieldProcessor;
import com.lehow.newapp.test.DiscountSelectFieldProcessor;
import com.lehow.newapp.test.FoldCombineFuc;
import com.lehow.newapp.test.NumAdapter;
import com.lehow.newapp.test.NumFieldProcessor;
import com.lehow.newapp.test.SelectAdapter;
import com.lehow.newapp.test.ShowAdapter;
import com.lehow.newapp.test.SimpleSelectFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/1 9:15
 */

@FlexEntity(combineVisible = {
    @CombineVisible(keys = { "aCategory", "aLoanType" }, combineFuc = FoldCombineFuc.class)
})
public class PriceEntity {

  @FlexField(title = "A价款选项", proxyAdapter = CategoryAdapter.class, fieldProcessor = CategoryFieldProcessor.class)
  transient boolean aCategory = true;
  @FlexField(title = "A价款总价", proxyAdapter = NumAdapter.class) String aamount = "1000";
      //将这个定义为String，方便数据的处理，不然会觉得莫名的多了一个.0
  @FlexField(title = "折扣信息", hint = "请选择折扣", proxyAdapter = SelectAdapter.class, fieldProcessor = DiscountSelectFieldProcessor.class)
  String adicont;
  @FlexField(title = "折后总价", hint = "", proxyAdapter = ShowAdapter.class, fieldProcessor = NumFieldProcessor.class)
  double adiscontAmount = 700;
  @FlexField(title = "首付成数", hint = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  int aFirstPercent = 3;
  @FlexField(title = "首付金额", hint = "请选择", proxyAdapter = NumAdapter.class, fieldProcessor = NumFieldProcessor.class)
  double aFirstAmount;
  @FlexField(title = "贷款类型", hint = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  int aLoanType;

  String aLoan;
  double aLoanAmount;
  int aLoanYear;
  double aLoanRate;

  String aLoan1;
  double aLoan1Amount;
  int aLoan1Year;
  double aLoan1Rate;

}
