// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.yui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YUI {

    private List<YUIConfig> configs = new ArrayList<>();

    public YUI(File config) {
        configs.add(new YUIConfig(config));
    }

}