package com.lehow.newapp.test;

import android.util.Log;
import com.lehow.newapp.entity.FlexEntity;
import com.lehow.flex.base.VisibleField;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;

/**
 * desc: TODO: 这里要不要把  Observable.combineLatest(aCategory.valueObservable, aLoanType.valueObservable,
 * new FoldCombineFuc(this))
 *
 * 这一个集成进来
 * author: luoh17
 * time: 2018/8/2 14:17
 */
public class FoldCombineFuc implements BiFunction<Boolean, Integer, VisibleField> {
  FlexEntity flexEntity;

  public FoldCombineFuc(FlexEntity flexEntity) {
    this.flexEntity = flexEntity;
  }

  public Observable getCombineObeserver() {
    return Observable.combineLatest(flexEntity.findFlexField("aCategory").getValueObservable(),
        flexEntity.findFlexField("aLoanType").getValueObservable(), this);
  }

  @Override public VisibleField apply(Boolean aBoolean, Integer type) throws Exception {
    Log.i("TAG", "apply: aCategory and aLoanType aBoolean=" + aBoolean + " type=" + type);
    int position = flexEntity.indexInShow("aLoanType");
    VisibleField visibleField;
    if (aBoolean) {//分类展开了
      if (position == -1) {
        position = flexEntity.indexInShow("aCategory");
        visibleField = new VisibleField(position).visible("aamount", "adicont", "adiscontAmount",
            "aFirstPercent", "aFirstAmount", "aLoanType");
      } else {//贷款类别变化
        visibleField = new VisibleField(position);
      }

      if (type == 1) {
        return visibleField.invisible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
            .invisible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate")
            .visible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate");
      } else if (type == 2) {
        return visibleField.invisible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
            .invisible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate")
            .visible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate");
      } else if (type == 3) {
        return visibleField.invisible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
            .invisible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate")
            .visible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
            .visible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate");
      }
      return null;
    } else {
      return new VisibleField(position).invisible("aLoan", "aLoanAmount", "aLoanYear", "aLoanRate")
          .invisible("aLoan1", "aLoan1Amount", "aLoan1Year", "aLoan1Rate")
          .invisible("aamount", "adicont", "adiscontAmount", "aFirstPercent", "aFirstAmount",
              "aLoanType");
    }
  }
}
