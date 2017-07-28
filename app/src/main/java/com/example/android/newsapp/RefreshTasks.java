package com.example.android.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.newsapp.data.DBHelper;
import com.example.android.newsapp.data.DatabaseUtils;
import com.example.android.newsapp.data.NewsItem;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Andres on 7/26/17.
 */

public class RefreshTasks {

    public static final String ACTION_REFRESH = "refresh";

    //setting up my database with the information attained from json
    public static void refreshArticles(Context context){
        ArrayList<NewsItem> result = null;
        URL url = NetworkUtils.makeUrl();

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        try{
            DatabaseUtils.deleteAll(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);
            DatabaseUtils.bulkInsert(db, result);
        }catch (IOException e){
            e.printStackTrace();
        }catch (JSONException e){
            e.printStackTrace();
        }
        db.close();
    }
}
