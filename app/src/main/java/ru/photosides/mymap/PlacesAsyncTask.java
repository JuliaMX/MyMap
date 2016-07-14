package ru.photosides.mymap;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlacesAsyncTask extends AsyncTask<URL, Void, String> {
    private Activity activity;
    private Fragment searchFragment;

    private ProgressDialog dialog;
    private PlaceIdJSONParser placeIdJsonParser;
    private JSONObject jObject;

    private URL url;

    private static final String TAG = "myLogs";

    public PlacesAsyncTask(Activity activity) {
        this.activity = activity;
    }

    protected void onPreExecute() {
        dialog = new ProgressDialog(activity);
        dialog.setTitle("Connecting...");
        dialog.setMessage("Please Wait...");
        dialog.show();
    }

    protected String doInBackground(URL... params) {
        try {
            url = params[0];

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int httpStatusCode = connection.getResponseCode();
            if (httpStatusCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                return "No Such Symbol";
            }
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                return "Error Code: " + httpStatusCode + "\nError Message: " + connection.getResponseMessage();
            }

            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String result = "";
            String oneLine = bufferedReader.readLine();

            while (oneLine != null) {
                result += oneLine + "\n";
                oneLine = bufferedReader.readLine();
            }

            return result;
        } catch (Exception ex) {
            return "Error: " + ex.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String response) {

//        Log.d(TAG, "Response " + response);
        dialog.dismiss();






        /*ParserTask parserTask = new ParserTask();
        // Start parsing the Google places in JSON format
        // Invokes the "doInBackground()" method of ParserTask
        parserTask.execute(response);
//        delegate.processFinish(response);*/

    }


}