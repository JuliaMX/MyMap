package ru.photosides.mymap;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements SeekBar.OnSeekBarChangeListener,
        SearchFragment.OnUpdatePlacesListListener {


    private LinearLayout linearLayout;
    private static final String TAG = "myLogs";
    private int power;
    private MapFragment mapFragment;
    private SearchFragment searchFragment;
    private Dialog dialogDeleteAllFavourites;
    private Dialog dialogRange;
    private TextView textViewRange;
    private SeekBar seekBarRange;
    private RadioButton radioButtonKm;
    private RadioButton radioButtonMiles;
    private boolean phone;
    private PlacesLogic placesLogic;
    public static int userRange = 1;
    public static String userUnit = "km";
    public static double userLatitude;
    public static double userLongitude;
    private boolean first_run = true;
    private ArrayList<LatLng> pointsArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "Internet connection is not available, working in offline mode", Toast.LENGTH_LONG).show();
        }

        registerReceiver(batInfoReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));

        LocationManager locationManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "we don't have perms to locate");
            // TODO: Consider calling
            //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        Location getLastLocation = locationManager.getLastKnownLocation
                (LocationManager.PASSIVE_PROVIDER);
        userLongitude = getLastLocation.getLongitude();
        userLatitude = getLastLocation.getLatitude();
        Log.d(TAG, "last known location: " + userLongitude + " " + userLatitude);

        placesLogic = new PlacesLogic(this);
        placesLogic.open();



        //readSharedPreferences();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayoutMainActivity);
        phone = linearLayout.getTag().toString().equals("telephone");

        if (phone) {

            Log.d(TAG, "IF TAG TELEPHONE");
        }

        mapFragment = (MapFragment)
                getSupportFragmentManager().findFragmentByTag("map_id");


        if (mapFragment != null) {
            Log.d(TAG, "MAP FRAGMENT EXIST");

            return;
        } else {


            if (phone) {


            } else {


                mapFragment = new MapFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frameLayoutContainerMap, mapFragment, "map_id")
                        .commit();

            }

        }
        if (searchFragment != null) {
            return;
        } else {


            searchFragment = new SearchFragment();
            Log.d(TAG, "New Search Fragment");


            if (phone) {
                Log.d(TAG, "IF TAG TELEPHONE create search frag");

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frameLayoutContainer, searchFragment, "search_id")
                        .commit();

            } else {

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.frameLayoutContainerSearch, searchFragment, "search_id")
                        .commit();
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onResume() {
        super.onResume();

/*
        Intent intent = getIntent();
        if (intent != null) {
            MyPlace myPlace = intent.getParcelableExtra("place");
            intent.removeExtra("place");
            if (myPlace != null) {
                Log.d(TAG, "onresume main: " + myPlace.toString());
                showOnePlace(myPlace);
            }
        }
*/


        userUnit = getMetricFromSettings();
        userRange = getRangeFromSettings();
        //placesLogic.open();

        if (!isNetworkAvailable()) {
            searchFragment = (SearchFragment)
                    getSupportFragmentManager().findFragmentByTag("search_id");

            if (searchFragment != null) {
                searchFragment.loadOfflinePlaces(placesLogic.getCachedPlaces());
            }
        } else {
            if (isPhone()) {
                searchFragment = (SearchFragment)
                        getSupportFragmentManager().findFragmentByTag("search_id");

                if (searchFragment != null) {
                    if (first_run) {
                        searchFragment.defaultSearch();
                        first_run = false;
                    }
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //placesLogic.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.deleteAllFavourites:

                dialogDeleteAllFavourites = new Dialog(this);
                dialogDeleteAllFavourites.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogDeleteAllFavourites.setContentView(R.layout.dialog_delete_all_favourites);
                dialogDeleteAllFavourites.setCancelable(false);
                dialogDeleteAllFavourites.show();

                return true;

            case R.id.exit:
                finish();
                return true;

            case R.id.action_settings:

                dialogRange = new Dialog(this);
                dialogRange.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogRange.setContentView(R.layout.dialog_range);
                dialogRange.show();

                radioButtonKm = (RadioButton) dialogRange.findViewById(R.id.radioButtonKm);
                radioButtonMiles = (RadioButton) dialogRange.findViewById(R.id.radioButtonMiles);
                textViewRange = (TextView) dialogRange.findViewById(R.id.textViewRange);
                seekBarRange = (SeekBar) dialogRange.findViewById(R.id.seekBarRange);

                textViewRange.setText(String.valueOf(getRangeFromSettings()));

                seekBarRange.setProgress(getRangeFromSettings());
                seekBarRange.setOnSeekBarChangeListener(this);

//                SharedPreferences sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);


                switch (getMetricFromSettings()) {
                    case "km":
                        radioButtonKm.setChecked(true);
                        Log.d(TAG, "------KM-----");
                        break;
                    case "miles":
                        radioButtonMiles.setChecked(true);
                        Log.d(TAG, "------MILES-----");
                        break;
                }


                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

    }


    public void buttonDeleteAllFavouritesCancel_onClick(View view) {
        dialogDeleteAllFavourites.dismiss();
    }

    public void buttonDeleteAllFavourites_onClick(View view) {
        dialogDeleteAllFavourites.dismiss();
        //delete!!!!
        placesLogic.deleteAllFavourites();
        Toast.makeText(this, "Deleted ALL!!!!!", Toast.LENGTH_SHORT).show();
    }

    public void buttonRangeOk_onClick(View view) {


        if (radioButtonKm.isChecked()) {
            userUnit = "km";
        } else {
            userUnit = "miles";
        }


        saveToSharedPreferences(seekBarRange.getProgress(), userUnit);
        Toast.makeText(this, "Your search radius " + seekBarRange.getProgress() + " " + userUnit, Toast.LENGTH_SHORT).show();
        dialogRange.dismiss();

    }

    public void buttonRangeCancel_onClick(View view) {
        dialogRange.dismiss();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textViewRange.setText(String.valueOf(seekBarRange.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        textViewRange.setText(String.valueOf(seekBarRange.getProgress()));

    }

    /*public int getRange() {

        USER_RANGE = seekBarRange.getProgress();
        textViewRange.setText(String.valueOf(USER_RANGE));

        return USER_RANGE;
    }*/

    public void updateCache(ArrayList<MyPlace> placeArrayList) {
        placesLogic.deleteCachedPlaces();
        placesLogic.addCachedPlaces(placeArrayList);
    }


    @Override
    public void onSearchEnded(ArrayList<MyPlace> placeArrayList) {
        MapFragment mapFragment2 = new MapFragment();
        if (placeArrayList.size() != 0) {

            //Save Search result to Cache table in DB
            updateCache(placeArrayList);

            pointsArray = new ArrayList<>();
            for (int i = 0; i < placeArrayList.size(); i++) {
                LatLng placeLocation = new LatLng(placeArrayList.get(i).getLat(), placeArrayList.get(i).getLng());
                pointsArray.add(placeLocation);
                Log.d(TAG, "create pointsArray after search ended");
            }


            if (phone) {

             /*   Log.d(TAG, "Try to start mapActivity");

                Intent intent = new Intent(this, MapActivity.class);
                intent.putParcelableArrayListExtra("pointsArray", pointsArray);
                startActivity(intent);
*/

            } else {


                mapFragment2 = (MapFragment)
                        getSupportFragmentManager().findFragmentByTag("map_id");


                if (mapFragment2 != null) {

                    mapFragment2.drawArrayMarker(pointsArray);

                    Log.d(TAG, "Map fragment exist");


                } else {

                    Log.d(TAG, "MapFragment not exist");
                }

            }

        } else {

            Toast.makeText(this, "Not Found Anything!", Toast.LENGTH_LONG).show();
        }

    }


    public boolean isPhone() {
        return phone;
    }

    public void showOnePlace(MyPlace myPlace) {
        MapFragment mapFragmentMarker = new MapFragment();

        if (phone) {
            Log.d(TAG, "Try to start mapActivity");
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("myplace", myPlace);
            startActivity(intent);
        } else {
            mapFragmentMarker = (MapFragment)
                    getSupportFragmentManager().findFragmentByTag("map_id");

            if (mapFragmentMarker != null) {
                mapFragmentMarker.drawMarker(myPlace);
                Log.d(TAG, "Map fragment exist");
            } else {
                Log.d(TAG, "MapFragment not exist");
            }
        }
    }


    public void addToFavouritesDB(MyPlace myPlace) {
        Log.d(TAG, "add to favourites:" + myPlace.toString());
        placesLogic.addFavouritePlace(myPlace);
    }


    public void saveToSharedPreferences(int userRange, String unit) {
        Log.d(TAG, "MODE: " + MODE_PRIVATE);

        SharedPreferences sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        MainActivity.userRange = userRange;
        userUnit = unit;
        editor.putInt("Range", userRange);
        editor.putString("Unit", unit);
        editor.commit();
        Log.d(TAG, "PUT range: " + userRange + ", unit: " + unit);

    }

    public int getRangeFromSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        Log.d(TAG, "getRange: " + sharedPreferences.getInt("Range", 1));
        if (sharedPreferences.contains("Range")) {
            return sharedPreferences.getInt("Range", 1);
        }else{ return userRange;}
    }

    public String getMetricFromSettings() {
        SharedPreferences sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        Log.d(TAG, "getMetric: unit = " + sharedPreferences.getString("Unit", null));
        if (sharedPreferences.contains("Unit")) {
            return sharedPreferences.getString("Unit", null);
        }else {return userUnit;}
    }

/*    public void getSharedPreferences() {

        SharedPreferences sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        USER_RANGE = sharedPreferences.getInt("Range", USER_RANGE);
        unit = sharedPreferences.getString("Unit", unit);
        Log.d(TAG, "GET range: " + USER_RANGE + ", unit: " + unit);

    }*/

    public void clearSharedPreferences() {

        SharedPreferences preferences = getSharedPreferences("PREFERENCE", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
        Log.d(TAG, "SharedPreferences was deleted");

    }

/*
    public void readSharedPreferences() {

        SharedPreferences sharedPreferences = getSharedPreferences("MainActivity", MODE_PRIVATE);
        if (sharedPreferences.contains("Range")) {

            getSharedPreferences();


        } else {

            USER_RANGE = 1;
            unit = "km";

        }

    }
*/


    @Override
    protected void onDestroy() {
        super.onDestroy();
        placesLogic.close();
        //clearSharedPreferences();

    }

    public void onColdStart() {
        Log.d(TAG, "main activity on map ready");
        if (isNetworkAvailable()) {
            searchFragment = (SearchFragment)
                    getSupportFragmentManager().findFragmentByTag("search_id");

            if (searchFragment != null) {
                searchFragment.defaultSearch();
            }
        }
    }

    public void toFavourites() {
        Intent intent = new Intent(this, FavouritesActivity.class);
        startActivityForResult(intent, 1);
    }

    public void addToFavourites(MyPlace myPlace) {
        addToFavouritesDB(myPlace);
        toFavourites();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            MyPlace myPlace = (MyPlace) data.getParcelableExtra("place");
            Log.d(TAG, "on click favourites =>>>activity result in Main : " + myPlace.toString());
            showOnePlace(myPlace);
        }
    }


    private BroadcastReceiver batInfoReceiver = new BroadcastReceiver() {
        @Override
        //When Event is published, onReceive method is called
        public void onReceive(Context c, Intent i) {

            int charge = i.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            if (power != charge) {
                if (charge == 0) {
                    Toast.makeText(MainActivity.this, "Battery discharged", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Battery charged", Toast.LENGTH_SHORT).show();
                }
            }
            power = charge;

        }

    };

}
