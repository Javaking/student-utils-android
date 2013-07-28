package com.javaking.clanteam.calendar;



import com.javaking.clanteam.calendar.provider.CalendarContract.Calendars;
import com.javaking.clanteam.calendar.provider.CalendarContract.Events;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;



@SuppressLint("Registered")
public class CalendarProvider extends ContentProvider {
	
	private Context mContext;
	private ContentResolver mResolver;
	
	public boolean onCreate() {
		mContext = getContext();
		mResolver = mContext.getContentResolver();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		if (Calendars.CONTENT_URI.equals(uri)) {
			return mResolver.query(Calendars.INTERNAL_URI, projection, selection, selectionArgs, sortOrder);
		} else if (Events.CONTENT_URI.equals(uri)) {
			return mResolver.query(Events.INTERNAL_URI, projection, selection, selectionArgs, sortOrder);
		}
		return null;
	}

	@Override
	public String getType(Uri uri) {
		if (Calendars.CONTENT_URI.equals(uri)) {
			return mResolver.getType(Calendars.INTERNAL_URI);
		} else if (Events.CONTENT_URI.equals(uri)) {
			return mResolver.getType(Events.INTERNAL_URI);
		}
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		if (Calendars.CONTENT_URI.equals(uri)) {
			Log.i("CalendarProvider", "Adding Calendar: "+values.getAsString(Calendars.NAME));
			return mResolver.insert(Calendars.INTERNAL_URI, values);
		} else if (Events.CONTENT_URI.equals(uri)) {
			Log.i("CalendarProvider", "Adding Event: "+values.getAsString(Events.TITLE)+" StartTime="+values.getAsLong(Events.START_TIME));
			return mResolver.insert(Events.INTERNAL_URI, values);
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		if (Calendars.CONTENT_URI.equals(uri)) {
			return mResolver.delete(Calendars.INTERNAL_URI, selection, selectionArgs);
		} else if (Events.CONTENT_URI.equals(uri)) {
			return mResolver.delete(Events.INTERNAL_URI, selection, selectionArgs);
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		if (Calendars.CONTENT_URI.equals(uri)) {
			return mResolver.update(Calendars.INTERNAL_URI, values, selection, selectionArgs);
		} else if (Events.CONTENT_URI.equals(uri)) {
			return mResolver.update(Events.INTERNAL_URI, values, selection, selectionArgs);
		}
		return 0;
	}

}
