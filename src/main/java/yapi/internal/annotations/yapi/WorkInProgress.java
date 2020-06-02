// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.internal.annotations.yapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface WorkInProgress {
    WorkInProgressType context() default WorkInProgressType.TESTING;
}