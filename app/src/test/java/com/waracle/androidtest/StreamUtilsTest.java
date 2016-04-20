package com.waracle.androidtest;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by murrayking on 20/04/2016.
 */
public class StreamUtilsTest extends TestCase {

    public void testReadUnknownFully() throws Exception {
        String testByteStream = "test data";
        InputStream testInput = new ByteArrayInputStream(testByteStream.getBytes());
        byte[] testData = StreamUtils.readUnknownFully(testInput);
        String result = new String(testData);

        Assert.assertNotNull(result);
        Assert.assertEquals(testByteStream, result);
    }
}