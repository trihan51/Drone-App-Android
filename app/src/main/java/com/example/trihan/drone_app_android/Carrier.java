package com.example.trihan.drone_app_android;

import android.icu.util.Output;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by trihan on 7/9/16.
 *
 * Carrier shuttles messages between the Android App and the Drone
 */
public class Carrier {

    private String mDestination;
    private Gson gson;

    public Carrier(String aDestination) {
        mDestination = aDestination;
        gson = new Gson();
    }

    public Message sendMessage(Message message) throws IOException {
        URL url = new URL(mDestination);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setDoOutput(true);
        urlConnection.setChunkedStreamingMode(0);
        urlConnection.connect();

        try {
            OutputStream output = new BufferedOutputStream(urlConnection.getOutputStream());
            writeStream(output, message);
            output.close();

            InputStream input = new BufferedInputStream(urlConnection.getInputStream());
            Message response = readStream(input);
            input.close();

            return response;
        } finally {
            urlConnection.disconnect();
        }
    }

    private void writeStream(OutputStream output, Message message) {
        try {
            String jsonString = gson.toJson(message);
            output.write(jsonString.getBytes());
            output.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private Message readStream(InputStream input) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = input.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        String jsonString = new String(out.toByteArray());
        return gson.fromJson(jsonString, Message.class);
    }
}
