package com.example.android.newsapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

import android.widget.ProgressBar;



import com.example.android.newsapp.data.Contract;
import com.example.android.newsapp.data.DBHelper;
import com.example.android.newsapp.data.DatabaseUtils;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>,
        NewsAdapter.ItemClickListener{

    static final String TAG = "mainactivity";
    private ProgressBar progress;
    private RecyclerView rv;

    private NewsAdapter newsAdapter;
    private Cursor cursor;
    private SQLiteDatabase db;

    private static final int NEWS_LOADER = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar)findViewById(R.id.progressBar);

        rv = (RecyclerView)findViewById(R.id.recycler_view);

        rv.setLayoutManager(new LinearLayoutManager(this));

        //checks to see if app has been installed before if not it begins the first run of the app
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean first = prefs.getBoolean("first", true);

        //then it gets data from the network
        if(first){
            load();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("first", false);
            editor.commit();

        }
        else{//else it grabs info from db
            db = new DBHelper(MainActivity.this).getReadableDatabase();
            cursor = DatabaseUtils.getAll(db);
            newsAdapter = new NewsAdapter(cursor, this);
            rv.setAdapter(newsAdapter);
        }


    //sets up schedule to refresh after every minute
    ScheduleUtilities.scheduleRefresh(this);

    }

    //gets data from database
    @Override
    protected void onStart() {
        super.onStart();
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        newsAdapter = new NewsAdapter(cursor, this);
        rv.setAdapter(newsAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        db.close();
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if(itemNumber == R.id.search){
            load();
        }
        return true;

    }

    //was added to replace AsyncTask and handles clicks on the refresh menu item
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                RefreshTasks.refreshArticles(MainActivity.this);
                return null;
            }


        };
    }


    //method used when the Loader is completed
    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progress.setVisibility(View.GONE);
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);

        newsAdapter = new NewsAdapter(cursor, this);
        rv.setAdapter(newsAdapter);

        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URL));

        Log.d(TAG, String.format("Url %s", url));
        //calls method to open webpage
        openWebPage(url);
    }
    //after the specific article has been clicked this method is called to open the a webpage for the specific article
    public void openWebPage(String url){
        Uri webpage = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(webpage);

        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);

        }

    }

    private void load(){
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWS_LOADER, null, this).forceLoad();
    }

}
