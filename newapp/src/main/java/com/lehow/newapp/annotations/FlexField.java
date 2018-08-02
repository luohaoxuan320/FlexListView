package com.lehow.newapp.annotations;

import com.lehow.newapp.base.FieldProxyAdapter;
import com.lehow.newapp.base.FlexFieldProcessor;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.PrimitiveIterator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 10:59
 */
@Retention(CLASS) @Target(FIELD)
public @interface FlexField {
  /**
   * key 默认字段名
   * @return
   */
  String key() default "";//不指定会默认给为字段的名字

  /**
   * 标题
   * @return
   */
  String title();

  /**
   * 提示
   * @return
   */
  String hint() default "请输入";

  /**
   * 显示文字（关于value处理，先不考虑）
   * @return
   */
  String summary() default "";

  /**
   * 在Recyclerview中显示的样式
   * @return
   */
  Class<? extends FieldProxyAdapter> proxyAdapter();

  Class<? extends FlexFieldProcessor> fieldProcessor();

  /**
   * 定义为String，方便接收数据
   * 注入的时候会根据字段的实际类型，转换
   */
  String value() default "";


}
