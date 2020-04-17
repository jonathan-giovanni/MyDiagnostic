package db;

/**
 * Created by admin on 30/6/17.
 */

public class MedicalHistory {
    String id_medicalhistory;
    String title;
    String description;
    String name_disease;
    String id_diseases;
    String username;
    String date_time;

    public MedicalHistory(String id_medicalhistory, String title, String description, String id_diseases, String username, String date_time) {
        this.id_medicalhistory = id_medicalhistory;
        this.title = title;
        this.description = description;
        this.id_diseases = id_diseases;
        this.username = username;
        this.date_time = date_time;
    }

    public String getId_medicalhistory() {
        return id_medicalhistory;
    }

    public void setId_medicalhistory(String id_medicalhistory) {
        this.id_medicalhistory = id_medicalhistory;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName_disease() {
        return name_disease;
    }

    public void setName_disease(String name_disease) {
        this.name_disease = name_disease;
    }

    public String getId_diseases() {
        return id_diseases;
    }

    public void setId_diseases(String id_diseases) {
        this.id_diseases = id_diseases;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
