package com.example.nitesh.evsapp;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

/*
 * Created by nitesh on 1/9/14.
*/
public class News extends Activity{
    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.newslayout);
        ListView listView = (ListView)findViewById(R.id.news_list);
        NewsListAdapter adapter = new NewsListAdapter(getApplicationContext());
        listView.setAdapter(adapter);
    }

    private class NewsListAdapter extends ArrayAdapter{

        Context context;
        public NewsListAdapter(Context context){
            super(context,R.layout.newslayout);
            this.context = context;
        }


        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.news_adapter,viewGroup,false);
            TextView textView = (TextView)v.findViewById(R.id.news_text);
            textView.setText("Some Random News");
            textView.setTextSize(30);
            Typeface typeface = Typeface.createFromAsset(getAssets(),"Blenda Script.otf");
            textView.setTypeface(typeface);
            ImageView imageView = (ImageView)v.findViewById(R.id.news_image);
            imageView.setImageResource(R.drawable.mainactivity);
            return v;
        }
    }
}
