package ru.photosides.mymap;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {


    private View rootView;
    private LinearLayout linearLayout;
    private boolean phone;
    private MarkerOptions markerOptions;
    private boolean firstTime = true;
    protected GoogleMap googleMap;
    private static final String TAG = "myLogs";
    public MyPlace ARG_PLACE;
    private ArrayList<LatLng> points = new ArrayList<>();

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated MAP_FRAGMENT");
        setRetainInstance(true);

        if (savedInstanceState != null) {
            Log.d(TAG, "onActivityCreated MAP_FRAGMENT savedInstanceState !=null");
            if (savedInstanceState.containsKey("points")) {
                Log.d(TAG, "onActivityCreated MAP_FRAGMENT have points key");
                points = savedInstanceState.getParcelableArrayList("points");
/*
                if (points != null) {
                    Log.d(TAG, "onActivityCreated MAP_FRAGMENT try to run drawArrayMarker");
                    drawArrayMarker(points);
                }
*/
            }
        }
//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        if (container == null) {
            return null;
        }


        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        return rootView;
    }

    public void onResume() {
        Log.d(TAG, "onResume");

        SupportMapFragment map = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap));
        map.getMapAsync(this);

        markerOptions = new MarkerOptions();
        markerOptions.title("You are here...");

        super.onResume();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;


        Log.d(TAG, "onMapReady");
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            public void onMyLocationChange(Location location) {
                MainActivity.userLatitude = location.getLatitude();
                MainActivity.userLongitude = location.getLongitude();
                LatLng userLocation = new LatLng(MainActivity.userLatitude, MainActivity.userLongitude);

                changeMyLocation(userLocation);


            }
        });


        if (points.size() != 0) {
            drawArrayMarker(points);
        }

    }

    public void changeMyLocation(LatLng userLocation) {

        markerOptions.position(userLocation);
        googleMap.addMarker(markerOptions);

        if (firstTime) {

            int zoom = 13;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, zoom);
            googleMap.animateCamera(cameraUpdate);
            firstTime = false;

            if (!getActivity().getClass().getSimpleName().equals("MapActivity")) {
                ((MainActivity) getActivity()).onColdStart();
            }
        }
    }


    public void drawMarker(MyPlace myPlace) {
        LatLng placeLocation = new LatLng(myPlace.getLat(), myPlace.getLng());

        points = new ArrayList<LatLng>();
        points.add(placeLocation);

        Log.d(TAG, "MapActivity draw marker: " + placeLocation.toString());

        if (googleMap == null) {
            Log.d(TAG, "googleMap not ready");
        } else {

            googleMap.clear();

            googleMap.addMarker(new MarkerOptions()
                    .position(placeLocation)
                    .title(myPlace.getName().toString()));

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(placeLocation, 15);
            googleMap.animateCamera(cameraUpdate);
        }
    }


    public void drawArrayMarker(ArrayList<LatLng> pointsArray) {
        points = new ArrayList<LatLng>(pointsArray);
        //points.clear();
        if (googleMap == null) {
            Log.d(TAG, "googleMap not ready");
        } else {

            googleMap.clear();
            //points = new ArrayList<LatLng>(pointsArray);

            for (int i = 0; i < pointsArray.size(); i++) {

                Log.d(TAG, "MapActivity draw marker: " + pointsArray.get(i).toString());


                googleMap.addMarker(new MarkerOptions()
                        .position(pointsArray.get(i)));

                if (pointsArray.size() == 1) {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pointsArray.get(i), 15);
                    googleMap.animateCamera(cameraUpdate);
                } else {
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pointsArray.get(i), 12);
                    googleMap.animateCamera(cameraUpdate);
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("points", points);
        Log.d(TAG, "onSaveInstanceState MAPFRAGMENT:" + points.toString());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    public void setPointsArray(ArrayList<LatLng> pointsArray) {

        this.points = pointsArray;
    }

}
