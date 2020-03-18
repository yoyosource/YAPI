// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.serializable;

import yapi.manager.yapion.YAPIONParser;
import yapi.manager.yapion.internal.serializable.YAPIONDeserializable;
import yapi.manager.yapion.value.YAPIONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class YAPIONDeserializer {

    public static void main(String[] args) {
        YAPIONObject yapionObject = YAPIONParser.parseObject("{type(yapi.manager.yapion.Test)content{i(10.0)hugo{type(yapi.manager.yapion.Test$Hugo)content{i(0)t{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}hugo2{type(yapi.manager.yapion.Test$Hugo$Hugo2)content{i(0)this$1->0002Aa1elg0e9djI}}this$0{type(yapi.manager.yapion.Test)content{i(10.0)hugo->0002Aa1elg0e9djItru(\"true\")hugo2->0002Aa1elg0e9djItest(0.0)}}}}tru(\"true\")hugo2->0002FK9dGl9giCH0test(0.0)}}");
        System.out.println(yapionObject);

        YAPIONDeserializer yapionDeserializer = new YAPIONDeserializer(yapionObject);
    }

    public YAPIONDeserializer(YAPIONObject yapionObject) {
        Object o = constructObject(yapionObject.getValue("type").getString());
        System.out.println(o.getClass().getName());
    }

    private YAPIONDeserializable constructObject(String className) {
        try {
            if (className.contains("$")) {
                String cName = className.substring(0, className.lastIndexOf('$'));
                YAPIONDeserializable yapionDeserializable = constructObject(cName);

                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getDeclaredConstructor(yapionDeserializable.getClazz());
                constructor.setAccessible(true);
                return new YAPIONDeserializable(clazz, constructor.newInstance(yapionDeserializable.getObject()));
            } else {
                Class<?> clazz = Class.forName(className);
                Constructor<?> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                return new YAPIONDeserializable(clazz, constructor.newInstance());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

}