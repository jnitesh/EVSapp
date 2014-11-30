package com.example.nitesh.evsapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by nitesh on 9/11/14.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{

    private ArrayList<String> contentArray;
    private ArrayList<String> headerArray;
    private Typeface typeface;

    public ContactAdapter(ArrayList<String> headerArray,ArrayList<String> contentArray,Typeface typeface){
        this.headerArray = headerArray;
        this.contentArray = contentArray;
        this.typeface = typeface;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.cardlayout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {
        contactViewHolder.vName.setText(headerArray.get(i));
        contactViewHolder.vName.setBackgroundColor(Color.parseColor("#ffac534d"));
        contactViewHolder.vName.setTypeface(typeface);
        contactViewHolder.vContent.setText(contentArray.get(i));
    }

    @Override
    public int getItemCount() {
        return headerArray.size();
    }



    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vContent;
        public ContactViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.tip_header);
            vContent = (TextView)v.findViewById(R.id.tip_content);
        }
    }
}
