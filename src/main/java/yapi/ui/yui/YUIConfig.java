package yapi.ui.yui;

import yapi.file.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YUIConfig {

    private String name;
    private List<String> lines = new ArrayList<>();

    YUIConfig(File config) {
        name = config.getName();
        String[] strings = FileUtils.fileContentAsString(config);
        for (String s : strings) {
            String t = s.trim();
            if (!t.isEmpty()) {
                lines.add(t);
            }
        }
    }

    @Override
    public String toString() {
        return "YUIConfig{" +
                "name='" + name + "'" +
                ", lines=" + lines +
                '}';
    }
}
