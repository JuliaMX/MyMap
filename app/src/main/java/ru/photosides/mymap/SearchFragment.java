package ru.photosides.mymap;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements MyPlaceHolder.Callbacks {

    private OnUpdatePlacesListListener updateListCallback;
    private View rootView;
    private static final String TAG = "myLogs";
    private URL url;

    private final int MAX_WIDTH = 200;
    private PlaceIdJSONParser placeIdJsonParser;
    private JSONObject jObject;

    private RecyclerView recyclerViewMyPlaces;
    private ArrayList<MyPlace> placesArrayList = new ArrayList<>();
    private MyPlace[] places;
    private MyPlaceAdapter myPlaceAdapter;
    private EditText editTextSearch;
    private int range;
    private String unit;

    @Override
    public void onClick(MyPlace myPlace) {
        Log.d(TAG, "onclick in search fragment" + myPlace.toString());

        ((MainActivity) getActivity()).showOnePlace(myPlace);


    }

    @Override
    public boolean onLongClick(final MyPlace myPlace, View v) {
        Log.d(TAG, "onLONGclick in search fragment" + myPlace.toString());

        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Add to Favourites")) {
                            Log.d(TAG, "menu item clicked in Search Activity: " + item.getTitle());

                            //Transfer to favorite

                            addToFavourites(myPlace);


                        } else {

                            Log.d(TAG, "menu item clicked in Search Activity: " + item.getTitle());
                            // Share place

                            sharePlace(myPlace);

                        }

                        return true;
                    }
                }
        );
        popup.inflate(R.menu.menu_popup_search);
        popup.show();

        return true;

    }

    public interface OnUpdatePlacesListListener {
        public void onSearchEnded(ArrayList<MyPlace> placesArrayList);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "On Attach");

        try {
            updateListCallback = (OnUpdatePlacesListListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerViewMyPlaces = (RecyclerView) rootView.findViewById(R.id.recyclerViewSearch);
        editTextSearch = (EditText) rootView.findViewById(R.id.editTextSearch);

        Button buttonAroundMe = (Button) rootView.findViewById(R.id.buttonAroundMe);
        buttonAroundMe.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                if (((MainActivity) getActivity()).isNetworkAvailable()) {
                    defaultSearch();
                }
            }
        });

        Button buttonTextSearch = (Button) rootView.findViewById(R.id.buttonTextSearch);
        buttonTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                range = ((MainActivity) getActivity()).getRangeFromSettings();
                unit = ((MainActivity) getActivity()).getMetricFromSettings();

                if (range != 0) {

                    if (editTextSearch.getText() != null) {

                        Log.d(TAG, "full text: " + editTextSearch.getText());

                        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
                        stringBuilder.append("&location=" + MainActivity.userLatitude + "," + MainActivity.userLongitude);
                        if (unit.equals("km")) {
                            stringBuilder.append("&radius=" + range * 1000);
                        } else {
                            stringBuilder.append("&radius=" + range * 1000 / 1.6093);
                        }
                        stringBuilder.append("&keyword=" + editTextSearch.getText().toString().replace(" ", "+"));
                        stringBuilder.append("&key=AIzaSyCnMUyDqKTrlzSQQTqFx09s_1RIHRBOkrE");

                        try {
                            url = new URL(stringBuilder.toString());
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                        Log.d(TAG, "URL full text: " + stringBuilder.toString());
                        if (((MainActivity) getActivity()).isNetworkAvailable()) {

                            searchPlaces(url);
                        }
                        } else {
                            Toast.makeText(getActivity(), "Please enter a text!", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        Toast.makeText(getActivity(), "You chose 0! Please set a radius!", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            );

            ImageButton imageButtonFavourites = (ImageButton) rootView.findViewById(R.id.imageButtonFavourites);
            imageButtonFavourites.setOnClickListener(new View.OnClickListener()

            {
                @Override
                public void onClick (View v){

//                Intent intent = new Intent(getActivity(), FavouritesActivity.class);
                //              startActivity(intent);
                ((MainActivity) getActivity()).toFavourites();


            }
            }

            );


            return rootView;
        }


    public void defaultSearch() {

        range = ((MainActivity) getActivity()).getRangeFromSettings();
        unit = ((MainActivity) getActivity()).getMetricFromSettings();

        if (range != 0) {
            editTextSearch.setText("");
            Log.d(TAG, "Around Me Button");

            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            stringBuilder.append("&location=" + MainActivity.userLatitude + "," + MainActivity.userLongitude);
            if (unit.equals("km")) {
                stringBuilder.append("&radius=" + range * 1000);
            }else{
                stringBuilder.append("&radius=" + range * 1000 / 1.6093);
            }
            stringBuilder.append("&key=AIzaSyCnMUyDqKTrlzSQQTqFx09s_1RIHRBOkrE");

            try {
                url = new URL(stringBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            Log.d(TAG, "URL: " + stringBuilder.toString());

            searchPlaces(url);
        } else {
            Toast.makeText(getActivity(), "You chose 0! Please set a radius!", Toast.LENGTH_SHORT).show();
        }
    }


    //-------------------------------------

    private void getDistance(ArrayList<MyPlace> placesArrayList) {
        Activity activity = getActivity();
        Log.d(TAG, "getDistance start");

        for (int i = 0; i < placesArrayList.size(); i++) {

            StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
            stringBuilder.append("&origin=" + MainActivity.userLatitude + "," + MainActivity.userLongitude);
            stringBuilder.append("&mode=" + "walking");
            stringBuilder.append("&destination=place_id:" + placesArrayList.get(i).getPlace_id());
            stringBuilder.append("&key=AIzaSyCnMUyDqKTrlzSQQTqFx09s_1RIHRBOkrE");

            try {
                url = new URL(stringBuilder.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "URL to get distance: " + stringBuilder.toString());

            new PlacesAsyncTask(activity) {

                @Override
                protected void onPostExecute(String result) {

                    super.onPostExecute(result);

                    DistanceJSONParser distanceJsonParser = new DistanceJSONParser();

                    DistanceMap distanceMap = new DistanceMap();

                    try {
                        jObject = new JSONObject(result);
                        distanceMap = distanceJsonParser.parse(jObject);


                    } catch (Exception e) {
                        Log.d("Exception", e.toString());
                    }

                    if (distanceMap.getDistance() != 0) {
                        Log.d(TAG, "distance to object: " + distanceMap.getDistance());

                        setDistanceToPlace(distanceMap);

                    } else {

                    }

                }

            }.execute(url);

        }

    }


    private void setDistanceToPlace(DistanceMap distanceMap) {

        for (int i = 0; i < placesArrayList.size(); i++) {


            if (placesArrayList.get(i).getPlace_id().equals(distanceMap.getPlace_id())) {
                Log.d(TAG, "set distance-place mapping: " + placesArrayList.get(i).getName() + "=" + distanceMap.getDistance());
                placesArrayList.get(i).setDistance(distanceMap.getDistance());
            }
        }
        updateRecyclerView();

    }


    private void getImagesForPlaces() {

        Activity activity = getActivity();
        Log.d(TAG, "getImages start");

        for (int i = 0; i < placesArrayList.size(); i++) {

            if (!placesArrayList.get(i).getPhotoReference().isEmpty()) {

                URL url = null;
                StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
                stringBuilder.append("&photoreference=" + placesArrayList.get(i).getPhotoReference());
                stringBuilder.append("&maxwidth=" + MAX_WIDTH);
                stringBuilder.append("&key=AIzaSyCnMUyDqKTrlzSQQTqFx09s_1RIHRBOkrE");

                try {
                    url = new URL(stringBuilder.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                //    Log.d(TAG, "URL to get Photo: " + stringBuilder.toString());

                new ImageDownloadAsyncTask(activity) {

                    @Override
                    protected void onPostExecute(PhotoMap photoMap) {
                        super.onPostExecute(photoMap);
                        Log.d(TAG, "SEARCHFRAGMENT Image downloaded");
                        setPhotoToPlace(photoMap);
                    }

                }.execute(url);

            }
        }
    }


    private void setPhotoToPlace(PhotoMap photoMap) {

        for (int i = 0; i < placesArrayList.size(); i++) {


            if (!placesArrayList.get(i).getPhotoReference().isEmpty()) {

                if (placesArrayList.get(i).getPhotoReference().equals(photoMap.getPhotoReference())) {
                    Log.d(TAG, "set photo-place mapping: " + placesArrayList.get(i).getName() + "=" + photoMap.getPhotoReference());
                    placesArrayList.get(i).setPhoto(photoMap.getPhoto());
                }
            }
        }
        updateRecyclerView();
        ((MainActivity) getActivity()).updateCache(placesArrayList);

    }


    //async task method for get places
    public void searchPlaces(URL url) {

        Activity activity = getActivity();
        Log.d(TAG, "search places method started");

        new PlacesAsyncTask(activity) {

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                places = null;
                placeIdJsonParser = new PlaceIdJSONParser();

                try {
                    jObject = new JSONObject(result);
                    /** Getting the parsed data as a List construct */
                    placesArrayList.clear();
                    placesArrayList = placeIdJsonParser.parse(jObject);

                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                }

                if (placesArrayList.size() != 0) {
                    //Log.d(TAG, "Object: " + places[0].getName().toString());

                    //============ fill distance ==================

                    getDistance(placesArrayList);

                    getImagesForPlaces();

                    //==============================

/*
                    placesArrayList.clear();
                    for (int i = 0; i < places.length; i++){
                        placesArrayList.add(places[i]);
                    }
*/

                } else {
                    placesArrayList.clear();
                }

                // Do something with result here
/*                setRecyclerListViewMyPlaces(placesArrayList);*/
                // update map
                updateListCallback.onSearchEnded(placesArrayList);
            }
        }.execute(url);
    }

    private void updateRecyclerView() {

/*        placesArrayList.clear();
        for (int i = 0; i < places.length; i++){
            placesArrayList.add(places[i]);
        }*/

        setRecyclerListViewMyPlaces(placesArrayList);

    }


    public void loadOfflinePlaces(ArrayList<MyPlace> placesArrayList) {
        this.placesArrayList = placesArrayList;
        setRecyclerListViewMyPlaces(placesArrayList);
    }

    public void setRecyclerListViewMyPlaces(ArrayList<MyPlace> placesArrayList) {
        recyclerViewMyPlaces.setLayoutManager(new LinearLayoutManager(getActivity()));
        myPlaceAdapter = new MyPlaceAdapter(getActivity(), placesArrayList, this);
        recyclerViewMyPlaces.setAdapter(myPlaceAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("search", placesArrayList);
        Log.d(TAG, "onSaveInstanceState SEARCH_FRAGMENT:" + placesArrayList.toString());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.d(TAG, "onActivityCreated SEARCH_FRAGMENT");
        setRetainInstance(true);

        if (savedInstanceState != null) {
            Log.d(TAG, "onActivityCreated SEARCH_FRAGMENT savedInstanceState !=null");
            if (savedInstanceState.containsKey("search")) {
                Log.d(TAG, "onActivityCreated SEARCH_FRAGMENT have search key");
                placesArrayList = savedInstanceState.getParcelableArrayList("search");
                updateRecyclerView();

            }
        }
    }

    public void sharePlace(MyPlace myPlace) {

        String url = "https://maps.google.com/maps?z=16&q=loc:" + myPlace.getLat() + "+" + myPlace.getLng();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Place: " + myPlace.getName() + ", \nlink: " + url);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        Log.d(TAG, url);

    }


    public void addToFavourites(MyPlace myPlace) {
        ((MainActivity) getActivity()).addToFavourites(myPlace);

/*        ((MainActivity) getActivity()).addToFavouritesDB(myPlace);

        Intent intent = new Intent(getActivity(), FavouritesActivity.class);
        intent.putExtra("myPlace", myPlace);
        startActivity(intent);*/

        //startActivityForResult(intent, 1);

    }

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            MyPlace myPlace = (MyPlace) data.getParcelableExtra("place");
            Log.d(TAG, "on click favourites =>>>activity result: " + myPlace.toString());

            //Do whatever you want with yourData
        }
    }
*/

}
