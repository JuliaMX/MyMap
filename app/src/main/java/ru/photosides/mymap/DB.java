package ru.photosides.mymap;


import android.graphics.Bitmap;

import java.lang.ref.SoftReference;

/**
 * Created by Julia on 2/24/2016.
 */
public class DB {

    public static final String NAME = "MyPlacesDatabase.db";
    public static final int VERSION = 1;

    public static class Places {


        public static final String ID = "id";
        public static final String CACHE_TABLE_NAME = "search_cache"; // table name for search
        public static final String FAVOURITES_TABLE_NAME = "favourites"; // table name for search
        public static final String PLACE_ID = "place_id";
        public static final String NAME = "name";
        public static final String LAT = "lat";
        public static final String LNG = "lng";
        public static final String VICINITY = "vicinity";
        public static final String PHOTO = "photo";

        public static final String[] ALL_COLUMNS = new String[] { PLACE_ID, NAME, LAT, LNG, VICINITY, PHOTO };

        public static final String CACHE_CREATION_STATEMENT = "CREATE TABLE " + CACHE_TABLE_NAME +
                " ( " + ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLACE_ID + " TEXT, " +
                NAME + " TEXT, " +
                LAT + " TEXT, " +
                LNG + " TEXT, " +
                VICINITY + " TEXT, " +
                PHOTO + " BLOB )";

        public static final String FAVOURITES_CREATION_STATEMENT = "CREATE TABLE " + FAVOURITES_TABLE_NAME +
                " ( " + ID  + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PLACE_ID + " TEXT, " +
                NAME + " TEXT, " +
                LAT + " TEXT, " +
                LNG + " TEXT, " +
                VICINITY + " TEXT, " +
                PHOTO + " BLOB )";

        public static final String CACHE_DELETION_STATEMENT = "DROP TABLE IF EXISTS " + CACHE_TABLE_NAME;
        public static final String FAVOURITES_DELETION_STATEMENT = "DROP TABLE IF EXISTS " + FAVOURITES_TABLE_NAME;


    }


}
