package org.dev4u.hv.my_diagnostic.FragmentsIntro;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.DownloadActivity;
import org.dev4u.hv.my_diagnostic.FragmentsIntro.MiAnimacion;
import org.dev4u.hv.my_diagnostic.R;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class LauncherActivity extends AppCompatActivity {


    private ViewPager viewPager;
    private CircleIndicator indicator;
    RelativeLayout backgroundApp;
    TextView lblSkip;
    AnimationDrawable animationDrawable;
    int pages=0;
    Fragment_disclaimer frmfive;
    private SharedPreferences savedData;
    private SharedPreferences.Editor editSavedData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        getSupportActionBar().hide();
        savedData = getSharedPreferences("Data", Context.MODE_PRIVATE);
        editSavedData = savedData.edit();


        viewPager = (ViewPager) findViewById(R.id.pager_introduction);
        indicator = (CircleIndicator) findViewById(R.id.indicator);

        backgroundApp = (RelativeLayout)findViewById(R.id.launcher_background);
        animationDrawable = (AnimationDrawable) backgroundApp.getBackground();

        animationDrawable.setEnterFadeDuration(8000);
        animationDrawable.setExitFadeDuration(4000);

        lblSkip = (TextView)findViewById(R.id.lblSkip);
        setupViewPager(viewPager);
        indicator.setViewPager(viewPager);

        lblSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager.getCurrentItem()!=(pages-1)){
                    viewPager.setCurrentItem( viewPager.getCurrentItem()+1 );
                }else{
                    if(viewPager.getCurrentItem()==pages-1){
                        if(frmfive.isAgreeChecked){
                            editSavedData.putBoolean("AGREE",true);
                            editSavedData.commit();
                            Intent gotoDownload = new Intent(LauncherActivity.this,DownloadActivity.class);
                            startActivity(gotoDownload);
                            finish();
                        }else{
                            Toast.makeText(LauncherActivity.this, R.string.if_you_want_to_use_my_diagnostic,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        FirstIntroductionFragment frmFirst = new FirstIntroductionFragment();
        SecondIntroductionFragment frmSecond = new SecondIntroductionFragment();
        ThirdIntroductionFragment frmThird = new ThirdIntroductionFragment();
        FourIntroductionFragment frmFour = new FourIntroductionFragment();
        frmfive = new Fragment_disclaimer();
        adapter.addFragment(frmFirst);
        adapter.addFragment(frmSecond);
        adapter.addFragment(frmThird);
        adapter.addFragment(frmFour);
        adapter.addFragment(frmfive);
        pages=5;
        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(animationDrawable != null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(animationDrawable!=null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }

}
