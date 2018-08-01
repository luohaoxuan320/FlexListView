package com.lehow.newapp.annotations;

import com.lehow.newapp.base.FlexFieldProcessor;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 11:40
 */
@Retention(CLASS) @Target(FIELD)
public @interface FieldClick{
  Class<? extends FlexFieldProcessor> processClass();
}
