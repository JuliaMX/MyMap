package ru.photosides.mymap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavouritesActivity extends ActionBarActivity implements MyFavouritePlaceHolder.Callbacks {

    private RecyclerView recyclerViewFavouritePlaces;
    private Dialog dialogDeleteAllFavourites;
    private PlacesLogic placesLogic;

    //    private RecyclerView recyclerViewFavouritePlaces;
    private ArrayList<MyPlace> placesArrayList = new ArrayList<>();
    private static final String TAG = "myLogs";
    private View relativeLayoutFavourites;


    @Override
    protected void onStop() {
        super.onStop();
        placesLogic.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        placesLogic = new PlacesLogic(this);
        placesLogic.open();


        Log.d(TAG, "Favourites Activity");

        relativeLayoutFavourites = (RelativeLayout) findViewById(R.id.relativeLayoutFavourites);

        refreshRecyclerView();

    }

    private void refreshRecyclerView(){
        placesArrayList = placesLogic.getFavouritePlaces();
        recyclerViewFavouritePlaces = (RecyclerView) findViewById(R.id.recyclerViewFavouritePlaces);
        recyclerViewFavouritePlaces.setLayoutManager(new LinearLayoutManager(this));
        MyFavouritePlaceAdapter myFavouritePlaceAdapter = new MyFavouritePlaceAdapter(this, placesArrayList, this);
        recyclerViewFavouritePlaces.setAdapter(myFavouritePlaceAdapter);
    }

    @Override
    public void onClick(MyPlace myPlace) {
        Log.d(TAG, "on click favourites");
        Intent intent = new Intent();
        intent.putExtra("place", myPlace);
        //startActivity(intent);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onLongClick(final MyPlace myPlace, View v) {

        Log.d(TAG, "ONLONG CLICK activity Inflate menu");

        PopupMenu popup = new PopupMenu(v.getContext(), v);
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Share")) {
                            Log.d(TAG, "menu item clicked in Favourites Activity: " + item.getTitle());

                            //Transfer to favorite

                            sharePlace(myPlace);

                        } else {

                            Log.d(TAG, "menu item clicked in Favourites Activity: " + item.getTitle());

                            new AlertDialog.Builder(FavouritesActivity.this)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setTitle("Delete Place")
                                    .setMessage("Are you sure?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            placesLogic.deleteFavouritePlace(myPlace);
                                            refreshRecyclerView();
                                        }
                                    })
                                    .setNegativeButton("No", null)
                                    .show();
                        }
                        return true;
                    }
                }
        );
        popup.inflate(R.menu.menu_popup_favourites);
        popup.show();

        return false;
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
        refreshRecyclerView();
    }
}
