package Utilities;

/**
 * Created by 461967 on 12/15/2015.
 */
public class KeyValuePair {

    private String key = "";
    private String value = "";

    public KeyValuePair() {

    }

    public KeyValuePair(String k, String v) {
        key = k;
        value = v;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "Key [" + key + "] has Value of " + value;
    }
}
