package ru.photosides.mymap;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.support.v4.app.Fragment;

import java.util.ArrayList;


public class MyPlaceAdapter extends RecyclerView.Adapter<MyPlaceHolder> implements MyPlaceHolder.Callbacks{

    private Activity activity;
    private ArrayList<MyPlace> placesArrayList;
    private MyPlaceHolder.Callbacks callbacks;

    private static final String TAG = "myLogs";


    public MyPlaceAdapter(Activity activity, ArrayList<MyPlace> placesArrayList, MyPlaceHolder.Callbacks callbacks) {

        this.activity = activity;
        this.placesArrayList = placesArrayList;
        this.callbacks = callbacks;
    }

    // Will be invoked only for the first shown items!
    // Creates the first x layout items.

    @Override
    public MyPlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View linearLayout = layoutInflater.inflate(R.layout.item_place, parent, false);
        MyPlaceHolder myPlaceHolder = new MyPlaceHolder(linearLayout, this);
        return myPlaceHolder;
    }

    // Will be invoked for each item presented.
    // Here we will set the data to the layout:
    @Override
    public void onBindViewHolder(MyPlaceHolder myPlaceHolder, int position) {

        MyPlace myPlace = placesArrayList.get(position);
        myPlaceHolder.bindMyPlace(myPlace);
        myPlaceHolder.itemView.setSelected(true);


    }

    @Override
    public int getItemCount() {
        return placesArrayList.size();
    }

    @Override
    public void onClick(MyPlace myPlace) {

        callbacks.onClick(myPlace);
        Log.d(TAG, "onclick in adapter");




    }

    @Override
    public boolean onLongClick(MyPlace myPlace, View v) {

        callbacks.onLongClick(myPlace, v);
        Log.d(TAG, "onLONGclick in adapter");
        return true;
    }
}


