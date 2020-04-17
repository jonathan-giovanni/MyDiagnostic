package org.dev4u.hv.my_diagnostic;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        WebView wView = (WebView) findViewById(R.id.webView);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.loadUrl("file:///android_asset/terms.html");
        wView.setWebViewClient(new MyWebViewClient());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                break;
            case android.R.id.home:
                IntroductionActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
