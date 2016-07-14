package ru.photosides.mymap;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DAL extends SQLiteOpenHelper {

    private SQLiteDatabase database;


    public DAL(Activity activity) {
        super(activity, DB.NAME, null, DB.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB.Places.CACHE_CREATION_STATEMENT);
        db.execSQL(DB.Places.FAVOURITES_CREATION_STATEMENT);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DB.Places.CACHE_DELETION_STATEMENT);
        db.execSQL(DB.Places.FAVOURITES_DELETION_STATEMENT);

        db.execSQL(DB.Places.CACHE_CREATION_STATEMENT);
        db.execSQL(DB.Places.FAVOURITES_CREATION_STATEMENT);

    }

    public void open() {
        database = getWritableDatabase();
    }


    public void close() {
        super.close();
    }

    public long insert(String tableName, ContentValues values) {

        long id = database.insert(tableName, null, values);
        return id;

    }

    public Cursor getTable(String tableName, String[] columns) {

        return database.query(tableName, columns, null, null, null, null, null);
    }

    public Cursor getTable(String tableName, String[] columns, String where) {

        return database.query(tableName, columns, where, null, null, null, null);
    }


    public long delete(String tableName, String where) {
        return database.delete(tableName, where, null);
    }

    public long deleteAll(String tableName) {
        return database.delete(tableName, null, null);
    }

}
