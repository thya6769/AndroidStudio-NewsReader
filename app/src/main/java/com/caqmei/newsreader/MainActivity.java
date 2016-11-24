package com.caqmei.newsreader;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> newsTitle = new ArrayList<>();
    private ArrayList<String> newsURL = new ArrayList<>();
    private int i = 0;
    static SQLiteDatabase myDatabase;
    private ArrayAdapter arrayAdapter;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1 ){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < 10; i++) {
                    DownloadJSON task = new DownloadJSON();
                    task.execute("https://hacker-news.firebaseio.com/v0/item/"
                            + jsonArray.get(i).toString() + ".json?print=pretty");
                    //Log.i("WebContent:", split[i]);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    public class DownloadJSON extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1 ){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {

                JSONObject jsonObject = new JSONObject(result);
                newsTitle.add(jsonObject.getString("title"));
                newsURL.add(jsonObject.getString("url"));
                arrayAdapter.notifyDataSetChanged();

                //Log.i("i value:" , Integer.toString(i));

                if(i == 9) {
                    DownloadContent task = new DownloadContent();
                    task.execute(jsonObject.getString("url"));
                    Log.i("Reached here", jsonObject.getString("url"));
                } else {
                    i++;
                }
                //Log.i("TITLE: ", jsonObject.getString("title"));
                //Log.i("URL: ", jsonObject.getString("url"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DownloadContent extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data != -1){
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                Log.i("Reached here", "Yey");

                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("i value:" , Integer.toString(i));
            String sql = "INSERT INTO news (title, url, content) VALUES (?, ?, ?)";
            SQLiteStatement statement = myDatabase.compileStatement(sql);
            statement.bindString(1, newsTitle.get(i));
            statement.bindString(2, newsURL.get(i));
            statement.bindString(3, result);
            statement.execute();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView myListView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, newsTitle);
        myListView.setAdapter(arrayAdapter);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                i.putExtra("locationInfo", position);
                startActivity(i);
            }
        });

        myDatabase = this.openOrCreateDatabase("News", MODE_PRIVATE, null);
        myDatabase.execSQL("CREATE TABLE IF NOT EXISTS news (title VARCHAR, url VARCHAR, content VARCHAR, id INTEGER PRIMARY KEY)");
        myDatabase.execSQL("DELETE FROM news");
        DownloadTask task = new DownloadTask();
        task.execute("https://hacker-news.firebaseio.com/v0/topstories.json?print=pretty");
    }

}
