package com.example.android.newsapp.data;

import android.provider.BaseColumns;

/**
 * Created by Andres on 7/26/17.
 */
public class Contract {

    public static class TABLE_ARTICLES implements BaseColumns {
        public static final String TABLE_NAME = "articles";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_URL_TO_IMAGE = "urlToImage";
        public static final String COLUMN_NAME_PUBLISHED_DATE = "published_date";

    }

}
