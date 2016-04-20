package com.waracle.androidtest;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Riad on 20/05/2015.
 */
public class StreamUtils {
    private static final String TAG = StreamUtils.class.getSimpleName();
    private static final int BUFFER_SIZE_16K = 16 * 1024;
    // Can you see what's wrong with this???

    public static byte[] readUnknownFully(InputStream stream) throws IOException {
        // Read in stream of bytes
        //really inefficient java (using Byte refs wrappers to byte) just sink a byte array stream (designed for this).

        InputStream in = new BufferedInputStream(stream);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] dataBuffer = new byte[BUFFER_SIZE_16K];
        int size = 0;
        while ((size = in.read(dataBuffer)) != -1) {
            out.write(dataBuffer, 0, size);
        }
        byte[] bytes = out.toByteArray();

        return bytes;
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
