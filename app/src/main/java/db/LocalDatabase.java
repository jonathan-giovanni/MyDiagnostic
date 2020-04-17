package db;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.dev4u.hv.my_diagnostic.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import utils.ConnectionSettings;
import utils.SearchUpdates;
import utils.VolleySingleton;

/**
 * Created by admin on 14/6/17.
 */

public class LocalDatabase {

    enum ErrorType{
        UNEXPECTED,
        BACKEND
    }

    ErrorType type;

    private Context context;
    private Gson gson;
    private Database db;
    Handler handler = new Handler();
    int mRequestCount = 6;//six tables to fill the database
    CountDownLatch requestCountDown = new CountDownLatch(mRequestCount);
    private Thread checkQueueThread;
    private AlertDialog alertDialogErrorUnexpected;
    private AlertDialog alertDialogErrorBackend;
    private Button button;
    private CoordinatorLayout coordinatorLayout;
    private boolean downloadFinished;
    private static Map stringJSONObjectMap;

    private static String getSymptom,getDisease,getDiseaseSymptom,getDiseaseCategory;

    public LocalDatabase(Context context,Button button,CoordinatorLayout coordinatorLayout){
        type            = ErrorType.BACKEND;
        this.context    = context;
        gson            = new Gson();
        db              = new Database(this.context);
        this.button     = button;
        this.coordinatorLayout = coordinatorLayout;

        /**GET information in diff languages*/
        String idiom = context.getResources().getConfiguration().locale.getLanguage().toLowerCase();
        if(idiom.equals("es")){
            getSymptom          = ConnectionSettings.getSintomas_esp;
            getDisease          = ConnectionSettings.getEnfermedades_esp;
            getDiseaseSymptom   = ConnectionSettings.getEnfermedadSintoma_esp;
            getDiseaseCategory  = ConnectionSettings.getCategorias_esp;
        }else{
            getSymptom          = ConnectionSettings.GETSymptom;
            getDisease          = ConnectionSettings.GetDisease;
            getDiseaseSymptom   = ConnectionSettings.GetDiseaseSymptom;
            getDiseaseCategory  = ConnectionSettings.GETDiseases_category;
        }

        initDialog();
    }

    private void checkQueue(){
        checkQueueThread = new Thread(new Runnable() {
            boolean interrupted=false;
            public void run() {
                try {
                    requestCountDown.await();
                    downloadFinished=false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    interrupted=true;
                    downloadFinished=false;
                }
                handler.post(new Runnable() {
                    public void run() {
                        if(!interrupted) {
                            new saveInLocal().execute(stringJSONObjectMap);
                        }
                    }
                });
            }
        });
        checkQueueThread.start();
    }
    public void initDialog(){
        AlertDialog.Builder alertDialogBuiliderUnexpected = new AlertDialog.Builder(context);
        alertDialogBuiliderUnexpected.setTitle("Error");
        alertDialogBuiliderUnexpected.setMessage(R.string.an_error_has_ocurred);
        alertDialogBuiliderUnexpected.setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(checkQueueThread!=null){
                    checkQueueThread.interrupt();
                    checkQueueThread = null;
                }
                initDatabase();
            }
        });
        alertDialogBuiliderUnexpected.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        alertDialogErrorUnexpected = alertDialogBuiliderUnexpected.create();

        //backend
        AlertDialog.Builder alertDialogBuiliderBackend = new AlertDialog.Builder(context);
        alertDialogBuiliderBackend.setTitle("Error");
        alertDialogBuiliderBackend.setMessage("Ha ocurrido un error en en el servidor de My Diagnostic\nponte en contacto nosotros para informar de la falla: \n\nhv12i04001@gmail.com\n\nno al correo\n\nmarvinmendez1605@gmail.com\n\nGracias por tu colaboración");
        alertDialogBuiliderBackend.setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if(checkQueueThread!=null){
                    checkQueueThread.interrupt();
                    checkQueueThread = null;
                }
                initDatabase();
            }
        });
        alertDialogBuiliderBackend.setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        alertDialogErrorBackend = alertDialogBuiliderBackend.create();

    }

    public void initDatabase(){
        //TODO esta es la funcion que llama las peticiones
        stringJSONObjectMap = new HashMap();
        Snackbar.make(coordinatorLayout, R.string.download_started,Snackbar.LENGTH_SHORT).show();
        button.setText(R.string.downloading);
        checkQueue();
        deleteData();

        getCountry();
        getBloodType();
        getDiseaseCategory();
        getSymptoms();
        getDisease();
        getSymptomsDisease();
    }

    private void hasError(final ErrorType type){
        db.deleteData();
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                if(type==ErrorType.UNEXPECTED){
                    alertDialogErrorUnexpected.show();
                }else{
                    alertDialogErrorBackend.show();
                }
            }
        });
    }

    private void hasError(){
        hasError(type);
    }


    public void deleteData(){
        db.deleteData();
    }

    public Database getDatabase(){
        return db;
    }


    public void getCountry() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GETCountry,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("COUNTRY",response);
                                        requestCountDown.countDown();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        hasError();
                                    }
                                }

                        )
                );
    }

    private boolean saveCountry(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("multitable");
                    Country[] countries = gson.fromJson(mensaje.toString(), Country[].class);
                    for (Country s:countries) {
                        String n = s.getName_country().replaceAll("'","''");
                        db.saveCountry(s.getId_country(),n,s.getShort_name());
                    }
                    Log.d("BACKEND", "Se completo obtener datos de backend get country ");
                    return true;
                default: // FAIL
                    Log.e("BACKEND", "Error en obtener datos de backend get country: "+response.toString());
                    type = ErrorType.BACKEND;
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }
    //bloodtype
    public void getBloodType() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                ConnectionSettings.GETBlood_type,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("BLOODTYPE",response);
                                        requestCountDown.countDown();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        hasError();
                                    }
                                }

                        )
                );
    }

    private boolean saveBloodType(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("multitable");
                    Bloodtype[] bloodtypes = gson.fromJson(mensaje.toString(), Bloodtype[].class);
                    for (Bloodtype s:bloodtypes) {
                        db.saveBloodType(s.getId_bloodtype(),s.getBloodtype());
                    }
                    Log.d("BACKEND", "Se completo obtener datos de backend get bloodtype");
                    return true;
                default: // FAIL
                    Log.e("BACKEND", "Error en obtener datos de backend get bloodtype: "+response.toString());
                    type = ErrorType.BACKEND;
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getDiseaseCategory() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                getDiseaseCategory,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("CATEGORY",response);
                                        requestCountDown.countDown();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        hasError();
                                    }
                                }

                        )
                );
    }

    private boolean saveDiseasesCategory(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("diseases_category");
                    Diseases_category[] diseases_categories = gson.fromJson(mensaje.toString(), Diseases_category[].class);
                    for (Diseases_category s:diseases_categories) {
                        String n = s.getCategory_name().replaceAll("'","''");
                        String d = s.getCategory_description().replaceAll("'","''");
                        db.saveDiseaseCategory(s.getId_disease_category(),n,d);
                    }
                    Log.d("BACKEND", "Se completo obtener datos de backend get diseases category");
                    return true;
                default: // FAIL
                    Log.e("BACKEND", "Error en obtener datos de backend get diseases category: "+response.toString());
                    type = ErrorType.BACKEND;
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getSymptoms() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                getSymptom,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("SYMPTOMS",response);
                                        requestCountDown.countDown();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        hasError();
                                    }
                                }

                        )
                );
    }

    private boolean saveSymptoms(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("symptoms");
                    Symptom[] symptom = gson.fromJson(mensaje.toString(), Symptom[].class);
                    for (Symptom s:symptom) {
                        db.saveSymptom(s.getid_symptom(),s.getSymptom());
                    }
                    Log.d("BACKEND", "Se completo obtener datos de backend get symptoms");
                    return true;
                default: // FAIL
                    Log.e("BACKEND", "Error en obtener datos de backend get symptoms: "+response.toString());
                    type = ErrorType.BACKEND;
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getDisease() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                getDisease,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("DISEASES",response);
                                        requestCountDown.countDown();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        hasError();
                                    }
                                }
                        )
                );
    }

    private boolean saveDiseases(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("diseases");
                    Disease[] diseases = gson.fromJson(mensaje.toString(), Disease[].class);
                    for (Disease s:diseases) {
                        String n = s.getName_disease().replaceAll("'","''");
                        String d = s.getDescription().replaceAll("'","''");
                        db.saveDisease(s.getId_disease(),n,d,s.getId_disease_category());
                    }
                    Log.d("BACKEND", "Se completo obtener datos de backend get diseases");
                    return true;
                default: // FAIL
                    Log.e("BACKEND", "Error en obtener datos de backend get diseases: "+response.toString());
                    type = ErrorType.BACKEND;
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void getSymptomsDisease() {
        VolleySingleton.
                getInstance(context).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                getDiseaseSymptom,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        stringJSONObjectMap.put("SYMPTOM_DISEASE",response);
                                        requestCountDown.countDown();
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                        hasError();
                                    }
                                }
                        )
                );
    }

    private boolean saveSymptomsDisease(JSONObject response) {
        try {
            switch (response.getString("estado")) {
                case "1": // SUCCESS
                    JSONArray mensaje = response.getJSONArray("multitable");
                    SymptomDisease[] symptomdiseases = gson.fromJson(mensaje.toString(), SymptomDisease[].class);
                    for (SymptomDisease s:symptomdiseases) {
                        db.saveSymptomDisease(s.getId_sympdiseases(),s.getId_diseases(),s.getId_symptom());
                    }
                    Log.d("BACKEND", "Se completo obtener datos de backend get symptoms-diseases");
                    return true;
                default: // FAIL
                    Log.e("BACKEND", "Error en obtener datos de backend get symptoms-diseases: "+response.toString());
                    type = ErrorType.BACKEND;
                    hasError();
                    return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public class saveInLocal extends AsyncTask<Map, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Map... params) {
            boolean state = fillDB(params[0]);
            if(isCancelled()) return false;
            return state;
        }
        private boolean fillDB(Map map){
            try{
                boolean state=true;
                if(map.containsKey("COUNTRY"))      state = saveCountry((JSONObject) map.get("COUNTRY"));
                if(map.containsKey("BLOODTYPE"))    state = saveBloodType((JSONObject) map.get("BLOODTYPE"));
                if(map.containsKey("CATEGORY"))     state = saveDiseasesCategory((JSONObject) map.get("CATEGORY"));
                if(map.containsKey("SYMPTOMS"))     state = saveSymptoms((JSONObject) map.get("SYMPTOMS"));
                if(map.containsKey("DISEASES"))     state = saveDiseases((JSONObject) map.get("DISEASES"));
                if(map.containsKey("SYMPTOM_DISEASE"))  state = saveSymptomsDisease((JSONObject) map.get("SYMPTOM_DISEASE"));
                SearchUpdates s = new SearchUpdates(context,true);
                s.getVersion(true);
                return state;
            }catch (Exception e){
                e.printStackTrace();
                hasError();
                return false;
            }

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean==true){
                downloadFinished=true;
                button.setText(R.string.continuar);
                Snackbar.make(coordinatorLayout, R.string.download_finished,Snackbar.LENGTH_SHORT).show();
                if (alertDialogErrorUnexpected.isShowing()) alertDialogErrorUnexpected.dismiss();
            }else{
                hasError();
            }
        }
    }



    public boolean isDownloadFinished() {
        return downloadFinished;
    }
}
