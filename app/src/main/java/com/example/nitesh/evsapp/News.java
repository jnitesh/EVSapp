package com.example.nitesh.evsapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.json.simple.JSONObject;

/*
 * Created by nitesh on 1/9/14.
*/
public class News extends Activity implements ResultReceiver{
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.newslayout);
        FetchNewsFeedZilla fetchNewsFeedZilla = new FetchNewsFeedZilla();
        fetchNewsFeedZilla.resultReceiver = this;
        fetchNewsFeedZilla.execute();
        recyclerView = (RecyclerView)findViewById(R.id.news_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        NewsAdapter adapter = new NewsAdapter(null);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void resultReceive(JSONObject jsonObject) {
        NewsAdapter adapter = new NewsAdapter(jsonObject);
        recyclerView.setAdapter(adapter);

    }


    public void uploadActivity(View view){
        Intent intent = new Intent(this,UploadArticles.class);
        startActivity(intent);
    }
}
