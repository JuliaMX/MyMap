package ru.photosides.mymap;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MapActivity extends AppCompatActivity {

    private Fragment mapFragment;
    private static final String TAG = "myLogs";
    private ArrayList<LatLng> pointsArray;
    private MyPlace myPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

/*        linearLayout = (LinearLayout) findViewById(R.id.as);

        if (!linearLayout.getTag().toString().equals("telephone")) {
            finish();
            return;

        }*/
        Intent intent = getIntent();
        pointsArray = new ArrayList<>();
        pointsArray = intent.getParcelableArrayListExtra("pointsArray");

     //   Log.d(TAG, "=============In Map Activity pointsArray: " + pointsArray.toString() + "==================");

        myPlace = new MyPlace();
        myPlace = intent.getParcelableExtra("myplace");
  //      Log.d(TAG, "=============In Map Activity myplace: " + myPlace.toString() + "==================");
        intent.removeExtra("pointsArray");
        intent.removeExtra("myplace");


        if (savedInstanceState == null) {

            mapFragment = new MapFragment();
         //   mapFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.frameMapLayoutContainerMap, mapFragment, "map_id")
                    .commit();
        }

        MapFragment mapFragment2 = new MapFragment();

        mapFragment2 = (MapFragment)
                getSupportFragmentManager().findFragmentByTag("map_id");

        Log.d(TAG, "search map fragment within mapActivity");


        if (mapFragment2 != null) {

            Log.d(TAG, "WE HAVE MAP!!!");

            mapFragment2.googleMap.clear();


            if (pointsArray != null) {
                mapFragment2.drawArrayMarker(pointsArray);
            }else{
                if (myPlace != null){mapFragment2.drawMarker(myPlace);}
            }
        }
    }

    @Override
    protected void onResume() {
        MapFragment mapFragment2 = new MapFragment();
        mapFragment2 = (MapFragment)
                getSupportFragmentManager().findFragmentByTag("map_id");

        Log.d(TAG, "search map fragment within mapActivity ONRESUME");

        if (mapFragment2 != null) {

            Log.d(TAG, "ONRESUME WE HAVE MAP!!!");

            if (pointsArray != null) {
                mapFragment2.drawArrayMarker(pointsArray);
            }else{
                if (myPlace != null){mapFragment2.drawMarker(myPlace);}
            }
        }
        super.onResume();
    }

}
