package Utilities;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by 461967 on 11/23/2015.
 */
public class JSONParser {

    public static JSONObject ToJsonObject(String jsonString) throws Exception{
        Object json = new JSONTokener(jsonString).nextValue();
        if(json instanceof JSONObject){
            return (JSONObject)json;
        }else{
            return null;
        }
    }

    private static JSONObject findJSONObjectByObjectName(String jsonString, String findKey) throws JSONException {
        Object json = new JSONTokener(jsonString).nextValue();
        if (json instanceof JSONObject) {
            JSONObject jobj = (JSONObject) json;
            Iterator<String> keys = jobj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String val = null;
                try {
                    if (jobj.get(key) instanceof JSONObject) {
//	            		System.out.println("BEGIN OBJECT.");
//	            		System.out.println("key : "+ key + " IS OBJECT.");

                        if (key.equals(findKey)) {
                            return (JSONObject) jobj.get(key);
                        }
                        JSONObject j = findJSONObjectByObjectName(jobj.get(key).toString(), findKey);
                        if (j != null) {
                            return j;
                        }
//	            		System.out.println("END OBJECT.");
                    } else if (jobj.get(key) instanceof JSONArray) {
//	            		System.out.println("BEGIN ARRAY.");
//	            		System.out.println("key : "+ key + " IS ARRAY");
                        JSONArray ja = (JSONArray) jobj.get(key);
                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject j = findJSONObjectByObjectName(ja.getJSONObject(i).toString(), findKey);
                            if (j != null) {
                                return j;
                            }
                        }
//	            		System.out.println("END ARRAY.");
                    } else if (jobj.get(key) instanceof String) {
//	            		System.out.println("key : "+ key + " IS STRING.");
                    } else if (jobj.get(key).getClass().isAssignableFrom(Integer.class)) {
//	            		System.out.println("key : "+ key + " IS INT.");
                    } else if (jobj.get(key).getClass().isAssignableFrom(Boolean.class)) {
//	            		System.out.println("key : "+ key + " IS BOOLEAN");
                    }

                } catch (Exception e) {
                    try {
                        val = jobj.getString(key);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } else if (json instanceof JSONArray) {
//	    	System.out.println("Is Array");
        }
        return null;
    }

    public static String findJsonObject(String jsonString, String findKey) throws Exception{
        Object json = new JSONTokener(jsonString).nextValue();
        if(json instanceof JSONObject){
            JSONObject jobj = (JSONObject) json;
            Iterator<String> keys = jobj.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                String val = null;
                try{
                    if(jobj.get(key) instanceof JSONObject){
                        if(key.equals(findKey)){
                            return jobj.get(key).toString();
                        }
                        String j = findJsonObject(jobj.get(key).toString(),findKey);
                        if(j!=null) {
                            return j;
                        }
                    }else if(jobj.get(key) instanceof JSONArray){
                        JSONArray ja = (JSONArray) jobj.get(key);
                        for (int i = 0; i < ja.length(); i++) {
                            String j = findJsonObject(ja.getJSONObject(i).toString(), findKey);
                            if (j != null) {
                                return j;
                            }
                        }
                    }else if (jobj.get(key) instanceof String) {
//	            		System.out.println("key : "+ key + " IS STRING.");
                        if(key.equals(findKey)){
                            return jobj.get(key).toString();
                        }
                    }
                } catch (Exception e) {
                    try {
                        val = jobj.getString(key);
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static void CompareToBenchmark(String response, String benchmark, ArrayList<String> ignoreKeys, boolean keysOnly) throws Exception{
        JSONObject json = (JSONObject) new JSONTokener(response).nextValue();
        JSONObject benchmarkJSON = (JSONObject) new JSONTokener(benchmark).nextValue();
        String error = CompareJSONObjects(json,benchmarkJSON,ignoreKeys, keysOnly);
        if(!error.equals("")){
            Assert.fail(error);
        }
    }

    private static String CompareJSONObjects(Object response, Object benchmark, ArrayList<String> ignoreKeys, boolean keysOnly){
        String error = "";
        if(response instanceof JSONObject){
            JSONObject respJSON = (JSONObject) response;
            JSONObject benchJSON = (JSONObject) benchmark;
            Iterator<String> responseKeys = respJSON.keys();
            while(responseKeys.hasNext()){
                String key = responseKeys.next();
                String value = null;
                if(!ignoreKeys.contains(key)) {
                    if (benchJSON.has(key)) {
                        if (respJSON.get(key) instanceof JSONObject) {
                            error = error + CompareJSONObjects((JSONObject) respJSON.get(key), (JSONObject) benchJSON.get(key), ignoreKeys, keysOnly);
                        } else if (respJSON.get(key) instanceof String) {
                            if(!keysOnly) {
                                if (!respJSON.get(key).equals(benchJSON.get(key))) {
                                    error = error + "Response JSON key " + key + ":" + respJSON.get(key)
                                            + " does not match with value in benchmark JSON "
                                            + key + ":" + benchJSON.get(key) + "\n";
                                }
                            }
                        } else if (respJSON.get(key) instanceof JSONArray) {
                            JSONArray responseArray = respJSON.getJSONArray(key);
                            JSONArray benchmarkArray = benchJSON.getJSONArray(key);
                            for (int i = 0; i < responseArray.length(); i++) {
                                error = error + CompareJSONObjects(responseArray.get(i), benchmarkArray.get(i), ignoreKeys, keysOnly);
                            }
                        }
                    } else {
                        error = error + key + " does not exist in benchmark JSON" + "\n";
                    }
                }
            }
        } else if (response instanceof JSONArray){
            JSONArray respJSON = (JSONArray) response;
            JSONArray benchJSON = (JSONArray) benchmark;
            for(int i = 0; i < respJSON.length(); i++){
                error = error + CompareJSONObjects(respJSON.get(i), benchJSON.get(i), ignoreKeys, keysOnly);
            }
        }
        return error;
    }
}
