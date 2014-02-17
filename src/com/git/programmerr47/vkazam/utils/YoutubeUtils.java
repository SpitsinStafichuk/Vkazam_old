package com.git.programmerr47.vkazam.utils;

import android.util.Log;
import com.git.programmerr47.vkazam.model.pleer.api.Utils;
import com.git.programmerr47.vkazam.model.pleer.api.WrongResponseCodeException;
import com.git.programmerr47.vkazam.model.pleer.api.WrongResponseCodeException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class YoutubeUtils {
    private static final String TAG = "Youtube";
    private static final int MAX_TRIES = 3;
    private static final String BASE_URL = "http://gdata.youtube.com/feeds/api/videos?q=";
    private static final String BASE_URL_END = "&max-results=1&v=2&alt=jsonc";

    private static String sendRequestInternal(String url) throws IOException {
        HttpURLConnection connection=null;
        try{
            connection = (HttpURLConnection)new URL(url.replaceAll(" ", "%20")).openConnection();
            connection.setConnectTimeout(30000);
            connection.setReadTimeout(30000);
            connection.setUseCaches(false);
            connection.setDoOutput(false);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            int code=connection.getResponseCode();
            Log.i(TAG, "code=" + code);
            //It may happen due to keep-alive problem http://stackoverflow.com/questions/1440957/httpurlconnection-getresponsecode-returns-1-on-second-invocation
            if (code==-1)
                throw new WrongResponseCodeException("Network error");
            if (code==400)
                return null;
            //может стоит проверить на код 200
            //on error can also read error stream from connection.
            InputStream is = new BufferedInputStream(connection.getInputStream());
            String response= Utils.convertStreamToString(is);
            return response;
        }
        finally{
            if(connection!=null)
                connection.disconnect();
        }
    }

    private static void processNetworkException(int i, IOException ex) throws IOException {
        ex.printStackTrace();
        if(i == MAX_TRIES)
            throw ex;
    }

    public static String sendRequest(String request) throws IOException {
        String url = BASE_URL + request + BASE_URL_END;
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
        Log.i(TAG, "response = "+response);
        return getUrlFromResponse(response);
    }

    public static String unescape(String text){
        if(text==null)
            return null;
        return text.replace("&amp;", "&").replace("&quot;", "\"").replace("<br>", "\n").replace("&gt;", ">").replace("&lt;", "<")
                .replace("&#39;", "'").replace("<br/>", "\n").replace("&ndash;","-").replace("&#33;", "!").trim();
        //возможно тут могут быть любые коды после &#, например были: 092 - backslash \
    }

    private static String getUrlFromResponse(String response) {
        if (response == null) {
            return null;
        }

        if (response.indexOf("player\":") != -1) {
            String playerSection = response.substring(response.indexOf("player\":"));
            if (playerSection.indexOf("default\":") != -1) {
                String defaultt = playerSection.substring(playerSection.indexOf("default\":"));
                if (defaultt.indexOf("http:") != -1) {
                    String url = defaultt.substring(defaultt.indexOf("http:"));
                    int endIndex = 0;
                    while ((endIndex < url.length()) && (url.charAt(endIndex) != '\"')) {
                        endIndex++;
                    }
                    if (url.charAt(endIndex) == '\"') {
                        url = url.substring(0, endIndex);
                        return url;
                    }
                }
            }
        }

        return null;
    }
}
