package ru.photosides.mymap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Julia on 2/19/2016.
 */
public class ImageDownloadAsyncTask extends AsyncTask<URL, Void, PhotoMap> {
    private Activity activity;
    private static final String TAG = "myLogs";
    Bitmap bitmap = null;
    private ProgressDialog dialog;
    private URL url;

    public ImageDownloadAsyncTask(Activity activity) {
        this.activity = activity;
    }


    protected void onPreExecute() {
    }

    @Override
    protected PhotoMap doInBackground(URL... params) {
        PhotoMap photoMap = new PhotoMap();
        try {
            url = params[0];

            Uri uri= Uri.parse(url.toString());
            String photoReference = uri.getQueryParameter("photoreference");
            Log.d(TAG, "REFERENCE: " + photoReference);


            // Starting image download
            bitmap = downloadImage(url);

            photoMap.setPhotoReference(photoReference);
            photoMap.setPhoto(bitmap);
        } catch (Exception e) {
            Log.d(TAG, "Download Image Exception: " + e.toString());
        }
        return photoMap;

    }

    @Override
    protected void onPostExecute(PhotoMap photoMap) {

    }

    private Bitmap downloadImage(URL url) throws IOException {
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{

            Log.d(TAG, "Download Image URL: " + url.toString());

            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        }catch(Exception e){
            Log.d(TAG, "Exception while downloading url: " + e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }
}