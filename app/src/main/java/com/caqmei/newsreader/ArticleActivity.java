package com.caqmei.newsreader;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        Intent i = getIntent();
        int id = i.getIntExtra("locationInfo", -1);

        Cursor c = MainActivity.myDatabase.rawQuery("SELECT * FROM news", null);
        //int idIndex = c.getColumnIndex("id");
        int titleIndex = c.getColumnIndex("title");
        Log.i("ID", Integer.toString(titleIndex));
        int urlIndex = c.getColumnIndex("url");
        Log.i("urlIndex", Integer.toString(urlIndex));

        int contentIndex = c.getColumnIndex("content");


            Log.i("URL", c.getString(titleIndex));
            Log.i("URL", c.getString(urlIndex));
            Log.i("URL", c.getString(contentIndex));

//
//        //Log.i("Content", c.getString(contentIndex));
//        Log.i("URL", c.getString(urlIndex));

//
//        int titleIndex = c.getColumnIndex("title");
//        int urlIndex = c.getColumnIndex("url");
//        int contentIndex = c.getColumnIndex("content");
//        c.moveToFirst();
//
//        while (c != null) {
//            Log.i("TITLE: ", c.getString(titleIndex));
//            Log.i("URL: ", c.getString(urlIndex));
//            //Log.i("Content: ", c.getString(contentIndex));
//
//            c.moveToNext();
//        }
//        webView.loadDataWithBaseURL(c.getString(urlIndex), c.getString(contentIndex), "text/html", "UTF-8", "");


    }


}
