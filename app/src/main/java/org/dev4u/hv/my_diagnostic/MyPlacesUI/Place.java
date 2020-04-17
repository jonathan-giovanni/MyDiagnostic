package org.dev4u.hv.my_diagnostic.MyPlacesUI;

import android.location.Location;


public class Place {

    private Location location;

    private String icon;

    private String name;

    private String placeId;

    private String[] types;

    private String vicinity;

    public Place() {
    }

    private Place(Builder builder) {
        setLocation(builder.location);
        setIcon(builder.icon);
        setName(builder.name);
        setPlaceId(builder.placeId);
        setTypes(builder.types);
        setVicinity(builder.vicinity);
    }

    @Override
    public String toString() {
        return "Place{" +
                "location=" + location +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getPlaceId() {
        return placeId;
    }


    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String[] getTypes() {
        return types;
    }


    public void setTypes(String[] types) {
        this.types = types;
    }


    public String getVicinity() {
        return vicinity;
    }


    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getLatitude(){
        return location.getLatitude();
    }

    public double getLongitude(){
        return location.getLongitude();
    }
    public static final class Builder {
        private Location location;
        private String icon;
        private String name;
        private String placeId;
        private String[] types;
        private String vicinity;

        public Builder() {
        }

        public Builder location(Location val) {
            location = val;
            return this;
        }

        public Builder icon(String val) {
            icon = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder placeId(String val) {
            placeId = val;
            return this;
        }



        public Builder vicinity(String val) {
            vicinity = val;
            return this;
        }

        public Place build() {
            return new Place(this);
        }
    }
}
