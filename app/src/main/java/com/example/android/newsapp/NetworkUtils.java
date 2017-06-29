package com.example.android.newsapp;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;



/**
 * Created by Andres on 6/17/17.
 */

public class NetworkUtils {

    private static final String BASE_URL = "https://newsapi.org/v1/articles";

    private static final String SOURCE = "the-next-web";

    private static final String SORT_BY = "sortBy";
    private static final String sortBy = "latest";



    private final static String APIKEY = "apiKey";
    //---------------------------------------------Please add apiKey!!!!!!------------------------------------------------------------
    final static String apiKey = "";



    public static URL makeUrl (String searchQuery){
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(searchQuery, SOURCE)
                .appendQueryParameter(SORT_BY, sortBy)
                .appendQueryParameter(APIKEY, apiKey).build();


        URL url = null;
        try{

            url = new URL(uri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try{

            InputStream input = urlConnection.getInputStream();

            Scanner sc = new Scanner(input);
            sc.useDelimiter("\\A");

            boolean hasInput = sc.hasNext();

            if(hasInput) {
                return sc.next();
            }else {
                return null;
            }
        }finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<NewsItem> parseJSON(String json) throws JSONException {
        ArrayList<NewsItem> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray articles = main.getJSONArray("articles");

        for(int i = 0; i < articles.length(); i++){
            JSONObject article = articles.getJSONObject(i);
            String title = article.getString("title");
            String description = article.getString("description");
            String url = article.getString("url");

            String publishedAt = article.getString("publishedAt");


            NewsItem report = new NewsItem(title, description, url, publishedAt);
            result.add(report);
        }
        return result;
    }
}
