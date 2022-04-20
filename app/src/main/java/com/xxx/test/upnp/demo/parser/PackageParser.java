package com.xxx.test.upnp.demo.parser;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class PackageParser {
    public static final String TAG = "PackageParser";
    public static String getLocation(String s){
//        Log.i(TAG,"getLocation s:" + s);
        BufferedReader br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(s.getBytes(Charset.forName("utf8"))), Charset.forName("utf8")));
        String line = null;
        try {
            while ((line = br.readLine()) != null){
//                Log.i(TAG,"getLocation line:" + line);
                if(line.contains("LOCATION") && line.contains("http")){
                    line = line.substring(line.indexOf("http"));
                    break;
                }
            }
        }catch (Exception e){

        }
//        Log.i(TAG,"getLocation return:" + line);
        return line;
    }
}
