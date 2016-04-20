package com.waracle;

import android.test.ActivityInstrumentationTestCase2;

import com.waracle.androidtest.MainActivity;


/**
 * Created by murrayking on 20/04/2016.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity activity;
    public MainActivityTest() {
        super(MainActivity.class);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        activity = getActivity();

    }

    public void testActivityLaunched(){
        assertNotNull("MainActivity launched", activity);
    }


}