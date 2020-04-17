package org.dev4u.hv.my_diagnostic.MyPlacesUI;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import org.dev4u.hv.my_diagnostic.MyPlacesUI.PlacesDetails;
import org.dev4u.hv.my_diagnostic.R;
import org.json.JSONObject;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, PlacesDetails> {

    JSONObject googlePlacesJson;
  String googleMap="mapa";
    PlacesDetails detailsJson= new PlacesDetails();
   MyPlacesJson myJson = new MyPlacesJson();
    public void setMycontext(Context mycontext) {
        this.mycontext = mycontext;
    }
  private   Context mycontext;


    @Override
    protected PlacesDetails doInBackground(Object... inputObj) {

       String googlePlacesList = null;
        MyPlacesJson placeJsonParser = new MyPlacesJson();

        try {
            googleMap = (String) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);

           detailsJson=(myJson.GetPlaceDetails(googlePlacesJson));
            return detailsJson;
        } catch (Exception e) {

        }
        return detailsJson;
    }
    private void showLocationDialog(String datos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.mycontext);
        builder.setTitle("Place Details");
        builder.setMessage(datos);
        String negativeText = "Close";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }
    @Override
    protected void onPostExecute(PlacesDetails details) {

        String separador="\n\n";
        String cadena="Name: "+details.getPlace_name()+separador+"Phone Number: "+details.getPhone_number() +separador
                +"Address: " + details.getAddress();
        showLocationDialog(cadena);



    }
}

