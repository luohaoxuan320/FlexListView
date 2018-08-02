package com.lehow.newapp.entity;

import com.lehow.newapp.annotations.FlexEntity;
import com.lehow.newapp.annotations.FlexField;
import com.lehow.newapp.annotations.CombineVisible;
import com.lehow.newapp.test.CategoryAdapter;
import com.lehow.newapp.test.CategoryFieldProcessor;
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

  @FlexField(title = "A价款总价", proxyAdapter = CategoryAdapter.class, fieldProcessor = CategoryFieldProcessor.class)
  private transient String aCategory;
  @FlexField(title = "A价款总价", proxyAdapter = NumAdapter.class, fieldProcessor = NumFieldProcessor.class, value = "1000")
  private double aamount;
  @FlexField(title = "折扣信息", hint = "请选择折扣", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  private String adicont;
  @FlexField(title = "折后总价", hint = "", proxyAdapter = ShowAdapter.class, fieldProcessor = NumFieldProcessor.class)
  private double adiscontAmount;
  @FlexField(title = "首付成数", hint = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  private int aFirstPercent;
  @FlexField(title = "首付金额", hint = "请选择", proxyAdapter = NumAdapter.class, fieldProcessor = NumFieldProcessor.class)
  private double aFirstAmount;
  @FlexField(title = "贷款类型", hint = "请选择", proxyAdapter = SelectAdapter.class, fieldProcessor = SimpleSelectFieldProcessor.class)
  private int aLoanType;

  private String aLoan;
  private double aLoanAmount;
  private int aLoanYear;
  private double aLoanRate;

  private String aLoan1;
  private double aLoan1Amount;
  private int aLoan1Year;
  private double aLoan1Rate;

}
