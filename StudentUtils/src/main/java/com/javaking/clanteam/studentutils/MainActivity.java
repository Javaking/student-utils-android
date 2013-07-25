package com.javaking.clanteam.studentutils;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.google.ads.AdRequest;
import com.google.ads.AdView;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * If ads are enabled, this will tell the ad to load. If not, it will make the
     * adView disappear
     */
    public static void checkForAds(Activity context) {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        StackTraceElement element = stackTrace[1];
        System.out.println(element.toString());
        Log.i("Ads", "Checking for to see if ads are enabled");
        AdView adView = (AdView)context.findViewById(R.id.ad);
        if (adView==null)
        {
            return;
        }
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getResources().getString(R.string.enableAds), true)) {
            Log.i("Ads", "Ads are enabled, requesting a new ad.");
            AdRequest adRequest = new AdRequest();
            String[] testDevices = context.getResources().getStringArray(R.array.testDevices);
            for(String testDevice: testDevices) {
                adRequest.addTestDevice(testDevice);
            }
            adRequest.addKeyword("marching band");
            adView.loadAd(adRequest);
            adView.setVisibility(View.VISIBLE);
        } else {
            Log.i("Ads", "Ads are disabled, hiding adView.");
            adView.stopLoading();
            adView.setVisibility(View.GONE);
        }
    }
}
