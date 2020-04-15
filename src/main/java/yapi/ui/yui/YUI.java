package yapi.ui.yui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YUI {

    public static void main(String[] args) {
        YUI yui = new YUI(new File("/Users/jojo/IdeaProjects/YAPI/src/main/resources/Test.yui"));
        System.out.println(yui.configs);
    }

    private List<YUIConfig> configs = new ArrayList<>();

    public YUI(File config) {
        configs.add(new YUIConfig(config));
    }

}
