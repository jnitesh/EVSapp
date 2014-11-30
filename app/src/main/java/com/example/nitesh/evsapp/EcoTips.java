package com.example.nitesh.evsapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/*
 * Created by nitesh on 1/9/14.
 */
public class EcoTips extends Activity{

    private ArrayList<String> headingArray;
    private ArrayList<String> contentArray;
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.tips_layout);
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject)parser.parse(new InputStreamReader(getAssets().open("desktop.json")));
            JSONArray jsonHeaderArray = (JSONArray)jsonObject.get("headings");
            JSONArray jsonContentArray = (JSONArray)jsonObject.get("content");
            headingArray = new ArrayList<String>();
            contentArray = new ArrayList<String>();
            for (int i = 0;i < jsonHeaderArray.size();i++){
                headingArray.add((String)jsonHeaderArray.get(i));
                contentArray.add((String)jsonContentArray.get(i));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final RecyclerView recyclerView = (RecyclerView)findViewById(R.id.cardList);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"Roboto-Regular.ttf");
        ContactAdapter adapter = new ContactAdapter(headingArray,contentArray,typeface);
        recyclerView.setAdapter(adapter);
    }

}
