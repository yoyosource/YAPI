// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.ui.console;

public interface RenderController {

    void add(ConsoleMessageTask task, CRenderAll renderAll);

    void add(ConsoleMessageTask task, CRenderLine renderLine);

    void add(ConsoleMessageTask task, CRenderSnippet renderSnippet);

    ConsoleAlignment getAlignment();

    ConsoleClipping getClipping();

    int getWidth();

}