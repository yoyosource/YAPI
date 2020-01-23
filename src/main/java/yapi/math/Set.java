package yapi.math;

import yapi.exceptions.math.SetException;

public class Set {

    private String name = "";
    private String definition = "";

    public Set(String set) {
        if (!set.matches("[a-zA-Z]+=.+")) {
            throw new SetException("Set has no valid head.");
        }
        if (!set.matches("[a-zA-Z]+=\\{([a-z])\\|(\\1|[ENZQRC0-9<>=!,])+\\}")) {
            throw new SetException("Set has no valid body.");
        }
        name = set.substring(0, set.indexOf('='));
        definition = set.substring(set.indexOf('=') + 1);
        System.out.println(name);
        System.out.println(definition);
    }

    public static void main(String[] args) {
        new Set("F={n|2>n>0}");
    }

}
