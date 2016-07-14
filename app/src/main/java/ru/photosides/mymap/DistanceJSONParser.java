package ru.photosides.mymap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class DistanceJSONParser {
    private static final String TAG = "myLogs";

    /**
     * Receives a JSONObject and returns a place_id and distance
     */
    public DistanceMap parse(JSONObject jObject) {

        JSONArray jGeocodedWaypoints = null;
        JSONArray jRoutes = null;
        try {
            /** Retrieves all the elements in the 'places' array */
            jGeocodedWaypoints = jObject.getJSONArray("geocoded_waypoints");
            jRoutes = jObject.getJSONArray("routes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /** Invoking getPlacesIds with the array of json object
         * where each json object represent a place
         */


        return new DistanceMap(getPlace_id(jGeocodedWaypoints), getDistance(jRoutes));
    }

/*
    private DistanceMap getDistanceMap(JSONArray jDistance) {
        if (jDistance != null) {


            DistanceMap distanceMap = new DistanceMap();

            try {
                distanceMap = getDistance((JSONObject) jDistance.get(0));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return distanceMap;
        } else {
            return null;
        }
    }

*/


    /**
     * Parsing the Place JSON object
     */
    private String getPlace_id(JSONArray jGeocodedWaypoins) {

        JSONObject jGeocodedWaypoint = new JSONObject();

        String place_id = new String();
        try {
            jGeocodedWaypoint = ((JSONObject) jGeocodedWaypoins.get(1));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            // Extracting Place_id, if available
            if (!jGeocodedWaypoint.isNull("place_id")) {
                place_id = jGeocodedWaypoint.getString("place_id");
                Log.d(TAG, "DISTANCE JSON PARSER place_id: " + place_id);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", e.toString());
        }

        return place_id;
    }


    private Double getDistance(JSONArray jRoutes) {

        JSONObject jRoute = new JSONObject();
        JSONArray jLegs = new JSONArray();
        JSONObject jLeg = new JSONObject();


        Double distance = new Double(0);

        try {
            jRoute = ((JSONObject) jRoutes.get(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            // Extracting distance if available
            if (!jRoute.isNull("legs")) {

                jLegs = jRoute.getJSONArray("legs");
                jLeg = ((JSONObject) jLegs.get(0));

                distance = Double.parseDouble(jLeg.getJSONObject("distance").getString("value"));
                Log.d(TAG, "DISTANCE JSON PARSER distance: " + distance);
            }


        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", e.toString());
        }

        return distance;
    }

}
