package yapi.manager.json.value;

import yapi.manager.json.JSONType;
import yapi.manager.json.JSONVariable;

import java.util.ArrayList;
import java.util.List;

public class JSONObject extends JSONType {

    private List<JSONVariable> jsonVariables = new ArrayList<>();

    public JSONObject() {

    }

    public void add(JSONVariable jsonVariable) {
        jsonVariables.add(jsonVariable);
    }

    @Override
    public String getType() {
        return "JSONObject";
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        st.append("{");
        int i = 0;
        for (JSONVariable jsonVariable : jsonVariables) {
            st.append(jsonVariable.toString());
            i++;
            if (i != jsonVariables.size()) {
                st.append(",");
            }
        }
        st.append("}");
        return st.toString();
    }

}
