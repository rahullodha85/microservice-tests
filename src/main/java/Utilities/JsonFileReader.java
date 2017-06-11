package Utilities;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by 461967 on 12/15/2015.
 */
public class JsonFileReader {

    public static String Read(String fileName) throws Exception{
        BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
        String line,body=null;
        while((line=file.readLine())!=null){
            if(body==null){
                body=line;
            }else {
                body = body + line;
            }
        }

        return body;
    }
}
