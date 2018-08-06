package com.duelit.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Romina on 11/17/16.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    public static final String NETWORK_AVAILABLE_ACTION = "com.ajit.singh.NetworkAvailable";
    public static final String NETWORK_SLOW_ACTION = "networkslow";
    public static final String IS_NETWORK_AVAILABLE = "isNetworkAvailable";
    private static final String TAG = "NetworkChangeReceiver";
    private static final double LIMIT_NETWORK_SECONDS = 1;

    // bandwidth in kbps
    private static int POOR_BANDWIDTH = 150;
    private static int AVERAGE_BANDWIDTH = 550;
    private static int GOOD_BANDWIDTH = 2000;
    private Context context;

    public NetworkChangeReceiver() {
    }

    public NetworkChangeReceiver(Context context) {
        this.context = context;
    }

    public void checkNetworkSpeed(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) { // connected to the internet
            final long startTime;
            final long[] endTime = new long[1];
            final long[] fileSize = new long[1];
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://www.duelit.com/favicon.png")
                    .build();

            startTime = System.currentTimeMillis();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        onLowNetworkSpeed();
                    }

                    InputStream input = response.body().byteStream();

                    try {
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] buffer = new byte[1024];

                        while (input.read(buffer) != -1) {
                            bos.write(buffer);
                        }
                        byte[] docBuffer = bos.toByteArray();
                        fileSize[0] = bos.size();

                    } finally {
                        input.close();
                    }

                    endTime[0] = System.currentTimeMillis();

                    // calculate how long it took by subtracting endtime from starttime

                    double timeTakenMills = Math.floor(endTime[0] - startTime);  // time taken in milliseconds
                    double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
                    final int kilobytePerSec = (int) Math.round(1024 / timeTakenSecs);

                    if (kilobytePerSec <= POOR_BANDWIDTH) {
                        // slow connection
                    }

                    // get the download speed by dividing the file size by time taken to download
                    double speed = fileSize[0] / timeTakenMills;

                    Log.d(TAG, "Time taken in secs: " + timeTakenSecs);
                    Log.d(TAG, "kilobyte per sec: " + kilobytePerSec);
                    Log.d(TAG, "Download Speed: " + speed);
                    //Log.d(TAG, "File size: " + fileSize[0]);

                    if (timeTakenSecs >= LIMIT_NETWORK_SECONDS) {
                        onLowNetworkSpeed();
                    }
                }
            });
        } else {
            // not connected to the internet
        }
    }

    private void onLowNetworkSpeed() {
        if (context != null) {
            Log.d("NetworkChangeReceiver", "sending slow connection");
            Intent networkStateIntent = new Intent(NETWORK_SLOW_ACTION);
            LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
        }
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        this.context = context;
        Intent networkStateIntent = new Intent(NETWORK_AVAILABLE_ACTION);
        networkStateIntent.putExtra(IS_NETWORK_AVAILABLE, ViewFinder.hasInternetConnection(context));
        LocalBroadcastManager.getInstance(context).sendBroadcast(networkStateIntent);
    }
}
