package com.lehow.flex.annotations;

import com.lehow.flex.base.CombineFuc;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc:标记那些自己值变化，需要控制显示和隐藏的哪些字段
 * author: luoh17
 * time: 2018/7/24 10:59
 */
@Retention(CLASS) @Target(TYPE) public @interface CombineVisible {

  /**
   * 级联的key
   */
  String[] keys();

  /**
   * 这里是Rx的封装，要注意keys与combineProcessor的Function的对应
   */
  Class<? extends CombineFuc> combineFuc();
}
