package io.guru98.telemetry;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/***
 * {@author} Guru Prasath (guru-98)
 */

public class TelemetryClient extends AsyncTask<URL, Void, String> {
        TelemetryClient() {
        }

        @Override
        protected String doInBackground(URL... arg) {
            String response;
            StringBuilder buffer = new StringBuilder();
            HttpURLConnection connection;
            InputStream inputStream;

            try {
                connection= (HttpURLConnection)arg[0].openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                inputStream = connection.getInputStream();

                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while ((line = rd.readLine()) != null) {
                    buffer.append(line);
                }
                response = buffer.toString();
                inputStream.close();
                connection.disconnect();

            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }
            return response;
        }
}
