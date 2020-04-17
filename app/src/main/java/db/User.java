package db;

/**
 * Created by admin on 27/6/17.
 */

public class User {
    String username;
    String fullname;
    String genre;
    String birthday;
    String id_country;
    String id_bloodtype;
    String name_country;
    String name_bloodtype;

    public User(String username, String fullname, String genre, String birthday, String id_country, String id_bloodtype) {
        this.username = username;
        this.fullname = fullname;
        this.genre = genre;
        this.birthday = birthday;
        this.id_country = id_country;
        this.id_bloodtype = id_bloodtype;
    }

    public User(String username, String fullname, String genre, String birthday, String id_country, String id_bloodtype, String name_country, String name_bloodtype) {
        this.username = username;
        this.fullname = fullname;
        this.genre = genre;
        this.birthday = birthday;
        this.id_country = id_country;
        this.id_bloodtype = id_bloodtype;
        this.name_country = name_country;
        this.name_bloodtype = name_bloodtype;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getId_country() {
        return id_country;
    }

    public void setId_country(String id_country) {
        this.id_country = id_country;
    }

    public String getId_bloodtype() {
        return id_bloodtype;
    }

    public void setId_bloodtype(String id_bloodtype) {
        this.id_bloodtype = id_bloodtype;
    }

    public String getName_country() {
        return name_country;
    }

    public void setName_country(String name_country) {
        this.name_country = name_country;
    }

    public String getName_bloodtype() {
        return name_bloodtype;
    }

    public void setName_bloodtype(String name_bloodtype) {
        this.name_bloodtype = name_bloodtype;
    }
}
