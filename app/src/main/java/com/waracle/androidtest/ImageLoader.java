package com.waracle.androidtest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Riad on 20/05/2015.
 */
public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();

    // Use a WeakReference to ensure the Bitmaps can be garbage collected
    private static final Map<String, Bitmap> bitmapCache = Collections.synchronizedMap(new WeakHashMap());

    public ImageLoader() {
    }

    /**
     * Simple function for returning bitmap image from the web
     *
     * @param url image url
     * @return bitmap from image url
     */
    public Bitmap load(String url) {
        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??
        // PERFORMANCE
        //cache in memory first -> use local file system.(not implemented)
        //consider creating thumbsnails resize for device resolution.
        if (bitmapCache.containsKey(url)) {
            return bitmapCache.get(url);
        }


        try {
            Bitmap bitmap = convertToBitmap(loadImageData(url));
            bitmapCache.put(url, bitmap);
            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        throw new IllegalStateException();
    }

    private static byte[] loadImageData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        InputStream inputStream = null;
        try {
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
            }

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

            return StreamUtils.readUnknownFully(inputStream);
        } finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            connection.disconnect();
        }
    }

    private static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }


}
