package db;

/**
 * Created by admin on 27/6/17.
 */

public class Bloodtype implements Comparable<Bloodtype>{
    String id_bloodtype;
    String bloodtype;

    public Bloodtype(String id_bloodtype, String bloodtype) {
        this.id_bloodtype = id_bloodtype;
        this.bloodtype = bloodtype;
    }

    public String getId_bloodtype() {
        return id_bloodtype;
    }

    public void setId_bloodtype(String id_bloodtype) {
        this.id_bloodtype = id_bloodtype;
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
    }

    @Override
    public int compareTo(Bloodtype o) {
        return 0;
    }
}
