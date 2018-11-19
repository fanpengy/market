package com.market.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

public class PostGetUtil {

    private static final String server = "http://10.2.56.24:8080";

    /**
     * 使用post方式与服务器通讯
     *
     * @param content
     * @return
     */
    public static String SendPostRequest(String path, String content) {
        HttpURLConnection conn = null;
        try {
            String Strurl = server + path;
            URL url = new URL(Strurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            //conn.setRequestProperty("ser-Agent", "Fiddler");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setConnectTimeout(5 * 1000);
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                Log.i("PostGetUtil", "post请求成功");
                InputStream in = conn.getInputStream();
                int available = in.available();
                byte[] bytes = new byte[available];
                String backcontent = new String(bytes);
                backcontent = URLDecoder.decode(backcontent, "UTF-8");
                Log.i("PostGetUtil", backcontent);
                in.close();
                return backcontent;
            } else {
                Log.i("PostGetUtil", "post请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

    /**
     * 使用get方式与服务器通信
     *
     * @param params
     * @return
     */
    public static String SendGetRequest(String path, Map<String, String> params) {

        StringBuilder sb = new StringBuilder();
        if (params.size() > 0) {
            sb.append("?");
        }
        params.forEach((k, v) -> {
            sb.append("&").append(k).append("=").append(v);
        });

        HttpURLConnection conn = null;
        try {

            String Strurl = server + path + sb.toString().replaceFirst("&", "");
            URL url = new URL(Strurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                Log.i("PostGetUtil", "get请求成功");
                InputStream in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                String backcontent = URLDecoder.decode(response.toString(), "UTF-8");

                Log.i("PostGetUtil", backcontent);
                in.close();
                return backcontent;
            } else {
                Log.i("PostGetUtil", "get请求失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }
}
