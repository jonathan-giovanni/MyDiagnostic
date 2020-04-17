package db;

/**
 * Created by admin on 14/6/17.
 */

public class SymptomDisease {
    private String id_sympdiseases;
    private String id_diseases;
    private String id_symptom;

    public SymptomDisease(String id_sympdiseases, String id_diseases, String id_symptom) {
        this.id_sympdiseases = id_sympdiseases;
        this.id_diseases = id_diseases;
        this.id_symptom = id_symptom;
    }

    public String getId_sympdiseases() {
        return id_sympdiseases;
    }

    public void setId_sympdiseases(String id_sympdiseases) {
        this.id_sympdiseases = id_sympdiseases;
    }

    public String getId_diseases() {
        return id_diseases;
    }

    public void setId_diseases(String id_diseases) {
        this.id_diseases = id_diseases;
    }

    public String getId_symptom() {
        return id_symptom;
    }

    public void setId_symptom(String id_symptom) {
        this.id_symptom = id_symptom;
    }
}
