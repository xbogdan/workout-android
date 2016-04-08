package com.boamfa.workout.utils;

import android.util.Pair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by bogdan on 24/11/15.
 */
public class HttpCaller {

    private final String USER_AGENT = "Mozilla/5.0";

    private Boolean secure;

    public HttpCaller() {
        this.secure = false;
    }

    public HttpCaller(Boolean secure) {
        this.secure = secure;
    }

    // HTTP request
    public Pair<Integer, String> sendRequest(String endPoint, String requestType, HashMap<String, String> params, HashMap<String, String> headers) throws Exception {

        URL obj = new URL(endPoint);
        HttpURLConnection con;
        if (this.secure) {
            con = (HttpsURLConnection) obj.openConnection();
        } else {
            con = (HttpURLConnection) obj.openConnection();
        }

        // Set request headers
        con.setRequestMethod(requestType);
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Origin", endPoint);

        // Set custom request headers
        if (headers != null) {
            Iterator it = headers.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                con.setRequestProperty(pair.getKey().toString(), pair.getValue().toString());
                it.remove();
            }
        }

        if (requestType.equals("POST") || requestType.equals("PUT") || requestType.equals("PATCH")) {
            // Set request parameters
            String urlParameters = "";
            if (params != null) {
                Iterator it = params.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    it.remove(); // avoids a ConcurrentModificationException
                    urlParameters += pair.getKey().toString() + "=" + pair.getValue().toString() + "&";
                }
            }

            // Send request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
        }

        int responseCode = con.getResponseCode();

        if (responseCode/100 == 4) {
            // TODO Invalidate auth token HERE not in sync adapter
        }

        StringBuffer response;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            response = new StringBuffer();

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new Pair<Integer, String>(responseCode, response.toString());
        } catch (Exception e) {
            return new Pair<Integer, String>(responseCode, con.getResponseMessage().toString());
        }


    }
}
