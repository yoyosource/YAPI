package yapi.regex;

import java.util.Arrays;
import java.util.List;

public class YAPIRegexBuilder {

    private static List<Character> escapeableCharacters = Arrays.asList('(', ')', '+', '*', '?', '{', '}', '|', '[', ']', '.', '^', '$', '#');

    public YAPIRegexBuilder(String regexString) {
        char[] regex = regexString.toCharArray();
        System.out.println(regex);

        StringBuilder st = new StringBuilder();

        boolean escaped = false;
        for (int i = 0; i < regex.length; i++) {
            if (regex[i] == '\\' && i < regex.length - 1 && escapeableCharacters.contains(regex[i + 1])) {
                escaped = true;
                continue;
            }

            if (escaped || (!escapeableCharacters.contains(regex[i]) && !escaped)) {
                st.append(regex[i]);
            } else {
                System.out.println(st + " " + regex[i]);
                st = new StringBuilder();
            }

            //System.out.println(regex[i] + " " + escaped);

            escaped = false;
        }
    }

    /*
    ()
    {}
    []
    */

    public static void main(String[] args) {
        YAPIRegexBuilder yapiRegexBuilder = new YAPIRegexBuilder("a\\bc\\(d\\)e+f*g?h{0, 10}([0-9]+|[a-f]+).+");
    }

}
