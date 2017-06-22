package com.example.android.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;

import android.widget.ProgressBar;
import android.widget.TextView;
import java.net.URL;



public class MainActivity extends AppCompatActivity {

    private TextView jsonResult;
    private TextView searchUrl;

    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        searchUrl = (TextView)findViewById(R.id.url_Query);
        jsonResult = (TextView)findViewById(R.id.displayJSON);

        progress = (ProgressBar)findViewById(R.id.progressBar);

    }

    private void start(){
        String source = "source";

        String a = source.toString();
        URL aUrl = NetworkUtils.makeUrl(a);
        searchUrl.setText(aUrl.toString());

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
    class NetworkTask extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(URL... params) {
            URL url = params[0];
            String result = null;

            try {
                result = NetworkUtils.getResponseFromHttpUrl(url);
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;

        }

        @Override
        protected void onPostExecute(String result) {
            if(result != null && !result.equals("")) {
                jsonResult.setText(result);
            }
            progress.setVisibility(View.GONE);

        }
    }



}
