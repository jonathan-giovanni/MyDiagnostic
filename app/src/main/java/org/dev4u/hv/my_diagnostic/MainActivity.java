package org.dev4u.hv.my_diagnostic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ncapdevi.fragnav.FragNavController;
import com.ncapdevi.fragnav.FragNavTransactionOptions;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabReselectListener;
import com.roughike.bottombar.OnTabSelectListener;

import org.dev4u.hv.my_diagnostic.Fragments.BaseFragment;
import org.dev4u.hv.my_diagnostic.Fragments.DiseaseFragment;
import org.dev4u.hv.my_diagnostic.Fragments.HistoryFragment;
import org.dev4u.hv.my_diagnostic.Fragments.MapFragment;
import org.dev4u.hv.my_diagnostic.Fragments.SearchFragment;
import org.dev4u.hv.my_diagnostic.FragmentsIntro.LauncherActivity;

import utils.DiseaseUtilitesSingleton;
import utils.SearchUpdates;

public class MainActivity extends AppCompatActivity implements BaseFragment.FragmentNavigation, FragNavController.TransactionListener, FragNavController.RootFragmentListener,Thread.UncaughtExceptionHandler{


    private final int RESULT_FROM_MAIN=2;
    private final int INDEX_SEARCH  = FragNavController.TAB1;
    private final int INDEX_HISTORY = FragNavController.TAB2;
    private final int INDEX_DISEASE = FragNavController.TAB3;
    private final int INDEX_MAP     = FragNavController.TAB4;
    private static SearchFragment frm1   = new SearchFragment();
    private static HistoryFragment frm2  = new HistoryFragment();
    private static DiseaseFragment frm3  = new DiseaseFragment();
    private static MapFragment frm4      = new MapFragment();
    private SharedPreferences savedData;
    private Handler handler = new Handler();

    private static boolean threadFinish=false;
    private BottomBar mBottomBar;
    private FragNavController mNavController;
    private SharedPreferences.Editor editSavedData;
    private int initial;
    public static final String FINISH_ALERT = "finish_alert";

    BroadcastReceiver finishAlert = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            MainActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        savedData = getSharedPreferences("Data", Context.MODE_PRIVATE);
        editSavedData = savedData.edit();
        initial = savedData.getInt("STATUS",0);

        if(!savedData.getBoolean("AGREE",false)){
            Intent gotoBeginning = new Intent(this,LauncherActivity.class);
            startActivity(gotoBeginning);
            this.finish();
        }else{
            if(initial==0){
                Intent gotoBeginning = new Intent(this,DownloadActivity.class);
                startActivity(gotoBeginning);
                this.finish();
            }else{
                if(savedData.getBoolean("SEARCH_AT_START",true)) new SearchUpdates(this,true).getVersion(false);
            }
        }



        Init();
        //animation
        //.defaultTransactionOptions(FragNavTransactionOptions.newBuilder().customAnimations
        // (R.anim.slide_int_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right).build())

        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);
        mBottomBar.selectTabAtPosition(0);
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.container)
                .transactionListener(this)
                .rootFragmentListener(this, 4)
                .defaultTransactionOptions(FragNavTransactionOptions.newBuilder().customAnimations(R.anim.alpha_in, R.anim.alpha_out,R.anim.alpha_in,R.anim.alpha_out).build())
                .build();



        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.bb_menu_search:
                        mNavController.switchTab(INDEX_SEARCH);
                        break;
                    case R.id.bb_menu_history:
                        mNavController.switchTab(INDEX_HISTORY);
                        break;
                    case R.id.bb_menu_diseases:
                        mNavController.switchTab(INDEX_DISEASE);
                        break;
                    case R.id.bb_menu_map:
                        mNavController.switchTab(INDEX_MAP);
                        break;

                }
            }
        });

        mBottomBar.setOnTabReselectListener(new OnTabReselectListener() {
            @Override
            public void onTabReSelected(@IdRes int tabId) {
                mNavController.clearStack();
            }
        });


        this.registerReceiver(this.finishAlert, new IntentFilter(FINISH_ALERT));
    }//end onCreate


    private void initFragments(){
        frm1   = new SearchFragment();
        frm2  = new HistoryFragment();
        frm3  = new DiseaseFragment();
        frm4      = new MapFragment();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        this.unregisterReceiver(finishAlert);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //savedData = getSharedPreferences("Data", Context.MODE_PRIVATE);
        //Log.d("ON RESUMEN","<=================================================== previous : "+initial +" after : "+savedData.getInt("STATUS",0));
    }

    @Override
    public void onBackPressed() {
        if (!mNavController.isRootFragment()) {
            mNavController.popFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_commom, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                goToSettings();
                return true;
            case R.id.action_profile:
                gotoProfile();
                return true;
            case R.id.action_about:
                gotoAbout();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void gotoAbout(){
        ///TODO aqui llamar al about
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);

    }

    public void Init(){
        DiseaseUtilitesSingleton.getInstance().init(this);
        new Thread(new Runnable() {
            public void run() {
                DiseaseUtilitesSingleton.getInstance().fillData();
                handler.post(new Runnable() {
                    public void run() {
                        threadFinish=true;
                        if(frm1!=null)
                            frm1.updateFragment();

                    }
                });
            }
        }).start();
    }
    @Override
    public Fragment getRootFragment(int index) {
        switch (index) {
            case INDEX_SEARCH:
                return frm1;
            case INDEX_HISTORY:
                return frm2;
            case INDEX_DISEASE:
                return frm3;
            case INDEX_MAP:
                return frm4;
        }
        throw new IllegalStateException("Need to send an index that we know");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mNavController != null) {
            mNavController.onSaveInstanceState(outState);
        }
    }


    @Override
    public void onTabTransaction(Fragment fragment, int i) {

    }

    @Override
    public void onFragmentTransaction(Fragment fragment, FragNavController.TransactionType transactionType) {

    }

    @Override
    public void pushFragment(Fragment fragment) {
        if (mNavController != null) {
            mNavController.pushFragment(fragment);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==RESULT_FROM_MAIN){

            if(mNavController.getCurrentStackIndex()==FragNavController.TAB2){
                if(mNavController.getCurrentStack().size()==1){
                    frm2.updateUserInfo();
                }
            }
        }
    }
    private void goToSettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent,RESULT_FROM_MAIN);
    }
    private void gotoProfile(){
        Intent intent = new Intent(this, UserDataActivity.class);
        startActivityForResult(intent,RESULT_FROM_MAIN);
    }

    @Override
    public void uncaughtException(@NonNull Thread t, @NonNull Throwable e) {
        e.printStackTrace();
        Toast.makeText(this,"Ocurrio un problema",Toast.LENGTH_SHORT).show();
        mNavController.clearStack();
        mNavController.switchTab(INDEX_SEARCH);
    }
}
