// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.os;

@FunctionalInterface
public interface OSChecker {

    String check(String s);

}