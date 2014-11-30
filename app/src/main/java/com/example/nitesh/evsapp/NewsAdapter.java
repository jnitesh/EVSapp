package com.example.nitesh.evsapp;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Created by nitesh on 11/11/14.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{

    JSONArray jsonArray = null;
    Typeface typeface;
    public NewsAdapter(JSONObject jsonObject){
        if (jsonObject != null){
            this.jsonArray = (JSONArray)jsonObject.get("articles");
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_card_layout,viewGroup,false);
        typeface = Typeface.createFromAsset(viewGroup.getContext().getAssets(),"Roboto-Regular.ttf");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        if (jsonArray != null){
            JSONObject article = (JSONObject)jsonArray.get(i);
            String contentString = (String)article.get("summary");
            String titleString = (String)article.get("title");
            String timeString = (String)article.get("publish_date");
            viewHolder.content.setText(contentString.trim());
            viewHolder.title.setText(titleString);
            viewHolder.title.setTypeface(typeface);
            viewHolder.time.setText(timeString.trim());

        }
    }


    @Override
    public int getItemCount() {
        return jsonArray == null ? 10 : jsonArray.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView content;
        public TextView title;
        public TextView time;
        public ViewHolder(View itemView) {
            super(itemView);
            content = (TextView)itemView.findViewById(R.id.news_content);
            title = (TextView)itemView.findViewById(R.id.news_header);
            time = (TextView)itemView.findViewById(R.id.news_time);
        }
    }
}