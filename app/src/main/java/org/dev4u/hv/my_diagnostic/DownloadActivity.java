package org.dev4u.hv.my_diagnostic;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import db.Database;
import db.LocalDatabase;
import utils.SearchUpdates;

public class DownloadActivity extends AppCompatActivity {

    private LocalDatabase localDatabase;
    private ImageView hearth;
    private AnimatedVectorDrawable hearthAnim;
    private Button btnDownload;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;
    private AnimatedVectorDrawable animDrawable;
    private CoordinatorLayout coordinatorLayout;
    private Boolean isUpdate;
    private int status;
    private boolean functionCalled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        findViewById(R.id.activity_download);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            isUpdate = false;
        } else {
            isUpdate = extras.getBoolean("IS_UPDATE",false);
        }
        //is first time
        preferences         = getSharedPreferences("Data", Context.MODE_PRIVATE);
        editorPreferences   = preferences.edit();
        status              = preferences.getInt("STATUS",0);
        //views
        btnDownload         = (Button) findViewById(R.id.btnDownload);
        coordinatorLayout   = (CoordinatorLayout) findViewById(R.id.activity_download);
        //prepare for download
        localDatabase = new LocalDatabase(this,btnDownload,coordinatorLayout);
        functionCalled=false;

        //animation
        hearth              = (ImageView)findViewById(R.id.pulse);
        hearthAnim          = ((AnimatedVectorDrawable) ((ImageView)findViewById(R.id.pulse)).getDrawable());
        animDrawable        = (AnimatedVectorDrawable) getDrawable(R.drawable.hearth_pulse_animation);
        hearth.setImageDrawable(animDrawable);
        animDrawable.start();
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

    }

    public void next(){
        //just one call
        if(!functionCalled){
            //TODO aqui llamo a las peticiones
            //new Download().execute();
            localDatabase.initDatabase();
            functionCalled=true;
        }
        if(localDatabase.isDownloadFinished()){
            if(status==0){
                startActivity(new Intent(this,UserDataActivity.class));
                this.finish();
            }else{
                if(isUpdate){
                    startActivity(new Intent(this,MainActivity.class));
                }
                this.finish();
            }
        }
    }

}
