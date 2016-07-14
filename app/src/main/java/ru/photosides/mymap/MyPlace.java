package ru.photosides.mymap;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;


public class MyPlace implements Parcelable{

    protected String place_id;
    protected String name;
    protected String vicinity;
    protected double distance;
    protected double rating;
    protected double lat;
    protected double lng;
    protected String photoReference;
    protected Bitmap photo;


    public MyPlace() {
    }

    public MyPlace(String place_id, String name, String vicinity, double distance, double rating,
                   double lat, double lng, String photoReference, Bitmap photo) {
        this.place_id = place_id;
        this.name = name;
        this.vicinity = vicinity;
        this.distance = distance;
        this.rating = rating;
        this.lat = lat;
        this.lng = lng;
        this.photoReference = photoReference;
        this.photo = photo;
    }


    public MyPlace(String place_id, String name, String vicinity,
                   double lat, double lng, Bitmap photo) {
        this.place_id = place_id;
        this.name = name;
        this.vicinity = vicinity;
        this.lat = lat;
        this.lng = lng;
        this.photo = photo;
    }


    protected MyPlace(Parcel in) {
        place_id = in.readString();
        name = in.readString();
        vicinity = in.readString();
        distance = in.readDouble();
        rating = in.readDouble();
        lat = in.readDouble();
        lng = in.readDouble();
        photoReference = in.readString();
        photo = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<MyPlace> CREATOR = new Creator<MyPlace>() {
        @Override
        public MyPlace createFromParcel(Parcel in) {
            return new MyPlace(in);
        }

        @Override
        public MyPlace[] newArray(int size) {
            return new MyPlace[size];
        }
    };

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    @Override
    public String toString() {
        return String.format("%s Name, %s Vicinity", name, vicinity);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(place_id);
        dest.writeString(name);
        dest.writeString(vicinity);
        dest.writeDouble(distance);
        dest.writeDouble(rating);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(photoReference);
        dest.writeParcelable(photo, flags);
    }
}
