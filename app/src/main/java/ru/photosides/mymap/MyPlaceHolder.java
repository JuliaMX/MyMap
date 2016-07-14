package ru.photosides.mymap;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyPlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private Activity activity;
    private ImageView imageViewPlace;
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewDistance;
    private LinearLayout linearLayout;
    private Callbacks callbacks;
    private MyPlace myPlace;
    private static final String TAG = "myLogs";

    public MyPlaceHolder(View linearLayout, Callbacks callbacks) {

        super(linearLayout);

        this.linearLayout = (LinearLayout) linearLayout;

        imageViewPlace = (ImageView) linearLayout.findViewById(R.id.imageViewPlace);
        textViewName = (TextView) linearLayout.findViewById(R.id.textViewName);
        textViewAddress = (TextView) linearLayout.findViewById(R.id.textViewAddress);
        textViewDistance = (TextView) linearLayout.findViewById(R.id.textViewDistance);
        imageViewPlace = (ImageView) linearLayout.findViewById(R.id.imageViewPlace);
        this.callbacks = callbacks;
        linearLayout.setOnClickListener(this);
        linearLayout.setOnLongClickListener(this);


    }

    // Enter the object data into the ui:
    public void bindMyPlace(MyPlace myPlace) {

        textViewName.setText(myPlace.getName());
        textViewAddress.setText(myPlace.getVicinity());
        if (myPlace.getPhoto() != null) {
            imageViewPlace.setImageBitmap(myPlace.getPhoto());
        } else {
            imageViewPlace.setImageResource(R.drawable.no_photo_small);
        }

        switch (MainActivity.userUnit) {

            case "km":

                if (myPlace.getDistance() < 1000) {
                    textViewDistance.setText(String.valueOf(myPlace.getDistance()) + " m");
                } else {
                    textViewDistance.setText(String.format("%.2f", myPlace.getDistance() / 1000) + " km");
                }
                break;

            case "miles":

                textViewDistance.setText(String.format("%.2f", myPlace.getDistance() / 1609.3) + " miles");

        }
        this.myPlace = myPlace;
    }


    @Override
    public void onClick(View v) {

        Log.d(TAG, "onclick in holder");

        callbacks.onClick(myPlace);


    }

    @Override
    public boolean onLongClick(View v) {
        Log.d(TAG, "longClick in holder");


        callbacks.onLongClick(myPlace, v);

        return true;
    }


    public interface Callbacks {
        void onClick(MyPlace myPlace);

        boolean onLongClick(MyPlace myPlace, View v);
    }


}

