package com.example.android.newsapp.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static com.example.android.newsapp.data.Contract.TABLE_ARTICLES.*;
/**
 * Created by Andres on 7/26/17.
 */

public class DatabaseUtils {

    //gets all the data from the database
    public static Cursor getAll(SQLiteDatabase db) {
        Cursor cursor = db.query(
                Contract.TABLE_ARTICLES.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED_DATE + " DESC"
        );
        return cursor;
    }
    //to add into the database after it was organized from the json
    public static void bulkInsert(SQLiteDatabase db, ArrayList<NewsItem> articles) {

        db.beginTransaction();
        try {
            for (NewsItem a : articles) {
                ContentValues cv = new ContentValues();
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_AUTHOR, a.getAuthor());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_TITLE, a.getTitle());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_DESCRIPTION, a.getDescription());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_URL, a.getUrl());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_URL_TO_IMAGE, a.getUrlToImage());
                cv.put(Contract.TABLE_ARTICLES.COLUMN_NAME_PUBLISHED_DATE, a.getPublishedAt());
                db.insert(Contract.TABLE_ARTICLES.TABLE_NAME, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public static void deleteAll(SQLiteDatabase db) {
        db.delete(TABLE_NAME, null, null);
    }

}