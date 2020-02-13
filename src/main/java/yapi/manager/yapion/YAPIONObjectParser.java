// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion;

import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;
import yapi.color.ColorUtils;
import yapi.string.StringFormatting;

import java.awt.*;

public class YAPIONObjectParser {

    public static YAPIONObject toYAPIONObject(Color color) {
        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(getTypeVariable("color"));
        yapionObject.add(getVariable("color", "#" + StringFormatting.toHex(new byte[]{(byte)color.getRed(), (byte)color.getGreen(), (byte)color.getBlue(), (byte)color.getAlpha()})));
        return yapionObject;
    }

    public static Color toColor(YAPIONObject yapionObject) {
        if (yapionObject.getKeys().size() == 2 && yapionObject.getKeys().contains("object-type") && yapionObject.getKeys().contains("color")) {
            if (yapionObject.getValue("object-type").getString().equals("color")) {
                return ColorUtils.getColor(yapionObject.getValue("color").toString());
            }
        }
        return null;
    }

    private static YAPIONVariable getTypeVariable(String type) {
        return new YAPIONVariable("object-type", new YAPIONValue(type));
    }

    private static YAPIONVariable getVariable(String key, String value) {
        return new YAPIONVariable(key, new YAPIONValue(value));
    }

}