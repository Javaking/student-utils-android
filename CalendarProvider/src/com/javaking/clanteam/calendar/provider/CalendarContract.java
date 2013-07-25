package com.javaking.clanteam.calendar.provider;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;

public final class CalendarContract {
	
	public static final String AUTHORITY = "com.javaking.clanteam.calendar";
	
	static final String INTERNAL_AUTHORITY = getInternalAuthority();

	private static String getInternalAuthority() {
		if (Build.VERSION.SDK_INT>=8) {
			return "com.android.calendar";
		} else {
			return "calendar";
		}
	}	
	
	public static final class Calendars implements BaseColumns {
		private static final String PATH = "/calendars";

		/**
		 * The Uri to be used for accessing all the calendars availible to the provider
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+PATH);
		
		/**
		 * This provider is really just a wrapper for android's provider.
		 */
		public static final Uri INTERNAL_URI = Uri.parse("content://"+INTERNAL_AUTHORITY+PATH);
		
		public static final String NAME = getName();

		private static String getName() {
			if (Build.VERSION.SDK_INT>=14) {
				return "name";
			} else {
				return "displayName";
			}
		}
		
        /**
         * The default location for the calendar. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String CALENDAR_LOCATION = "calendar_location";
		
		/** Is the calendar selected to be displayed?*/
		public static final String VISIBLE = "visible";
		
		/** The time zone the calendar is associated with.
        * <P>Type: TEXT</P>
        */
       public static final String CALENDAR_TIME_ZONE = "calendar_timezone";
       
       /**
        * The owner account for this calendar, based on the calendar feed.
        * This will be different from the _SYNC_ACCOUNT for delegated calendars.
        * Column name.
        * <P>Type: String</P>
        */
       public static final String OWNER_ACCOUNT = "ownerAccount";
       
       /**
        * Can the organizer modify the time zone of the event? Column name.
        * <P>Type: INTEGER (boolean)</P>
       */
       public static final String CAN_MODIFY_TIME_ZONE = "canModifyTimeZone";
       
       /**
        * Is this the primary calendar for this account. If this column is not explicitly set, the
        * provider will return 1 if {@link Calendars#ACCOUNT_NAME} is equal to
        * {@link Calendars#OWNER_ACCOUNT}.
        */
       public static final String IS_PRIMARY = "isPrimary";
	}
	
	public static final class Events implements BaseColumns {
		
		private static final String PATH = "/events";

		public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+PATH);
		
		public static final Uri INTERNAL_URI = Uri.parse("content://"+INTERNAL_AUTHORITY+PATH);
		
        /**
         * The {@link Calendars#_ID} of the calendar the event belongs to.
         * Column name.
         * <P>Type: INTEGER</P>
         */
        public static final String CALENDAR_ID = "calendar_id";

        /**
         * The title of the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String TITLE = "title";

        /**
         * The description of the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String DESCRIPTION = "description";

        /**
         * Where the event takes place. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String EVENT_LOCATION = "eventLocation";
		
		/**
		 * Both start and end time should be millis since epoch.
		 */
		public static final String START_TIME = getStartTime();
		
		private static String getStartTime() {
			if (Build.VERSION.SDK_INT>=14) {
				return "dtstart";
			} else  {
				return "startTime";
			}
		}
		
		/**
		 * Both start and end time should be millis since epoch.
		 */
		public static final String END_TIME = getEndTime();
		
		private static String getEndTime() {
			if (Build.VERSION.SDK_INT>=14) {
				return "dtend";
			} else  {
				return "endTime";
			}
		}
		
        /**
         * The duration of the event in RFC2445 format. Column name.
         * <P>Type: TEXT (duration in RFC2445 format)</P>
         */
        public static final String DURATION = "duration";
        
        /**
         * The timezone for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String EVENT_TIMEZONE = "eventTimezone";
        
        /**
         * The timezone for the end time of the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String EVENT_END_TIMEZONE = "eventEndTimezone";

        /**
         * Is the event all day (time zone independent). Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final String ALL_DAY = "allDay";
        
        /**
         * Whether the event has an alarm or not. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final String HAS_ALARM = "hasAlarm";
        
        
        /**
         * The recurrence rule for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String RRULE = "rrule";

        /**
         * The recurrence dates for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String RDATE = "rdate";

        /**
         * The recurrence exception rule for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String EXRULE = "exrule";

        /**
         * The recurrence exception dates for the event. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String EXDATE = "exdate";

        /**
         * The {@link Events#_ID} of the original recurring event for which this
         * event is an exception. Column name.
         * <P>Type: TEXT</P>
         */
        public static final String ORIGINAL_ID = "original_id";

        /**
         * The last date this event repeats on, or NULL if it never ends. Column
         * name.
         * <P>Type: INTEGER (long; millis since epoch)</P>
         */
        public static final String LAST_DATE = "lastDate";
        
        /**
         * Whether guests can modify the event. Column name.
         * <P>Type: INTEGER (boolean)</P>
         */
        public static final String GUESTS_CAN_MODIFY = "guestsCanModify";
        
        /**
         * Email of the organizer (owner) of the event. Column name.
         * <P>Type: STRING</P>
         */
        public static final String ORGANIZER = "organizer";

        /**
         * Are we the organizer of this event. If this column is not explicitly set, the provider
         * will return 1 if {@link #ORGANIZER} is equal to {@link Calendars#OWNER_ACCOUNT}.
         * Column name.
         * <P>Type: STRING</P>
         */
        public static final String IS_ORGANIZER = "isOrganizer";

        /**
         * Whether the user can invite others to the event. The
         * GUESTS_CAN_INVITE_OTHERS is a setting that applies to an arbitrary
         * guest, while CAN_INVITE_OTHERS indicates if the user can invite
         * others (either through GUESTS_CAN_INVITE_OTHERS or because the user
         * has modify access to the event). Column name.
         * <P>Type: INTEGER (boolean, readonly)</P>
         */
        public static final String CAN_INVITE_OTHERS = "canInviteOthers";
		
		/** 
		 * This is a helper class to help you generate recurrence rules.
		 * It can be used for both the rrule property and the exrule property.
		 * @author scott
		 *
		 */
		public static class RRuleBuilder {
			private Map<String,Object> mData;
			
			public RRuleBuilder() {
				mData = new HashMap<String, Object>();
			}
			
			/**
			 * @param freq Choose a frequency from the enum; the event will then
			 * repeat at that frequency
			 */
			public void setFreq(Frequencies freq) {
				mData.put("freq", freq.name());
			}

			/**
			 * @param interval How many "frequencies" go by before a repeat event
			 * occurs. For example, if you are at SECONDLY and interval is 2, 
			 * a repeat event will occur ever other second.
			 */
			public void setInterval(int interval) {
				mData.put("interval", interval);
			}

			/**
			 * @param until Repeat the event until 
			 */
			public void setUntil(Date until) {
				DateFormat dayFormat = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
				DateFormat timeFormat = new SimpleDateFormat("HHmmss",Locale.getDefault());
				mData.put("until", dayFormat.format(until)+"T"+timeFormat.format(until));
			}

			/**
			 * @param count How many times to repeat this event
			 */
			public void setCount(int count) {
				mData.put("count", count);
			} 

			/**
			 * The BYxxx rules simply specify that the event will only be
			 * created when the time period is matched. For example, "BYMONTH=1"
			 * reduces the number of recurring events from every month, to only 
			 * days in January.
			 */
			public void setBySecond(int[] bySecond) {
				mData.put("bySecond", bySecond);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setByMinute(int[] byMinute) {
				mData.put("byMinute", byMinute);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setByHour(int[] byHour) {
				mData.put("byHour", byHour);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setByDay(int[] byDay) {
				mData.put("byDay", byDay);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setByMonthDay(int[] byMonthDay) {
				mData.put("byMonthDay", byMonthDay);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setByYearDay(int[] byYearDay) {
				mData.put("byYearDay", byYearDay);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setByWeekNo(int[] byWeekNo) {
				mData.put("byWeekNo", byWeekNo);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setByMonth(int[] byMonth) {
				mData.put("byMonth", byMonth);
			}

			/**
			 * see {@link RRuleBuilder#setBySecond(int[])}
			 */
			public void setBySetPos(int[] bySetPos) {
				mData.put("bySetPos", bySetPos);
			}

			/**
			 * @param weekday Days of the week that this event can recur.
			 */
			public void setWeekday(Weekdays weekday) {
				mData.put("WKST", weekday.name());
			}

			public enum Frequencies {
				SECONDLY,
				MINUTELY,
				HOURLY,
				DAILY,
				WEEKLY,
				MONTHLY,
				YEARLY
			}
			
			public enum Weekdays {
				SU,
				MO,
				TU,
				WE,
				TH,
				FR,
				SA
			}
			
			public String buildRRule() {
				StringBuffer buffer = new StringBuffer();
				
				for (Iterator<String> iterator = mData.keySet().iterator(); iterator
						.hasNext();) {
					String key = iterator.next();
					buffer.append(key.toUpperCase(Locale.getDefault()));
					buffer.append('=');
					String string = mData.get(key).toString();
					if (string.startsWith("[")) {
						string = string.substring(1, string.length()-1).replaceAll(" ", "");
					}
					buffer.append(string);
					buffer.append(';');
				}
				return buffer.toString();
			}
		}
		
	}
	
}
