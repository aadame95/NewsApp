package com.example.android.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    static final String TAG = "mainactivity";


    private TextView notice;
    private ProgressBar progress;

    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notice = (TextView)findViewById(R.id.notice);

        progress = (ProgressBar)findViewById(R.id.progressBar);

        rv = (RecyclerView)findViewById(R.id.recycler_view);

        rv.setLayoutManager(new LinearLayoutManager(this));

    }

    private void start(){
        String source = "source";

        String a = source.toString();
        URL aUrl = NetworkUtils.makeUrl(a);
        // searchUrl.setText(aUrl.toString());

        new NetworkTask().execute(aUrl);

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
            start();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
    class NetworkTask extends AsyncTask<URL, Void, ArrayList<NewsItem>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
            notice.setVisibility(View.INVISIBLE);


        }

        @Override
        protected ArrayList<NewsItem> doInBackground(URL... params) {
            URL url = params[0];
            ArrayList<NewsItem> json = null;

            try {
                String result = NetworkUtils.getResponseFromHttpUrl(url);
                json = NetworkUtils.parseJSON(result);

            }catch (Exception e){
                e.printStackTrace();
            }
            return json;

        }

        @Override
        protected void onPostExecute(final ArrayList<NewsItem> result) {
            super.onPostExecute(result);
            progress.setVisibility(View.GONE);
            if (result != null) {
                NewsAdapter adapter = new NewsAdapter(result, new NewsAdapter.ItemClickListener() {
                    @Override
                    public void onItemClick(int clickedItemIndex) {
                        String url = result.get(clickedItemIndex).getUrl();
                        Log.d(TAG, String.format("Url %s", url));
                        openWebPage(url);

                    }
                });
                rv.setAdapter(adapter);

            }
        }

        public void openWebPage(String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

    }



}
