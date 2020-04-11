// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.runtime;

import java.util.HashMap;
import java.util.Map;

public class HookManager {

    private HookManager() {
        throw new IllegalStateException("Utility class");
    }

    private static Map<Long, Hook> hookMap = new HashMap<>();

    public static void addHook(Hook hook) {
        hookMap.put(hook.getId(), hook);
    }

    public static boolean hasHook(Hook hook) {
        return  hookMap.containsKey(hook.getId());
    }

    public static Thread getHookThread(Hook hook) {
        if (!hasHook(hook)) {
            addHook(hook);
        }
        return new Thread(hookMap.get(hook.getId()));
    }

}