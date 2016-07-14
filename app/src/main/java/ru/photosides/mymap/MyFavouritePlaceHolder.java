package ru.photosides.mymap;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.cast.CastRemoteDisplayLocalService;

public class MyFavouritePlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private Activity activity;
    private ImageView imageViewPlace;
    private TextView textViewName;
    private TextView textViewAddress;
    private TextView textViewDistance;
    private LinearLayout linearLayout;
    private Callbacks callbacks;
    private MyPlace myPlace;
    private static final String TAG = "myLogs";

    public MyFavouritePlaceHolder(View linearLayout, Callbacks callbacks) {

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
        }else{
            imageViewPlace.setImageResource(R.drawable.no_photo_small);
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

