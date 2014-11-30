package com.example.nitesh.evsapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import engine.*;

/**
 * Created by nitesh on 19/9/14.
 */
public class UploadArticles extends Activity {
    public static final String UPLOAD_PREF = "UPLOAD_PREFER";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.add_new_article);
        LeanEngine.init(this, "http://lean-engine.appspot.com");
        preferences = getSharedPreferences(UPLOAD_PREF, Context.MODE_PRIVATE);
        if (!preferences.contains("finalCheck")){
            LeanEntity leanEntity = LeanEntity.init("MainCounter");
            leanEntity.put("Value","1");
            leanEntity.saveInBackground(new NetworkCallback<Long>() {
                @Override
                public void onResult(Long... result) {
                    Toast.makeText(UploadArticles.this,"Initialized",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("finalCheck",true);
                    editor.apply();
                }

                @Override
                public void onFailure(LeanError error) {
                    //
                }
            });
        }
        final EditText editHeading = (EditText)findViewById(R.id.edit_heading);
        final EditText editContent = (EditText)findViewById(R.id.edit_content);
        editContent.setHint("Content Here");
        editHeading.setHint("Enter Title Here");
        Button button = (Button)findViewById(R.id.final_upload);
        button.setText("Click To Upload");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LeanQuery leanQuery = new LeanQuery("MainCounter");
                System.out.println(leanQuery);
                leanQuery.fetchInBackground(new NetworkCallback<LeanEntity>() {
                    @Override
                    public void onResult(final LeanEntity... result) {

                        final String value = result[0].getString("Value");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String tobemade = "a" + value;
                                    LeanEntity newEntity = LeanEntity.init(tobemade);
                                    newEntity.put("heading", editHeading.getText().toString());
                                    newEntity.put("content", editContent.getText().toString());
                                    newEntity.saveInBackground(new NetworkCallback<Long>() {
                                        @Override
                                        public void onResult(Long... result) {
                                            Toast.makeText(UploadArticles.this, "Saved", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(LeanError error) {

                                        }
                                    });
                                    LeanEntity.delete("MainCounter", result[0].getId());
                                    LeanEntity entity = LeanEntity.init("MainCounter");
                                    entity.put("Value", String.valueOf(Integer.parseInt(value) + 1));
                                    entity.save();
                                    UploadArticles.this.finish();
                                } catch (LeanException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(LeanError error) {

                    }
                });
            }
        });
    }

}
