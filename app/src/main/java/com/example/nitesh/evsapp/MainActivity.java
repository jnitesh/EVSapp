package com.example.nitesh.evsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import engine.*;

import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends Activity {
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LeanEngine.init(this, "http://lean-engine.appspot.com");

        setContentView(R.layout.activity_main);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "PoetsenOne-Regular.ttf");
        TextView news = (TextView) findViewById(R.id.news);
        news.setTypeface(typeface);
        TextView tips = (TextView) findViewById(R.id.tips);
        tips.setTypeface(typeface);
        TextView gmail = (TextView)findViewById(R.id.gmail);
        Typeface robot = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        gmail.setTypeface(robot);
        if (LeanAccount.isUserLoggedIn()){
            gmail.setText("logOut");
        }else {
            gmail.setText("logIn");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void newsActivity(View v) {
        Intent newsIntent = new Intent(this,News.class);
        startActivity(newsIntent);

    }

    public void ecoTipsActivity(View view) {
//        Intent intent = new Intent(this,EcoTips.class);
//        startActivity(intent);
        final LeanEntity leanEntity = LeanEntity.init("Articles2");
        leanEntity.put("1", "This is n one news");
        leanEntity.put("2", "This is no two news");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    long id = leanEntity.save();
                    LeanEntity.delete("Articles2",id);
                } catch (LeanException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void loginGmail(View view) {
        final TextView textView = (TextView)view;
        if (LeanAccount.isUserLoggedIn()) {
            LeanAccount.logoutInBackground(new NetworkCallback<Boolean>() {
                @Override
                public void onResult(Boolean... result) {
                    Toast.makeText(MainActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();
                    textView.setText("logIn");
                }

                @Override
                public void onFailure(LeanError error) {

                }
            });
        } else {
            Uri loginUri = LeanEngine.getGoogleLoginUri();

            LoginDialog fbDialog = new LoginDialog(MainActivity.this, loginUri.toString(), new LoginListener() {
                @Override
                public void onSuccess() {
                    // do your thing here
                    textView.setText("logOut");
                }

                @Override
                public void onCancel() {
                    // do your thing here
                }

                @Override
                public void onError(LeanError error) {
                    // do your thing here
                }
            });

            fbDialog.show();
        }
    }
}
