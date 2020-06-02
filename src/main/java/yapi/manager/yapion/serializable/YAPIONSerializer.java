// SPDX-License-Identifier: Apache-2.0
// YAPI
// Copyright (C) 2019,2020 yoyosource

package yapi.manager.yapion.serializable;

import yapi.internal.annotations.yapion.YAPIONSave;
import yapi.internal.annotations.yapion.YAPIONSaveExclude;
import yapi.internal.runtimeexceptions.objectnotation.YAPIONException;
import yapi.manager.yapion.Test;
import yapi.manager.yapion.YAPIONType;
import yapi.manager.yapion.YAPIONVariable;
import yapi.manager.yapion.internal.serializable.YAPIONSerializable;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;

import java.lang.reflect.Field;
import java.util.*;

public class YAPIONSerializer {

    public static void main(String[] args) {
        YAPIONSerializer yapionSerializer = new YAPIONSerializer(new Test(), "");
        System.out.println(yapionSerializer.getYapionObject().toHierarchyString());
        System.out.println(yapionSerializer.getYapionObject().toString());
    }

    private static final List<String> implementedTypes = new ArrayList<>();
    private static final List<String> implementedObjects = new ArrayList<>();
    static {
        implementedTypes.add("java.lang.String");
        implementedTypes.add("java.lang.Character");
        implementedTypes.add("java.lang.Integer");
        implementedTypes.add("java.lang.Long");
        implementedTypes.add("java.lang.Float");
        implementedTypes.add("java.lang.Double");
        implementedTypes.add("java.math.BigDecimal");
        implementedTypes.add("java.math.BigInteger");
        implementedTypes.add("char");
        implementedTypes.add("int");
        implementedTypes.add("long");
        implementedTypes.add("float");
        implementedTypes.add("double");

        implementedObjects.add("java.util.List");
    }

    private int objectID = 0;

    private YAPIONObject yapionObject;
    private List<YAPIONSerializable> yapionSerializables = new ArrayList<>();

    public YAPIONSerializer(Object object, String state) {
        if (state.contains(" ")) {
            throw new YAPIONException();
        }
        YAPIONType yapionType = serialize(object, state);
        if (yapionType instanceof YAPIONObject) {
            this.yapionObject = (YAPIONObject) yapionType;
            return;
        }
        throw new YAPIONException();
    }

    public YAPIONObject getYapionObject() {
        if (yapionObject == null) {
            throw new YAPIONException();
        }
        return yapionObject;
    }

    private YAPIONType serialize(Object object, String state) {
        int index = isSerialized(object);
        if (index > 0) {
            return yapionSerializables.get(index).getPointer();
        }

        YAPIONObject yapionObject = new YAPIONObject();
        if (object.getClass().getSimpleName().contains("$")) {
            throw new YAPIONException();
        }
        yapionObject.add(new YAPIONVariable("type", new YAPIONValue(object.getClass().getName())));
        YAPIONObject content = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("content", content));
        objectID++;

        YAPIONSerializable yapionSerializable = new YAPIONSerializable(object, objectID, yapionObject);
        yapionSerializables.add(yapionSerializable);
        List<Field> fields = getFields(object, state);

        for (Field f : fields) {
            try {
                f.setAccessible(true);
                Object obj = f.get(object);
                String type = f.getGenericType().getTypeName();
                addValue(obj, type, f.getName(), state, content);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }

        return yapionObject;
    }

    private boolean contains(String type) {
        for (String s : implementedObjects) {
            if (type.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    private void addValue(Object obj, String type, String name, String state, YAPIONObject content) {
        if (contains(type)) {
            // Todo: Implement List and array, Map
        } else if (implementedTypes.contains(type)) {
            YAPIONValue yapionValue = new YAPIONValue(obj, true);
            YAPIONVariable yapionVariable = new YAPIONVariable(name, yapionValue);
            content.add(yapionVariable);
        } else {
            YAPIONType yapionType = serialize(obj, state);
            YAPIONVariable yapionVariable = new YAPIONVariable(name, yapionType);
            content.add(yapionVariable);
        }
    }

    private int isSerialized(Object obj) {
        for (int i = 0; i < yapionSerializables.size(); i++) {
            if (yapionSerializables.get(i).checkObjectEquality(obj)) {
                return i;
            }
        }
        return -1;
    }

    private YAPIONObject errorCreator(String message) {
        boolean b = false;
        if (message.startsWith("*")) {
            b = true;
        }
        message = message.substring(55);
        message = message.substring(message.indexOf("class ") + 6);
        String className = message.substring(0, message.indexOf(' '));

        YAPIONObject yapionObject = new YAPIONObject();
        yapionObject.add(new YAPIONVariable("class-name", new YAPIONValue(className)));

        int errorID;
        String description;

        if (message.contains("public")) {
            if (b) {
                errorID = 5;
                description = "Public getter cannot be accessed by YAPIONSerializer within inner or private class";
            } else {
                errorID = 1;
                description = "Public fields cannot be accessed by YAPIONSerializer within inner or private class";
            }
        } else if (message.contains("protected")) {
            if (b) {
                errorID = 6;
                description = "Protected getter cannot be accessed by YAPIONSerializer";
            } else {
                errorID = 2;
                description = "Protected fields cannot be accessed by YAPIONSerializer";
            }
        } else if (message.contains("private")) {
            if (b) {
                errorID = 7;
                description = "Private getter cannot be accessed by YAPIONSerializer";
            } else {
                errorID = 3;
                description = "Private fields cannot be accessed by YAPIONSerializer";
            }
        } else {
            if (b) {
                errorID = 8;
                description = "Unknown error";
            } else {
                errorID = 4;
                description = "Unknown error";
            }
        }

        yapionObject.add(new YAPIONVariable("error-id", new YAPIONValue(errorID, true)));
        yapionObject.add(new YAPIONVariable("description", new YAPIONValue(description)));
        return yapionObject;
    }

    private List<Field> getFields(Object object, String state) {
        YAPIONSaveExclude typeExclude = object.getClass().getAnnotation(YAPIONSaveExclude.class);
        YAPIONSave typeSave = object.getClass().getAnnotation(YAPIONSave.class);

        if (typeExclude == null && typeSave == null) {
            return getFields(object, state, false, false);
        } else if (typeExclude != null && getContext(typeExclude.context(), state).contains(state) && typeSave == null) {
            return getFields(object, state, false, true);
        } else if (typeExclude == null && typeSave != null && getContext(typeSave.context(), state).contains(state)) {
            return getFields(object, state, true, false);
        } else if (typeExclude != null && getContext(typeExclude.context(), state).contains(state) && getContext(typeSave.context(), state).contains(state)) {
            return getFields(object, state, true, true);
        }
        return new ArrayList<>();
    }

    private List<Field> getFields(Object object, String state, boolean typeSave, boolean typeExclude) {
        Field[] fields = object.getClass().getDeclaredFields();
        List<Field> fieldList = new ArrayList<>();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];

            if (!typeSave && !typeExclude) {
                YAPIONSave yapionSave = f.getAnnotation(YAPIONSave.class);

                if (yapionSave != null && getContext(yapionSave.context(), state).contains(state)) {
                    fieldList.add(f);
                }
            } else if (typeSave && !typeExclude) {
                YAPIONSaveExclude yapionSaveExclude = f.getAnnotation(YAPIONSaveExclude.class);

                if (!(yapionSaveExclude != null && getContext(yapionSaveExclude.context(), state).contains(state))) {
                    fieldList.add(f);
                }
            } else if (typeSave && typeExclude) {
                YAPIONSave yapionSave = f.getAnnotation(YAPIONSave.class);
                YAPIONSaveExclude yapionSaveExclude = f.getAnnotation(YAPIONSaveExclude.class);

                if (yapionSave != null && getContext(yapionSave.context(), state).contains(state)) {
                    fieldList.add(f);
                    continue;
                }
                if (yapionSaveExclude != null && getContext(yapionSaveExclude.context(), state).contains(state)) {
                    continue;
                }
                fieldList.add(f);
            }
        }
        return fieldList;
    }

    private List<String> getContext(String context, String state) {
        List<String> strings = new ArrayList<>();
        String[] str = context.split(" ");
        if (str.length == 0) {
            strings.add(state);
        }
        for (int i = 0; i < str.length; i++) {
            if (str[i].isEmpty()) {
                strings.add(state);
            } else {
                strings.add(str[i]);
            }
        }
        return strings;
    }

}