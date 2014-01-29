package com.git.programmerr47.testhflbjcrhjggkth.model.pleer.api;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Api {
    static final String TAG = "Pleer.com.Api";
    
    public static final String BASE_URL = "http://pleer.com/browser-extension/";
    
    /*** utils methods***/
    private void checkError(JSONObject root, String url) throws JSONException,KException {
        if(!root.isNull("error")){
            JSONObject error = root.getJSONObject("error");
            int code=error.getInt("error_code");
            String message=error.getString("error_msg");
            KException e = new KException(code, message, url); 
            if (code==14) {
                e.captcha_img = error.optString("captcha_img");
                e.captcha_sid = error.optString("captcha_sid");
            }
            throw e;
        }
        if(!root.isNull("execute_errors")){
            JSONArray errors=root.getJSONArray("execute_errors");
            if(errors.length()==0)
                return;
            //only first error is processed if there are multiple
            JSONObject error=errors.getJSONObject(0);
            int code=error.getInt("error_code");
            String message=error.getString("error_msg");
            KException e = new KException(code, message, url); 
            if (code==14) {
                e.captcha_img = error.optString("captcha_img");
                e.captcha_sid = error.optString("captcha_sid");
            }
            throw e;
        }
    }
    
    private final static int MAX_TRIES = 3;
    private static JSONObject sendRequest(Params params) throws IOException, MalformedURLException, JSONException, KException {
        String url = BASE_URL + params.method_name+"?" + params.getParamsString();
        Log.i(TAG, "url="+url);
        String response="";
        for(int i = 1;i <= MAX_TRIES; ++i){
            try{
                if(i!=1)
                    Log.i(TAG, "try "+i);
                response = sendRequestInternal(url);
                break;
            }catch(javax.net.ssl.SSLException ex){
                processNetworkException(i, ex);
            }catch(java.net.SocketException ex){
                processNetworkException(i, ex);
            }
        }
        Log.i(TAG, "response="+response);
        JSONObject root = new JSONObject(response);
        //checkError(root, url);
        return root;
    }

    private static void processNetworkException(int i, IOException ex) throws IOException {
        ex.printStackTrace();
        if(i == MAX_TRIES)
            throw ex;
    }

    private static String sendRequestInternal(String url) throws IOException, MalformedURLException, WrongResponseCodeException {
        HttpURLConnection connection=null;
        try{
            connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setUseCaches(false);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            int code=connection.getResponseCode();
            Log.i(TAG, "code="+code);
            //It may happen due to keep-alive problem http://stackoverflow.com/questions/1440957/httpurlconnection-getresponsecode-returns-1-on-second-invocation
            if (code==-1)
                throw new WrongResponseCodeException("Network error");
            //может стоит проверить на код 200
            //on error can also read error stream from connection.
            InputStream is = new BufferedInputStream(connection.getInputStream(), 8192);
            String response=Utils.convertStreamToString(is);
            return response;
        }
        finally{
            if(connection!=null)
                connection.disconnect();
        }
    }
    
    public static ArrayList<Audio> searchAudio(String q, int limit, int page) throws MalformedURLException, IOException, JSONException, KException{
        Params params = new Params("search");
        params.put("q", q);
        params.put("limit", limit);
        params.put("page", page);
        JSONObject root = sendRequest(params);
        JSONArray array = root.optJSONArray("tracks");
        return parseAudioList(array, 0);
    }
    
    private static ArrayList<Audio> parseAudioList(JSONArray array, int type_array) //type_array must be 0 or 1
            throws JSONException {
        ArrayList<Audio> audios = new ArrayList<Audio>();
        Log.i("Api", "array == null = " + (array == null));
        if (array != null) {
            for(int i = type_array; i < array.length(); ++i) { //get(0) is integer, it is audio count
                JSONObject o = (JSONObject)array.get(i);
                audios.add(Audio.parse(o));
            }
        }
        return audios;
    }
    
    public static String unescape(String text){
        if(text==null)
            return null;
        return text.replace("&amp;", "&").replace("&quot;", "\"").replace("<br>", "\n").replace("&gt;", ">").replace("&lt;", "<")
        .replace("&#39;", "'").replace("<br/>", "\n").replace("&ndash;","-").replace("&#33;", "!").trim();
        //возможно тут могут быть любые коды после &#, например были: 092 - backslash \
    }
    
}