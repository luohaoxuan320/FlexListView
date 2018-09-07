package com.lehow.flex.annotations;

import android.support.annotation.ArrayRes;

/**
 * desc:使用这个注解的field的fieldProcessor 必须有
 * SimpleSelectFieldProcessor(Context context,@ArrayRes int...valueRes)
 * 这样的构造器，否则会崩溃
 * author: luoh17
 * time: 2018/8/8 8:54
 */
public @interface InjectActivityAndArrayRes {

  @ArrayRes int[] value() default 0;
}
