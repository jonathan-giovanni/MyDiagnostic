package org.dev4u.hv.my_diagnostic;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.appyvet.rangebar.RangeBar;

import utils.DiseaseUtilitesSingleton;
import utils.SearchUpdates;

public class SettingsActivity extends AppCompatActivity {

    private ToggleButton toggleSettings;
    private FloatingActionButton btnSaveSettings;
    private RangeBar barDistance;
    private RangeBar barPercentage;
    private TextView lblDistance;
    private TextView lblPercentage;
    private Button btnUpdate;

    private SharedPreferences.Editor editSavedData;
    private SharedPreferences savedData;
    private int percentage;
    private float distance;

    SearchUpdates searchUpdates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        savedData       = getSharedPreferences("Data", Context.MODE_PRIVATE);
        editSavedData   = savedData.edit();

        distance        = savedData.getFloat("DISTANCE",3);
        percentage      = savedData.getInt("PERCENTAGE",20);

        searchUpdates   = new SearchUpdates(this,false);


        toggleSettings  = (ToggleButton) findViewById(R.id.toggleUpdates);
        lblDistance     = (TextView) findViewById(R.id.lblSettingRadius);
        lblPercentage   = (TextView) findViewById(R.id.lblSettingPercentage);
        barDistance     = (RangeBar) findViewById(R.id.radiusRangeBar);
        barPercentage   = (RangeBar) findViewById(R.id.searchRangeBarMin);
        btnUpdate       = (Button)   findViewById(R.id.btnSearchUpdateNow);
        btnSaveSettings = (FloatingActionButton) findViewById(R.id.btnSaveSetttings);


        lblDistance.setText(getString(R.string.search_radius_for_clinics)+distance+" Km");
        lblPercentage.setText(getString(R.string.minimus_search_percentage)+percentage+"%");
        toggleSettings.setChecked(savedData.getBoolean("SEARCH_AT_START",true));

        barDistance.setRangePinsByValue(0.5f, distance);
        barPercentage.setRangePinsByValue(1,percentage);


        barDistance.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                distance = Float.parseFloat(rightPinValue);
                lblDistance.setText(getString(R.string.search_radius_for_clinics)+distance+" Km");
            }
        });
        barPercentage.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                percentage = Integer.parseInt(rightPinValue);
                lblPercentage.setText(getString(R.string.minimus_search_percentage)+percentage+"%");
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Updates();
            }
        });
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
            }
        });
    }


    private void saveSettings(){
        editSavedData.putFloat("DISTANCE",distance);
        editSavedData.putInt("PERCENTAGE",percentage);
        editSavedData.putBoolean("SEARCH_AT_START",toggleSettings.isChecked());
        editSavedData.commit();
        this.finish();
    }

    private void Updates(){
        try {
            searchUpdates.getVersion(false);
        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }

    }
}
