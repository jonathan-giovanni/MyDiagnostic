package org.dev4u.hv.my_diagnostic.MyPlacesUI;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class PlacesException extends Exception {
    private static final String TAG = "PlacesException";
    private static final String KEY_STATUS = "status";

    private String statusCode;
    private String message;

    public PlacesException(JSONObject json){
        if(json == null){
            statusCode = "";
            message = "Parsing error";
            return;
        }
        try {
            statusCode = json.getString(KEY_STATUS);
            message = json.getString(KEY_STATUS);
        } catch (JSONException e) {

        }
    }

    public PlacesException(String msg){
        message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getStatusCode() {
        return statusCode;
    }
}