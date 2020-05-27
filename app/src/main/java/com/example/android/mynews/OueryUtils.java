package com.example.android.mynews;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.mynews.MainActivity.LOG_TAG;

class QueryUtils {


    private QueryUtils() {
    }
    public static ArrayList<MyNews> extractFeatureFromJson(String newsJSON){
        if(TextUtils.isEmpty(newsJSON)){
            return null;
        }

        ArrayList<MyNews> newsArrayList = new ArrayList<>();

        try{
            JSONObject baseJsonObject = new JSONObject(newsJSON);
            JSONObject getResponse = baseJsonObject.getJSONObject("response");
            JSONArray getArray = getResponse.getJSONArray("results");

            for(int i =0;i<getArray.length();i++){
                JSONObject currentnews = getArray.getJSONObject(i);
                String Name = currentnews.getString("sectionName");
                String headline = currentnews.getString("webTitle");
                String date = currentnews.getString("webPublicationDate");
                String url = currentnews.getString("webUrl");

                JSONArray nameArray = currentnews.getJSONArray("tags");
                for (int j = 0 ; j<nameArray.length();j++){
                    JSONObject currentName = nameArray.getJSONObject(j);
                    String firstName = currentName.optString("firstName");
                    String lastName = currentName.optString("lastName");

                    MyNews news = new MyNews(Name, headline, date, url, firstName, lastName);
                    newsArrayList.add(news);
                }
            }

        }catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        return newsArrayList;
    }
    public static List<MyNews> fetchData(String requestUrl) {


        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<MyNews> news = extractFeatureFromJson(jsonResponse);

        return news;
    }
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
