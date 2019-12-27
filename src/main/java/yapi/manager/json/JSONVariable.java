package yapi.manager.json;

public class JSONVariable {

    private String name = "";
    private JSONType jsonType = null;

    public JSONVariable(String name, JSONType jsonType) {
        this.name = name;
        this.jsonType = jsonType;
    }

    public String getName() {
        return name;
    }

    public JSONType getJsonType() {
        return jsonType;
    }

    @Override
    public String toString() {
        return "\"" + name + "\"" + ": " + jsonType.toString();
    }
}
