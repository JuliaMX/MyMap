package ru.photosides.mymap;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

public class PlacesLogic extends BaseLogic {

    private static final String TAG = "myLogs";


    public PlacesLogic(Activity activity) {
        super(activity);
    }

    public long addFavouritePlace(MyPlace myPlace) {

        if (dal.getTable(DB.Places.FAVOURITES_TABLE_NAME, DB.Places.ALL_COLUMNS,
                new String(DB.Places.PLACE_ID + "='" + myPlace.getPlace_id()+ "'")).getCount() == 0 ) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DB.Places.PLACE_ID, myPlace.getPlace_id());
            contentValues.put(DB.Places.NAME, myPlace.getName());
            contentValues.put(DB.Places.VICINITY, myPlace.getVicinity());
            contentValues.put(DB.Places.LAT, myPlace.getLat());
            contentValues.put(DB.Places.LNG, myPlace.getLng());
            contentValues.put(DB.Places.PHOTO, DbBitmapUtility.getBytes(myPlace.getPhoto()));
            long id = dal.insert(DB.Places.FAVOURITES_TABLE_NAME, contentValues);
            return id;
        }
        return 0;
    }

    public void addCachedPlaces(ArrayList<MyPlace> places) {
        Log.d(TAG, "addCachedPlaces");
        for (int i = 0; i < places.size(); i++) {
            Log.d(TAG, "addCachedPlaces, place = " + places.get(i).toString());
            ContentValues contentValues = new ContentValues();
            contentValues.put(DB.Places.PLACE_ID, places.get(i).getPlace_id());
            contentValues.put(DB.Places.NAME, places.get(i).getName());
            contentValues.put(DB.Places.LAT, places.get(i).getLat());
            contentValues.put(DB.Places.LNG, places.get(i).getLng());
            contentValues.put(DB.Places.VICINITY, places.get(i).getVicinity());
            contentValues.put(DB.Places.PHOTO, DbBitmapUtility.getBytes(places.get(i).getPhoto()));
            long id = dal.insert(DB.Places.CACHE_TABLE_NAME, contentValues);
            Log.d(TAG, "inserted id=" + id);

        }
    }


    public long deleteFavouritePlace(MyPlace myPlace) {

        String where = DB.Places.PLACE_ID + "='" + myPlace.getPlace_id()+ "'";
        long affectedRows = dal.delete(DB.Places.FAVOURITES_TABLE_NAME, where);
        return affectedRows;

    }

    public long deleteAllFavourites() {
        long affectedRows = dal.deleteAll(DB.Places.FAVOURITES_TABLE_NAME);
        return affectedRows;
    }


    public long deleteCachedPlaces() {
        long affectedRows = dal.deleteAll(DB.Places.CACHE_TABLE_NAME);
        return affectedRows;
    }


    public ArrayList<MyPlace> getFavouritePlaces() {
        return getAllPlaces(DB.Places.FAVOURITES_TABLE_NAME);
    }

    public ArrayList<MyPlace> getCachedPlaces() {
        Log.d(TAG, "getCachedPlaces");

        return getAllPlaces(DB.Places.CACHE_TABLE_NAME);
    }

    private ArrayList<MyPlace> getAllPlaces(String tableName) {
        Log.d(TAG, "getAllPlaces from: " + tableName);

        ArrayList<MyPlace> places = new ArrayList<>();
        Cursor cursor = dal.getTable(tableName, DB.Places.ALL_COLUMNS);
        if (cursor.getCount() != 0) {
            Log.d(TAG, "Cursor:" + cursor.getCount());
            while (cursor.moveToNext()) {
                String place_id = cursor.getString(cursor.getColumnIndex(DB.Places.PLACE_ID));
                String name = cursor.getString(cursor.getColumnIndex(DB.Places.NAME));
                double lat = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DB.Places.LAT)));
                double lng = Double.parseDouble(cursor.getString(cursor.getColumnIndex(DB.Places.LNG)));
                String vicinity = cursor.getString(cursor.getColumnIndex(DB.Places.VICINITY));
                Bitmap photo = DbBitmapUtility.getImage(cursor.getBlob(cursor.getColumnIndex(DB.Places.PHOTO)));
                places.add(new MyPlace(place_id, name, vicinity, lat, lng, photo));
            }
            cursor.close();
        }

        Log.d(TAG, "Cursor is 0!");

        return places;
    }
}
