package com.tuankhai.travelassistants.fcm;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tuank on 06/04/2017.
 */

public class SaveIDTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... strings) {
        if (strings[0] == null) {
            return null;
        } else {
            try {
                String link = "http://phimmoi.pe.hu/addtoken.php?token=" + strings[0];
                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = "";
                StringBuffer sb = new StringBuffer("");
                while ((line = in.readLine()) != null) {
                    sb.append(line);
                }
                connection.disconnect();
                Log.e("AddToken", sb.toString() + " id = " + strings[0]);
            } catch (Exception e) {
                Log.e("AddToken", e.toString());
            }
        }
        return null;
    }
}
