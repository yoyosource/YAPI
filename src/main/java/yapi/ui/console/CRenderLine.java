// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

import java.util.ArrayList;
import java.util.List;

public class CRenderLine {

    RenderController controller;

    List<CRenderSnippet> snippets = new ArrayList<>();

    public CRenderLine(RenderController controller) {
        this.controller = controller;
        snippets.add(new CRenderSnippet(controller));
    }

    public void add(ConsoleMessageTask task) {
        controller.add(task, this);
    }

    public boolean isEmpty() {
        return snippets.isEmpty();
    }

    public List<CRenderSnippet> getSnippets() {
        return snippets;
    }

}