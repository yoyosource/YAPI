package yapi.manager.json.value;

import yapi.manager.json.JSONType;

import java.util.ArrayList;
import java.util.List;

public class JSONArray extends JSONType {

    private List<JSONType> array = new ArrayList<>();

    public int size() {
        return array.size();
    }

    public JSONType get(int index) {
        return array.get(index);
    }

    public void add(JSONType jsonType) {
        array.add(jsonType);
    }

    @Override
    public String getType() {
        return "JSONArray";
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("[");
        boolean b = false;
        for (JSONType jsonType : array) {
            if (b) {
                st.append(", ");
            }
            st.append(jsonType.toString());
            b = true;
        }
        st.append("]");
        return st.toString();
    }

}
