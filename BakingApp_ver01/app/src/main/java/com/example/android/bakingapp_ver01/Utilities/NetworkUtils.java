package com.example.android.bakingapp_ver01.Utilities;

import android.content.ContextWrapper;
import android.content.res.Resources;
import android.net.Uri;

import com.example.android.bakingapp_ver01.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by john.osorio on 13/06/2017.
 */

public class NetworkUtils {

    //This is the base recipe list URL
    private static final String BAKINGAPP_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    public static URL buildBakingAppUrl() {
        Uri builtUri = Uri.parse(BAKINGAPP_BASE_URL).buildUpon().build();

        URL url = null;

        try {
            url = new URL (builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
