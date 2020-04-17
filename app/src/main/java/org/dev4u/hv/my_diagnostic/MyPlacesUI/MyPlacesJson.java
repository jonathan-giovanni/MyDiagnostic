package org.dev4u.hv.my_diagnostic.MyPlacesUI;
import android.util.Log;

import com.google.android.gms.location.places.PlaceDetectionApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesDetails;
public class MyPlacesJson {
    PlacesDetails details = new PlacesDetails();
    public PlacesDetails GetPlaceDetails(JSONObject jsonObject) {

        String json=jsonObject.toString();
        try {
            JSONObject parentObject = new JSONObject(json);
            JSONObject userDetails = parentObject.getJSONObject("result");

            //And then read attributes like
            if(!userDetails.isNull("name")){
            details.setPlace_name(userDetails.getString("name"));}
            else
            {details.setPlace_name("N/A");}
            if(!userDetails.isNull("formatted_phone_number")){
                details.setPhone_number( userDetails.getString("formatted_phone_number"));}
            else
            {
                details.setPhone_number("N/A");
            }
            if(!userDetails.isNull("formatted_address")){
                details.setAddress(userDetails.getString("formatted_address"));}
            else{ details.setAddress("N/A");}

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return details;
    }
    
}