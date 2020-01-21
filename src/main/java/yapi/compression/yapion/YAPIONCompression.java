package yapi.compression.yapion;

import yapi.manager.yapion.YAPIONParser;
import yapi.manager.yapion.YAPIONType;
import yapi.manager.yapion.YAPIONVariable;
import yapi.manager.yapion.value.YAPIONArray;
import yapi.manager.yapion.value.YAPIONObject;
import yapi.manager.yapion.value.YAPIONValue;

import java.util.ArrayList;
import java.util.List;

public class YAPIONCompression {

    private YAPIONCompression() {
        throw new IllegalStateException();
    }

    public static String compress(YAPIONObject yapionObject) {
        List<YAPIONCount> yapionCounts = new ArrayList<>();
        countKeyToValue(yapionObject, yapionCounts);
        countKeyAndValue(yapionObject, yapionCounts);
        sort(yapionCounts);
        filter(yapionCounts);
        sort(yapionCounts);

        String s = yapionObject.toString();
        YAPIONKey yapionKey = new YAPIONKey();
        YAPIONObject compression = new YAPIONObject();

        s = s.replace("%", "\\%");
        s = s.replace("\\$", "%");
        for (int i = 0; i < yapionCounts.size(); i++) {
            YAPIONCount yapionCount = yapionCounts.get(i);
            String key = yapionKey.getKey();
            if (yapionCounts.size() <= 94) {
                key = key.substring(0, 1);
            }
            compression.add(new YAPIONVariable(key, new YAPIONValue(yapionCount.getString())));
            s = s.replace(yapionCount.getString(), ("\\$" + key));
        }

        if ((compression.toString() + s).length() > yapionObject.toString().length() + 2) {
            return "{}" + yapionObject.toString();
        }
        return compression.toString() + s;
    }

    private static void countKeyToValue(YAPIONObject yapionObject, List<YAPIONCount> yapionCounts) {
        List<String> strings = yapionObject.getKeys();
        for (String s : strings) {
            YAPIONType yapionType = yapionObject.getValue(s);
            if (yapionType instanceof YAPIONObject) {
                countKeyToValue((YAPIONObject)yapionType, yapionCounts);
            } else {
                count(new YAPIONCount(s + yapionType.toString()), yapionCounts);
            }
        }
    }

    private static void countKeyAndValue(YAPIONObject yapionObject, List<YAPIONCount> yapionCounts) {
        List<String> strings = yapionObject.getKeys();
        for (String s : strings) {
            count(new YAPIONCount(s), yapionCounts);
            String[] strs = s.split("\\.");
            if (strs.length > 1) {
                for (int i = 0; i < strs.length; i++) {
                    if (i != 0) {
                        count(new YAPIONCount("." + strs[i]), yapionCounts);
                    }
                    count(new YAPIONCount(strs[i]), yapionCounts);
                    count(new YAPIONCount(strs[i] + "."), yapionCounts);
                }
            }

            YAPIONType yapionType = yapionObject.getValue(s);
            if (yapionType instanceof YAPIONObject) {
                countKeyAndValue((YAPIONObject) yapionType, yapionCounts);
            } else if (yapionType instanceof YAPIONArray) {
                YAPIONArray yapionArray = ((YAPIONArray) yapionType);
                for (int i = 0; i < yapionArray.size(); i++) {
                    count(new YAPIONCount(yapionArray.get(i).toString()), yapionCounts);
                }
            } else if (yapionType instanceof YAPIONValue) {
                count(new YAPIONCount(((YAPIONValue) yapionType).getValue().toString()), yapionCounts);
            }
        }
    }

    private static void count(YAPIONCount yapionCount, List<YAPIONCount> yapionCounts) {
        int i = yapionCounts.indexOf(yapionCount);
        if (i == -1) {
            yapionCounts.add(yapionCount);
        } else {
            yapionCounts.get(i).increment();
        }
    }

    private static void sort(List<YAPIONCount> yapionCounts) {
        yapionCounts.sort((o1, o2) -> Long.compare(o1.getLoss(), o2.getLoss()) * -1);
    }

    private static void filter(List<YAPIONCount> yapionCounts) {
        for (int i = yapionCounts.size() - 1; i >= 0; i--) {
            if (yapionCounts.get(i).getLoss() <= 0) {
                yapionCounts.remove(i);
            }
        }
        List<YAPIONCount> counts = new ArrayList<>();
        while (counts.size() < 8836) {
            if (yapionCounts.isEmpty()) {
                break;
            }
            YAPIONCount yapionCount = yapionCounts.get(0);
            yapionCounts.remove(0);
            counts.add(yapionCount);
        }
        yapionCounts.clear();
        for (YAPIONCount yapionCount : counts) {
            yapionCounts.add(yapionCount);
        }
    }

    public static YAPIONObject decompress(String s) {
        YAPIONObject yapionObject = getDecompressionTable(s);
        List<String> keys = yapionObject.getKeys();
        s = s.substring(yapionObject.toString().length());
        for (String str : keys) {
            s = s.replace("\\$" + str, ((YAPIONValue)yapionObject.getValue(str)).getValue().toString());
        }
        s = s.replace("%", "\\$");
        s = s.replace("\\%", "%");
        return YAPIONParser.parse(s);
    }

    private static YAPIONObject getDecompressionTable(String s) {
        StringBuilder st = new StringBuilder();
        st.append('{');
        int bracket = 0;
        boolean escaped = false;
        for (int i = 0; i < s.length(); i++) {
            if (!escaped && s.charAt(i) == '\\') {
                escaped = true;
                continue;
            }

            if (!escaped && s.charAt(i) == '{') {
                bracket++;
            } else if (!escaped && s.charAt(i) == '}') {
                bracket--;
            } else {
                if (escaped) {
                    st.append('\\');
                }
                st.append(s.charAt(i));
            }
            escaped = false;

            if (bracket == 0) {
                break;
            }
        }

        st.append('}');
        return YAPIONParser.parse(st.toString());
    }

}