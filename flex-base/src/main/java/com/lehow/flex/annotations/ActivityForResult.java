package com.lehow.flex.annotations;

import com.lehow.flex.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 14:14
 */
public @interface ActivityForResult {
    Class<? extends FlexFieldProcessor> processClass();
}
