package db;

/**
 * Created by admin on 4/6/17.
 */
public class Symptom implements Comparable<Symptom>{
    private static final String TAG = Symptom.class.getSimpleName();
    /*
        Atributos
    */
    private String id_symptom;
    private String symptom;

    public Symptom (String id_symptom,
                    String symptom
    ) {
        this.id_symptom = id_symptom;
        this.symptom=symptom;

    }

    public String getid_symptom() {
        return id_symptom;
    }

    public void setid_symptom(String id_symptom) {
        this.id_symptom = id_symptom;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    /**
     * Compara los atributos de dos metas
     *
     * @param symptom Meta externa
     * @return true si son iguales, false si hay cambios
     */
    public boolean compararCon(Symptom symptom) {
        return this.symptom.compareTo(symptom.symptom) == 0;

    }
    @Override
    public int compareTo(Symptom o) {
        return this.symptom.compareTo(o.getSymptom());
    }
}
