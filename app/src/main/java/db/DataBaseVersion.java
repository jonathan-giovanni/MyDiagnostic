package db;

/**
 * Created by admin on 12/7/17.
 */

public class DataBaseVersion {
    String id_version;
    String version;
    String To_date;

    public DataBaseVersion(String id_version, String version, String to_date) {
        this.id_version = id_version;
        this.version = version;
        To_date = to_date;
    }

    public String getId_version() {
        return id_version;
    }

    public void setId_version(String id_version) {
        this.id_version = id_version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTo_date() {
        return To_date;
    }

    public void setTo_date(String to_date) {
        To_date = to_date;
    }
}
