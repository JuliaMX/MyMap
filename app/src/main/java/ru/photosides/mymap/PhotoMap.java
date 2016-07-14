package ru.photosides.mymap;

import android.graphics.Bitmap;

/**
 * Created by Julia on 2/19/2016.
 */
public class PhotoMap {

    protected String photoReference;
    protected Bitmap photo;

    public PhotoMap() {
    }

    public PhotoMap(String photoReference, Bitmap photo) {
        this.photoReference = photoReference;
        this.photo = photo;
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
        return String.format("%s photoReference", photoReference);
    }

}
