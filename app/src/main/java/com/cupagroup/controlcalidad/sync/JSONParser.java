package com.cupagroup.controlcalidad.sync;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Entity;

public class JSONParser {
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    public JSONObject makeHttpRequest(String url, String method, List<NameValuePair> params)
    {
        try {
            if (method.equals("POST")){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                httpPost.setEntity(new UrlEncodedFormEntity(params, "utf-8"));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                //Segmento que se usa para control y desarrollo
                //String message = EntityUtils.toString(httpResponse.getEntity());
                //Log.i("Http Response", "Context: "+ message);

            }else if (method.equals("GET")){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params,"utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }
        } catch (IOException e){
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, StandardCharsets.ISO_8859_1),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            is.close();
            json = sb.toString();
        }catch (Exception e){
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            jObj = new JSONObject(json);
        }catch (JSONException e){
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        return jObj;
    }
}
