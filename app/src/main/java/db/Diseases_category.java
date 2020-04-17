package db;

/**
 * Created by admin on 25/6/17.
 */

public class Diseases_category implements Comparable<Diseases_category>{
    String id_disease_category;
    String category_name;
    String category_description;
    int diseasesNumber;
    public Diseases_category(String id_disease_category, String category_name, String category_description) {
        this.id_disease_category = id_disease_category;
        this.category_name = category_name;
        this.category_description = category_description;
    }

    public Diseases_category(String id_disease_category, String category_name, String category_description, int diseasesNumber) {
        this.id_disease_category = id_disease_category;
        this.category_name = category_name;
        this.category_description = category_description;
        this.diseasesNumber = diseasesNumber;
    }

    public String getId_disease_category() {
        return id_disease_category;
    }

    public void setId_disease_category(String id_disease_category) {
        this.id_disease_category = id_disease_category;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_description() {
        return category_description;
    }

    public void setCategory_description(String category_description) {
        this.category_description = category_description;
    }

    public int getDiseasesNumber() {
        return diseasesNumber;
    }

    public void setDiseasesNumber(int diseasesNumber) {
        this.diseasesNumber = diseasesNumber;
    }

    @Override
    public int compareTo(Diseases_category o) {
        return this.category_name.compareTo(o.getCategory_name());
    }
}
