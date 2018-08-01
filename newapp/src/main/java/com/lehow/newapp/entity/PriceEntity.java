package com.lehow.newapp.entity;

import com.lehow.newapp.annotations.FlexEntity;
import com.lehow.newapp.annotations.FlexField;
import com.lehow.newapp.test.CategoryAdapter;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/1 9:15
 */

@FlexEntity
public class PriceEntity {

  @FlexField(title = "A价款总价",proxyAdapter= CategoryAdapter.class)
  private transient String aCategory;
  private double aamount;
  private String adicont;
  private double adiscontAmount;
  private int aFirstPercent;
  private double aFirstAmount;
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
