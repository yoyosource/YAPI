package yapi.math.calculator.normal;

/**
 * The type Token.
 */
public class Token {

    private String key = "";
    private Object val;

    /**
     * Instantiates a new Token.
     *
     * @param key   the key
     * @param value the value
     */
    public Token(String key, Object value) {
        this.key = key;
        this.val = value;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets val.
     *
     * @return the val
     */
    public Object getVal() {
        return val;
    }

    /**
     * Is type boolean.
     *
     * @return the boolean
     */
    public boolean isType() {
        return key.equals("str") || key.equals("num") || key.equals("int") || key.equals("bol") || key.equals("chr") || key.equals("lon");
    }

    /**
     * Compact Representation of this Token.
     *
     * @return the String
     */
    public String compact() {
        return "<" + key + ">" + val;
    }


    @Override
    public String toString() {
        return "Token{" +
                "key='" + key + '\'' +
                ", val='" + val + "'" +
                '}';
    }

}

