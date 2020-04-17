package org.dev4u.hv.my_diagnostic.MyPlacesUI;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;

public class GooglePlacesReadTask extends AsyncTask<Object, Integer, String> {
    String googlePlacesData = null;
    String googleMap="other_map";

    public Context getMyothercontext() {
        return myothercontext;
    }

    public void setMyothercontext(Context myothercontext) {
        this.myothercontext = myothercontext;
    }

    Context myothercontext;

    @Override
    protected String doInBackground(Object... inputObj) {
        try {
            googleMap = (String) inputObj[0];
            String googlePlacesUrl = (String) inputObj[1];
            Http http = new Http();
            googlePlacesData = http.read(googlePlacesUrl);
        } catch (Exception e) {

        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        PlacesDisplayTask placesDisplayTask = new PlacesDisplayTask();
        Object[] toPass = new Object[2];
        toPass[0] = googleMap;
        toPass[1] = result;
        placesDisplayTask.setMycontext(getMyothercontext());
        placesDisplayTask.execute(toPass);
    }
}