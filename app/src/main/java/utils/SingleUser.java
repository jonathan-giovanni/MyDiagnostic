package utils;

import android.graphics.Bitmap;

import java.util.Date;

/**
 * Created by admin on 13/5/17.
 */

public class SingleUser {
    private static SingleUser instance = null;

    private String name;
    private String id;
    private Date birthday;
    private String country;
    private Bitmap profilePicture;


    protected SingleUser(){}

    public static SingleUser getInstance(){
        if(instance==null){
            instance = new SingleUser();
        }
        return instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}
