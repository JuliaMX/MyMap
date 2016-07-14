package ru.photosides.mymap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class PlaceIdJSONParser {
    private static final String TAG = "myLogs";

    /**
     * Receives a JSONObject and returns a list of place_ids
     */
    public ArrayList<MyPlace> parse(JSONObject jObject) {

        JSONArray jPlaces = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jPlaces = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlacesIds with the array of json object
         * where each json object represent a place
         */
        return getPlacesId(jPlaces);
    }

    private ArrayList<MyPlace> getPlacesId(JSONArray jPlaces) {
        if (jPlaces != null) {


            int placesCount = jPlaces.length();
            MyPlace[] places = new MyPlace[placesCount];
            ArrayList <MyPlace> placeArr = new ArrayList<MyPlace>();
            /** Taking each place, parses and adds to list object */
            for (int i = 0; i < placesCount; i++) {
                try {
                    /** Call getPlace with place JSON object to parse the place */

                    MyPlace place = getPlaceId((JSONObject) jPlaces.get(i));

                    Log.d(TAG, "Parser JSON Places " + place.toString());

                    if (place.getName() != null) {
                        placeArr.add(place);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            for (int i=0; i < placeArr.size(); i++){
                Log.d(TAG, "ARRAY PLACES: " + placeArr.get(i).toString());
            }
            return placeArr;
        } else {
            return null;
        }
    }

    /**
     * Parsing the Place JSON object
     */
    private MyPlace getPlaceId(JSONObject jPlace) {

        MyPlace myPlace = new MyPlace();

        try {

            if (!jPlace.isNull("types")) {

                JSONArray temp = jPlace.getJSONArray("types");
                int length = temp.length();
                String[] types = new String[length];
                if (length > 0) {

                    for (int i = 0; i < length; i++) {
                        types[i] = temp.getString(i);
                    }
                }

                if (!Arrays.asList(types).contains("political")) {

                    // Extracting Place Vicinity, if available
                    if (!jPlace.isNull("place_id")) {
                        myPlace.place_id = jPlace.getString("place_id");
                        Log.d(TAG, "place_id: " + myPlace.getPlace_id());
                    }

                    // Extracting Place name, if available
                    if (!jPlace.isNull("name")) {
                        myPlace.name = jPlace.getString("name");
                        Log.d(TAG, "name: " + myPlace.getName());
                    }

                    if (!jPlace.isNull("vicinity")) {
                        myPlace.vicinity = jPlace.getString("vicinity");
                        Log.d(TAG, "vicinity: " + myPlace.getVicinity());
                    }

                    if (!jPlace.isNull("distance")) {
                        myPlace.distance = jPlace.getInt("distance");
                        Log.d(TAG, "distance: " + myPlace.getVicinity());
                    }

                    if (!jPlace.isNull("rating")) {
                        myPlace.rating = jPlace.getDouble("rating");
                        Log.d(TAG, "rating: " + myPlace.getRating());
                    }



                    if(!jPlace.isNull("photos")){
                        JSONArray photos = jPlace.getJSONArray("photos");
                        myPlace.photoReference = ((JSONObject)photos.get(0)).getString("photo_reference");

                    }else{
                        myPlace.photoReference = "";
                    }

                    myPlace.lat = Double.parseDouble(jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat"));
                    Log.d(TAG, "lat: " + myPlace.getLat());

                    myPlace.lng = Double.parseDouble(jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng"));
                    Log.d(TAG, "lng: " + myPlace.getLng());


                }

            }





            /*// Extracting Place Vicinity, if available
            if(!jPlace.isNull("place_id")){
                myPlace.place_id = jPlace.getString("place_id");
                Log.d(TAG, "place_id: " + myPlace.getPlace_id());
            }

            // Extracting Place name, if available
            if(!jPlace.isNull("name")){
                myPlace.name = jPlace.getString("name");
                Log.d(TAG, "name: " + myPlace.getName());
            }

            if(!jPlace.isNull("vicinity")) {
                myPlace.vicinity = jPlace.getString("vicinity");
                Log.d(TAG, "vicinity: " + myPlace.getVicinity());
            }

            if(!jPlace.isNull("distance")) {
                myPlace.distance = jPlace.getInt("distance");
                Log.d(TAG, "distance: " + myPlace.getVicinity());
            }

            if(!jPlace.isNull("rating")) {
                myPlace.rating = jPlace.getDouble("rating");
                Log.d(TAG, "rating: " + myPlace.getRating());
            }

            myPlace.lat = Double.parseDouble(jPlace.getJSONObject("geometry").getJSONObject("location").getString("lat"));
            Log.d(TAG, "lat: " + myPlace.getLat());

            myPlace.lng = Double.parseDouble(jPlace.getJSONObject("geometry").getJSONObject("location").getString("lng"));
            Log.d(TAG, "lng: " + myPlace.getLng());*/


    }

    catch(
    JSONException e
    )

    {
        e.printStackTrace();
        Log.d("EXCEPTION", e.toString());
    }

    return myPlace;
}

}