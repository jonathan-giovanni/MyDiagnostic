package db;

/**
 * Created by admin on 27/6/17.
 */

public class Country implements Comparable<Country>{

    String id_country;
    String name_country;
    String short_name;

    public Country(String id_country, String name_country, String short_name) {
        this.id_country = id_country;
        this.name_country = name_country;
        this.short_name = short_name;
    }

    public String getId_country() {
        return id_country;
    }

    public void setId_country(String id_country) {
        this.id_country = id_country;
    }

    public String getName_country() {
        return name_country;
    }

    public void setName_country(String name_country) {
        this.name_country = name_country;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    @Override
    public int compareTo(Country o) {
        return 0;
    }
}
