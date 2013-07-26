package com.javaking.clanteam.studentutils;

import android.app.Activity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

/**
 * Created by Scott on 7/25/13.
 */
public class AdManager {
    private static AdManager ourInstance = new AdManager();

    private boolean forceEnabled;
    private boolean forceDisabled;

    public static AdManager getInstance() {
        return ourInstance;
    }

    private AdManager() {
    }

    public void forceEnabled(boolean force) {
        forceEnabled = force;
    }

    public void forceDisabled(boolean force) {
        forceDisabled = force;
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
        if ((ourInstance.forceEnabled&&!ourInstance.forceDisabled) || PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
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
