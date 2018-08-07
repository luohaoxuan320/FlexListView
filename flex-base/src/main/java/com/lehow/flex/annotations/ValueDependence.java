package com.lehow.flex.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc:
 * author: luoh17
 * time: 2018/8/6 9:34
 */
@Retention(CLASS) @Target(FIELD) public @interface ValueDependence {
  String[] dependenOn();

  Class<?> function();
}
