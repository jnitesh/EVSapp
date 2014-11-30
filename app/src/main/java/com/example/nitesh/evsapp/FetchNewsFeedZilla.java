package com.example.nitesh.evsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by nitesh on 11/11/14.
 */
public class FetchNewsFeedZilla extends AsyncTask<Void,Void,JSONObject>{

    public static final String SCIENCELINK = "http://api.feedzilla.com/v1/categories/4/subcategories/100/articles.json";
    public ResultReceiver resultReceiver = null;

    private static InputStream getJsonStream() throws IOException{
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpResponse = httpClient.execute(new HttpGet(SCIENCELINK));
        return httpResponse.getEntity().getContent();
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        try {
            Log.d("receiver","called");
            JSONParser parser = new JSONParser();
            JSONObject mJson = (JSONObject)parser.parse(new InputStreamReader(getJsonStream()));
            return mJson;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onPostExecute(JSONObject jsonObject){
        resultReceiver.resultReceive(jsonObject);
    }
}
