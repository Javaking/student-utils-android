/**
 * 
 */
package com.javaking.clanteam.studentutils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.javaking.clanteam.calendar.provider.CalendarContract.Calendars;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;

/**
 * @author scott
 *
 */
@SuppressWarnings("deprecation")
public class SettingsActivity extends SherlockPreferenceActivity implements OnSharedPreferenceChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_general);
//		addPreferencesFromResource(R.xml.pref_courses);
		addPreferencesFromResource(R.xml.pref_calendars);
		
		try {
			ListPreference calChooser = (ListPreference)findPreference("useCalendar");
			if (calChooser!=null) {
				calChooser.setEntries(getCalendarNames());
				calChooser.setEntryValues(getCalendarIds());
			}
		} catch (ClassCastException ignored) {
			
		}
	}
	
	private CharSequence[] getCalendarNames() {
		Cursor cursor = getContentResolver().query(Calendars.CONTENT_URI, new String[]{Calendars.NAME}, null, null, null);
		String[] names = new String[cursor.getCount()];
		int nameCol = cursor.getColumnIndex(Calendars.NAME);
		for (int i = 0; cursor.moveToNext(); i++) {
			names[i] = cursor.getString(nameCol);
		}
		cursor.close();
		return names;
	}

	private CharSequence[] getCalendarIds() {
		Cursor cursor = getContentResolver().query(Calendars.CONTENT_URI, new String[]{Calendars._ID}, null, null, null);
		String[] ids = new String[cursor.getCount()];
		int idCol = cursor.getColumnIndex(Calendars._ID);
		for (int i = 0; cursor.moveToNext(); i++) {
			ids[i] = cursor.getString(idCol);
		}
		cursor.close();
		return ids;
	}

	@Override
	protected void onResume() {
	    super.onResume();
	    getPreferenceScreen().getSharedPreferences()
	            .registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
	    super.onPause();
	    getPreferenceScreen().getSharedPreferences()
	            .unregisterOnSharedPreferenceChangeListener(this);
	}

	/* (non-Javadoc)
	 * @see android.content.SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(android.content.SharedPreferences, java.lang.String)
	 */
	public void onSharedPreferenceChanged(SharedPreferences prefs,
			String key) {
		String adsPreference = getResources().getString(R.string.enableAds);
		if (key.equals(adsPreference)) {
			if(!prefs.getBoolean(adsPreference, true)) {
				
				String message = "These ads are the only way for me to make money off " +
						"my apps. I won't stop you from disabling ads, but I " +
						"would prefer you don't. Money is good, you see.";
				showAlertDialog("Are you sure?",message,"disableAds","disableAds",null);
			}
		} else if (key.equals("endDate")) {
			long startTime = prefs.getLong("startDate", Calendar.getInstance().getTimeInMillis());
			long endTime = prefs.getLong("endDate", Calendar.getInstance().getTimeInMillis());
			if (endTime-startTime<=0) {
				showAlertDialog("WRONG!", "The start of your term cannot be after the end.", null, null, null);
				prefs.edit().putLong("endDate", startTime).commit();
			}
		}
		
	}

	private void showAlertDialog(String title, String message, final String onNegativeMethod, final String onCancelMethod, final String onPositiveMethod) {
		new AlertDialog.Builder(this).setTitle(title)
		.setMessage(message)
		.setNegativeButton(android.R.string.cancel,	new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				invokeMethod(onNegativeMethod);
				dialog.dismiss();
			}
		}).setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				invokeMethod(onCancelMethod);
				dialog.dismiss();
			}
		}).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				invokeMethod(onPositiveMethod);
				dialog.dismiss();
			}
		}).show();
	}

	protected void invokeMethod(String methodName) {
		if (methodName==null) return;
		try {
			getClass().getMethod(methodName, (Class[]) null).invoke(this, (Object[])null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public void disableAds() {
		CheckBoxPreference p = (CheckBoxPreference)findPreference(
				getResources().getString(R.string.enableAds));
        p.setChecked(true); 
	}

}
