package com.varry.facebooksofteng;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Homepage extends AppCompatActivity {

    private Intent intent;
    private String jsonData;
    private JSONArray postList;
    private ArrayList<String> posts;
    private ArrayAdapter adapter;
    private ListView listView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        initComponents();
        generatePosts();
        attachAdapter();

}

    private void attachAdapter() {
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview,posts);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }

    private void generatePosts() {
        try {
            postList = new JSONArray(jsonData);
            for(int i = 0; i < postList.length(); i++) {
                if(postList.getJSONObject(i).toString().contains("story"))
                    posts.add(postList.getJSONObject(i).getString("story"));
                else
                    posts.add(postList.getJSONObject(i).getString("message"));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void initComponents() {
        intent = getIntent();
        jsonData = intent.getStringExtra("jsondata");
        posts = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listView);

    }
}
