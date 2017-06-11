package cucumber.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonFileReader {

    public static String ReadFileToString(String fileName) throws Exception {
        System.out.print(fileName);
        InputStream inputStream = JsonFileReader.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader file = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String body = "";
        while ((line = file.readLine()) != null) {
            body += line;
        }
        return body;
    }

    public static JsonObject ReadFileToJsonObject(String fileName) throws Exception {
        String jsonString = ReadFileToString(fileName);
        return new JsonParser().parse(jsonString).getAsJsonObject();
    }
}
