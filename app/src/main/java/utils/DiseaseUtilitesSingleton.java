package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import db.Bloodtype;
import db.Country;
import db.Database;
import db.Disease;
import db.MedicalHistory;
import db.Symptom;
import db.User;

/**
 * Created by admin on 4/6/17.
 */

public class DiseaseUtilitesSingleton {

    private ArrayList<Symptom> temporarySymptoms;
    private ArrayList<Disease> diseasesList;
    private ArrayList<Symptom> allSymptomsList;
    private ArrayList<String> diseasesNames;
    private ArrayList<String> symptomsNames;
    private ArrayList<Country> countryArrayList;
    private ArrayList<Bloodtype> bloodtypeArrayList;
    private ArrayList<MedicalHistory> medicalHistoryArrayList;
    private static User user=null;
    public static float minimunPercentage = 20.0f;
    public HistoryAdapter historyAdapter;
    private Database db;
    private Context context;
    private Thread fillThread;
    private Handler fillHandler = new Handler();
    private SharedPreferences preferences;
    private SharedPreferences.Editor editorPreferences;
    private static DiseaseUtilitesSingleton instance = null;

    protected DiseaseUtilitesSingleton(){
        temporarySymptoms=new ArrayList<>();
    }

    public static DiseaseUtilitesSingleton getInstance(){
        if(instance==null){
            instance = new DiseaseUtilitesSingleton();
        }
        return instance;
    }

    public void init(Context c){
        db = new Database(c);
        context = c;
        preferences = context.getSharedPreferences("Data", Context.MODE_PRIVATE);
        editorPreferences = preferences.edit();
        String status = preferences.getString("USERNAME","null");
        if(!status.equals("null")){
            user = getUser(status);
        }
    }

    public Disease searchDisease(String name){
        for (Disease disease : diseasesList) {
            if (disease.getName_disease().equals(name)) {
                return disease;
            }
        }
        return null;
    }

    public Disease getDisease(String id){
        for (Disease disease : diseasesList) {
            if (disease.getId_disease().equals(id)) {
                return disease;
            }
        }
        return null;
    }

    public MedicalHistory getHistory(String id){
        for (MedicalHistory md : medicalHistoryArrayList) {
            if (md.getId_medicalhistory().equals(id)) {
                return md;
            }
        }
        return null;
    }


    public void fillData(){
        Cursor diseaseCursor = db.getDiseases();
        Cursor allSymptomsCursor = db.getSymptoms();
        Cursor medicalHistoryCursor;
        //ERROR
        if(user!=null)
            medicalHistoryCursor = db.getMedicalHistory(user.getUsername());
        else
            medicalHistoryCursor = db.getMedicalHistory("");

        diseaseCursor.moveToFirst();
        allSymptomsCursor.moveToFirst();
        medicalHistoryCursor.moveToFirst();

        diseasesList = new ArrayList<>();
        allSymptomsList = new ArrayList<>();
        symptomsNames = new ArrayList<>();
        diseasesNames = new ArrayList<>();
        medicalHistoryArrayList = new ArrayList<>();

        //llenando todas las enfermedades y sus sintomas
        if(diseaseCursor != null && diseaseCursor.getCount() > 0){
            diseaseCursor.moveToFirst();
            do{
                //filling up the diseases and the symptoms
                ArrayList<Symptom> symptomsList = new ArrayList<>();
                Cursor symptomCursor = db.getDiseaseSymptoms(diseaseCursor.getString(0));
                symptomCursor.moveToFirst();
                if(symptomCursor != null && symptomCursor.getCount() > 0){
                    do{
                        symptomsList.add(new Symptom(symptomCursor.getString(0),symptomCursor.getString(1)));
                        Log.d("Enfemedad : "+diseaseCursor.getString(1),"Sintoma "+symptomCursor.getString(1));
                    }while (symptomCursor.moveToNext());
                    diseasesNames.add(diseaseCursor.getString(0));
                    Collections.sort(symptomsList);
                    Disease d = new Disease(diseaseCursor.getString(0),diseaseCursor.getString(1),diseaseCursor.getString(2),diseaseCursor.getString(3),symptomsList);
                    Log.d("Cargado"," id categoria "+diseaseCursor.getString(3)+ " nombre "+diseaseCursor.getString(4));
                    d.setCategory_name(diseaseCursor.getString(4));
                    diseasesList.add(d);
                }
            }while(diseaseCursor.moveToNext());
            Collections.sort(diseasesList, Disease.compareByName() );
            Collections.sort(diseasesNames);
        }
        //llenando todos los sintomas
        if(allSymptomsCursor != null && allSymptomsCursor.getCount() > 0){
            do{
                allSymptomsList.add(new Symptom(allSymptomsCursor.getString(0),allSymptomsCursor.getString(1)));
                symptomsNames.add(allSymptomsCursor.getString(1));
            }while (allSymptomsCursor.moveToNext());
            Collections.sort(allSymptomsList);
            Collections.sort(symptomsNames);
        }

        if(medicalHistoryCursor != null && medicalHistoryCursor.getCount() > 0){
            do{
                MedicalHistory md = new MedicalHistory(
                        medicalHistoryCursor.getString(0),//id
                        medicalHistoryCursor.getString(1),//title
                        medicalHistoryCursor.getString(2),//description
                        medicalHistoryCursor.getString(3),//id_disease
                        medicalHistoryCursor.getString(4),//username
                        medicalHistoryCursor.getString(5)//datetime
                );
                md.setName_disease(medicalHistoryCursor.getString(6));
                medicalHistoryArrayList.add(md);
            }while (medicalHistoryCursor.moveToNext());
        }

    }//end fillData

    public void fillPrimaryData(){
        countryArrayList = new ArrayList<>();
        bloodtypeArrayList = new ArrayList<>();
        Cursor countriesCursor = db.getCountry();
        Cursor bloodtypeCursor = db.getBloodType();
        countriesCursor.moveToFirst();
        bloodtypeCursor.moveToFirst();
        if(countriesCursor!=null && countriesCursor.getCount()>0){
            do{
                countryArrayList.add(new Country(countriesCursor.getString(0),countriesCursor.getString(1),countriesCursor.getString(2)));
            }while (countriesCursor.moveToNext());
        }
        if(bloodtypeCursor!=null && bloodtypeCursor.getCount()>0){
            do{
                bloodtypeArrayList.add(new Bloodtype(bloodtypeCursor.getString(0),bloodtypeCursor.getString(1)));
            }while (bloodtypeCursor.moveToNext());
        }
    }


    public User getActiveUser(){
        return user;
    }

    public void updateUser(User username){
        db.saveUser(username);
        user = db.getUser(username.getUsername());
    }

    public User getUser(String username){
        return db.getUser(username);
    }

    public String getIdCountry(String name){
        String id=null;

        for(int i=0;i<countryArrayList.size();i++)
            if(countryArrayList.get(i).getName_country().contains(name)) return countryArrayList.get(i).getId_country();

        return id;
    }
    public String getIdBloodType(String type){
        String id=null;
        for(int i=0;i<bloodtypeArrayList.size();i++)
            if(bloodtypeArrayList.get(i).getBloodtype().contains(type)) return bloodtypeArrayList.get(i).getId_bloodtype();

        return id;
    }


    public ArrayList<Disease> getDiseasesMatches(ArrayList<String> inputs){
        ArrayList<Disease> diseasesfound = new ArrayList<>();
        for(int i=0;i<diseasesList.size();i++)
            if(diseasesList.get(i).evaluateSymptoms(inputs)>=minimunPercentage) diseasesfound.add(diseasesList.get(i));
        Collections.sort(diseasesfound,Disease.compareByPercentage());
        return diseasesfound;
    }
    public ArrayList<Symptom> getAllSymptomsList(){return this.allSymptomsList;}

    public ArrayList<String> getDiseasesNames() {
        return diseasesNames;
    }

    public ArrayList<String> getSymptomsNames() {
        return symptomsNames;
    }

    public ArrayList<Symptom> getTemporarySymptoms() {
        return temporarySymptoms;
    }

    public void deleteHistory(String id){
        for (MedicalHistory md: medicalHistoryArrayList) {
            if(md.getId_medicalhistory().equals(id)){
                medicalHistoryArrayList.remove(md);
                break;
            }
        }
        db.deleteMedicalHistory(id);
    }


    public void saveOrUpdateHistory(boolean firsttime,MedicalHistory md){
        String id= db.saveMedicalHistory(md);
        if(firsttime){
            if(id!=null){
                md.setId_medicalhistory(id);
                medicalHistoryArrayList.add(md);
            }else{
                Log.d("Error ","Al asignar id");
            }
        }
        Log.d("MD Guardados : "," "+db.getMedicalHistory(user.getUsername()).getCount());
    }


    public void setTemporarySymptoms(ArrayList<Symptom> temporarySymptoms) {
        this.temporarySymptoms = temporarySymptoms;
    }

    public ArrayList<Disease> getDiseasesList() {
        return diseasesList;
    }

    public ArrayList<MedicalHistory> getMedicalHistoryList(){return medicalHistoryArrayList;}

    public void setDiseasesList(ArrayList<Disease> diseasesList) {
        this.diseasesList = diseasesList;
    }


}
