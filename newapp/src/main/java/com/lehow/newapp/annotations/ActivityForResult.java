package com.lehow.newapp.annotations;

import com.lehow.newapp.base.FlexFieldProcessor;

/**
 * desc:
 * author: luoh17
 * time: 2018/7/24 14:14
 */
public @interface ActivityForResult {
    Class<? extends FlexFieldProcessor> processClass();
}
