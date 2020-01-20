package yapi.string;

public class StringMatching {

    /**
     *
     * @param s
     * @param c
     * @return
     */
    public static boolean contains(String s, char c) {
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param s
     * @param c
     * @return
     */
    public static int occurrences(String s, char c) {
        char[] chars = s.toCharArray();
        int count = 0;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == c) {
                count++;
            }
        }
        return count;
    }

    /**
     *
     * @param s
     * @param chars
     * @return
     */
    public static boolean contains(String s, char[] chars) {
        int index = 0;
        char[] input = s.toCharArray();

        for (int i = 0; i < input.length; i++) {
            if (input[i] == chars[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == chars.length) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param s
     * @param chars
     * @return
     */
    public static int occurrences(String s, char[] chars) {
        int index = 0;
        int count = 0;
        char[] input = s.toCharArray();

        for (int i = 0; i < input.length; i++) {
            if (input[i] == chars[index]) {
                index++;
            } else {
                index = 0;
            }

            if (index == chars.length) {
                count++;
                index = 0;
            }
        }

        return count;
    }

}
