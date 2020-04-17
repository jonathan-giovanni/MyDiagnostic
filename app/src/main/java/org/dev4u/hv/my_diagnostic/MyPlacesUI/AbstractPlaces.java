package org.dev4u.hv.my_diagnostic.MyPlacesUI;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractPlaces extends AsyncTask<Void, Void, List<Place>> {
    protected final String PARAM_KEY = "key=";
    protected final String PARAM_LOCATION = "location=";
    protected final String PARAM_RADIUS = "radius=";
    protected final String PARAM_RANK_BY = "rankby=";
    protected final String PARAM_KEYWORD = "keyword=";
    protected final String PARAM_LANGUAGE = "language=";
    protected final String PARAM_MINPRICE = "minprice=";
    protected final String PARAM_MAXPRICE = "maxprice=";
    protected final String PARAM_NAME = "name=";
    protected final String PARAM_TYPE = "type=";
    protected final String PARAM_PAGETOKEN = "pagetoken=";
    protected String PLACES_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    private PlacesException exception;
    private List<PlacesListener> listeners;

    protected AbstractPlaces(PlacesListener listener) {
        this.listeners = new ArrayList<>();
        registerListener(listener);
    }

    @Override
    protected List<Place> doInBackground(Void... params) {
        try {
            //first 20 records
            Pair<String, List<Place>> pair =
                    new PlacesParser(constructURL(null)).parseNearbyPlaces();
            dispatchOnSuccess(pair.second);
            //check for 20-40records
            if (pair.first != null) {
                Thread.sleep(2000);
                pair = new PlacesParser(constructURL(pair.first)).parseNearbyPlaces();
                dispatchOnSuccess(pair.second);

                //check for 40-60 records
                if (pair.first != null) {
                    Thread.sleep(2000);
                    pair = new PlacesParser(constructURL(pair.first)).parseNearbyPlaces();
                    dispatchOnSuccess(pair.second);

                }
            }

            return pair.second;
        } catch (PlacesException e) {
            exception = e;
            dispatchOnFailure(e);
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        dispatchOnStart();
    }

    @Override
    protected void onPostExecute(List<Place> places) {
        if (places == null) {
            dispatchOnFailure(exception);
        } else {
            dispatchOnFinished();
        }

    }

    public void registerListener(PlacesListener mListener) {
        if (mListener != null) {
            listeners.add(mListener);
        }
    }

    protected void dispatchOnStart() {
        for (PlacesListener mListener : listeners) {
            mListener.onPlacesStart();
        }
    }

    protected void dispatchOnFailure(PlacesException exception) {
        Log.e("MyPlacesUI", exception.toString());
        for (PlacesListener mListener : listeners) {
            mListener.onPlacesFailure(exception);

        }
    }

    protected void dispatchOnSuccess(List<Place> places) {
        for (PlacesListener mListener : listeners) {
            mListener.onPlacesSuccess(places);
        }
    }

    private void dispatchOnFinished() {
        for (PlacesListener mListener : listeners) {
            mListener.onPlacesFinished();
        }
    }

    protected abstract String constructURL(String nexPageToken);


}
