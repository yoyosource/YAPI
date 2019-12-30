package yapi.manager.yapion.value;

import yapi.manager.yapion.YAPIONType;
import yapi.manager.yapion.YAPIONVariable;

import java.util.ArrayList;
import java.util.List;

public class YAPIONObject extends YAPIONType {

    private List<YAPIONVariable> variables = new ArrayList<>();

    public YAPIONObject() {

    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();
        for (YAPIONVariable yapionVariable : variables) {
            keys.add(yapionVariable.getName());
        }
        return keys;
    }

    public YAPIONType getValue(String key) {
        for (YAPIONVariable yapionVariable : variables) {
            if (yapionVariable.getName().equals(key)) {
                return yapionVariable.getYapionType();
            }
        }
        return null;
    }

    public void add(YAPIONVariable yapionVariable) {
        variables.add(yapionVariable);
    }

    @Override
    public String getType() {
        return "YAPIONObject";
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        for (YAPIONVariable yapionVariable : variables) {
            st.append(yapionVariable.toString());
        }
        return st.append("}").toString();
    }

    public String toHierarchyString() {
        return toHierarchyString(0);
    }

    public String toHierarchyString(int index) {
        StringBuilder st = new StringBuilder();
        st.append("{\n");
        for (YAPIONVariable yapionVariable : variables) {
            st.append(yapionVariable.toHierarchyString(index == 0 ? index + 1 : index)).append('\n');
        }
        if (index > 1) {
          st.append(" ".repeat(2 * (index - 1)));
        }
        st.append("}");
        return st.toString();
    }
}
