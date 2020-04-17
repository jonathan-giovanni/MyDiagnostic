package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 3/6/17.
 */

public class DBHelper extends SQLiteOpenHelper {



    private String createDiseases_category="create table diseases_category"+
            "("+
            "id_disease_category INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "category_name VARCHAR(250),"+
            "category_description VARCHAR(250)"+
            ")";

    private String createDiseases="create table diseases"+
            "("+
            "id_diseases INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "name_disease VARCHAR(250),"+
            "description TEXT,"+
            "id_disease_category INTEGER,"+
            "foreign key (id_disease_category) references diseases_category (id_disease_category)" +
            ")";
    private String createCountry="create table country"+
            "("+
            "id_country INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "name_country VARCHAR(100),"+
            "short_name VARCHAR(4)"+
            ")";
    private String createAllergies="create table allergies"+
            "("+
            "id_allergy INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "allergy VARCHAR(250)"+
            ")";
    private String createBloodtype="create table bloodtype"+
            "("+
            "id_bloodtype INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "bloodtype VARCHAR(20)"+
            ")";
    private String createDatabaseVersion="create table database_version"+
            "("+
            "id_version INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "version INTEGER,"+
            "To_date TEXT"+
            ")";
    private String createSymptons="create table symptoms"+
            "("+
            "id_symptom INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "symptom VARCHAR(250)"+
            ")";
    private String createUserSystem="create table User_system"+
            "("+
            "username VARCHAR(250) PRIMARY KEY,"+
            "fullname VARCHAR(250),"+
            "genre VARCHAR(30),"+
            "birthday TEXT,"+
            "id_country INTEGER,"+
            "id_bloodtype INTEGER,"+
            "id_allergy INTEGER DEFAULT NULL,"+
            "foreign key (id_country) references country (id_country)," +
            "foreign key (id_bloodtype) references bloodtype (id_bloodtype)," +
            "foreign key (id_allergy) references allergies (id_allergy)" +
            ")";
    private String createMedicalHistory="create table medicalhistory"+
            "("+
            "id_medicalhistory INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "title VARCHAR(250),"+
            "date_time TEXT,"+
            "description TEXT DEFAULT NULL,"+
            "id_diseases INTEGER DEFAULT NULL,"+
            "username VARCHAR(250),"+
            "foreign key (id_diseases) references diseases (id_diseases)," +
            "foreign key (username) references User_system (username)" +
            ")";

    private String createSymptonsDiseases="create table symptoms_diseases"+
            "("+
            "id_sympdiseases INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "id_diseases INTEGER,"+
            "id_symptom INTEGER,"+
            "foreign key (id_diseases) references diseases (id_diseases)," +
            "foreign key (id_symptom) references symptoms (id_symptom)" +
            ")";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createDiseases_category);
        db.execSQL(createDiseases);
        db.execSQL(createCountry);
        db.execSQL(createAllergies);
        db.execSQL(createBloodtype);
        db.execSQL(createDatabaseVersion);
        db.execSQL(createSymptons);
        db.execSQL(createUserSystem);
        db.execSQL(createMedicalHistory);
        db.execSQL(createSymptonsDiseases);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS symptoms_diseases");
        db.execSQL("DROP TABLE IF EXISTS diseases");
        db.execSQL("DROP TABLE IF EXISTS symptoms");
        db.execSQL("DROP TABLE IF EXISTS diseases_category");
        db.execSQL(createDiseases_category);
        db.execSQL(createDiseases);
        db.execSQL(createSymptons);
        db.execSQL(createSymptonsDiseases);

    }
}
