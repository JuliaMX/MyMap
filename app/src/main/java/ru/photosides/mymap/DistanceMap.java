package ru.photosides.mymap;

/**
 * Created by Julia on 2/19/2016.
 */
public class DistanceMap {

    protected String place_id;
    protected double distance;

    public DistanceMap() {
    }

    public DistanceMap(String place_id, double distance) {
        this.place_id = place_id;
        this.distance = distance;
    }


    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return String.format("%s place_id, %s distance", place_id, distance);
    }

}
