package com.lehow.newapp.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/4 15:57
 */
@Retention(CLASS) @Target(TYPE)
public @interface FlexEntity {
}
