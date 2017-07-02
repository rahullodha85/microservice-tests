package runner.Steps.Common;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.sun.org.apache.regexp.internal.RE;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class ScenarioBase {
    private static final Random RNG = new Random();
    protected static List<String> cookies;
    protected static Response httpResponse;
    protected static Map context;

    protected static Config config = ConfigFactory.load();
    public static String getServiceUrl() {
        return config.getString("testEnvironment");
    }

    public static String createCookieHeader() {
        String cookie = "";
        for (String item : cookies) {
            cookie += item + ";";
        }

        return cookie;
    }

    protected static String randomName(int length) {
        String characters = "abcdefghijklmnopq";
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = characters.charAt(RNG.nextInt(characters.length()));
        }
        return new String(text);
    }

}
