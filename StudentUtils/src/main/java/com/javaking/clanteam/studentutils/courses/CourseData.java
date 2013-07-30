package com.javaking.clanteam.studentutils.courses;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Scott on 7/25/13.
 *
 * A class to contain all the relevant data for any given course
 */
public class CourseData {

    public static class DatePair implements Cloneable {

        /**
         *	Similar to a {@link java.text.SimpleDateFormat} except that this is designed for
         *	formatting a pair of dates instead of a single date.
         *
         *  Patterns should be a valid DateFormat pattern then followed by the
         *  character 'i' then you should place whatever text you want to appear between the
         *  two dates (or nothing if you wish). Now place another 'i' to end the "interlude"
         *  section (If you wish to have an 'i' in the interlude section, preface it with
         *  a '\') Next will come another valid DateFormat pattern. Ended with an 'e'
         *  which indicates the "end" section. This will be formatted like a DateFormat,
         *  but the format must be prefaced with either a 1 or a 2 indicating which
         *  date (start or end respectively) to pull that data from.
         *
         *  Any of sections, except the first, may be omitted and they will become the default.
         *  The second date format defaults to whatever you set for the first; The default
         *  interlude is " to "; and the default ending is blank. If you explicitly
         *  do not want the second date to be there (madman) then place "NULL" where
         *  you would normally put the date format.
         *
         *  EXAMPLE:
         *  	"MM/ddi through iMM/dde 1E" might become "12/25 through 12/31 Wed"
         *  	"HH:mm" could become "12:34 to 5:55"
         *  	"YYYY-MM-ddiiNULLe" would simply be "2012-2-29"
         */
        @SuppressLint("SimpleDateFormat")
        public static class PairFormat  {

            private DateFormat mFirstFormat;
            private DateFormat mSecondFormat;
            private String mEndFromat = null;

            private String mInterlude = " to ";

            public PairFormat(String pattern) {
                applyPattern(pattern);
            }

            public PairFormat() {
                applyPattern("HH:mmi to iHH:mme 1E");
            }

            public void applyPattern(String pattern) {
                Matcher matcher = Pattern.compile("([^ie]*)(i(([^ie]|\\\\i)*)[^\\\\]i)?([^e]*)e?(.*)")
                        .matcher(pattern);
                if (!matcher.matches()) {
                    throw new IllegalArgumentException("Pattern does not fit the criteria");
                }
                mFirstFormat = new SimpleDateFormat(matcher.group(1));
                String interlude = matcher.group(2);
                interlude = interlude.substring(1,interlude.length()-1);
                if (interlude!=null) {
                    mInterlude = interlude.equals("") ?
                            null : interlude.replace("\\\\i", "i");
                }
                String secondFormat = matcher.group(5);
                if (secondFormat.equals("")) {
                    mSecondFormat = mFirstFormat;
                } else if (secondFormat.equalsIgnoreCase("NULL")) {
                    mSecondFormat = null;
                } else {
                    mSecondFormat = new SimpleDateFormat(secondFormat);
                }

                String ending = matcher.group(6);
                mEndFromat = ending.equals("") ? null : ending;
            }

            public String format(DatePair pair) {
                StringBuilder builder = new StringBuilder();
                builder.append(mFirstFormat.format(pair.getStartDate()));
                if (mInterlude!=null) {
                    builder.append(mInterlude);
                }
                if (mSecondFormat!=null) {
                    builder.append(mSecondFormat.format(pair.getEndDate()));
                }
                if (mEndFromat!=null) {
                    String endPattern = mEndFromat;
                    Matcher matcher = Pattern.compile("([12])").matcher(endPattern);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException("Found an end clause but it " +
                                "didn't specify a date reference number");
                    }
                    String index = matcher.group(1);
                    endPattern = endPattern.replace(index, "");
                    DateFormat format = new SimpleDateFormat(endPattern);
                    Date date;
                    if (index.equals("1")) {
                        date = pair.getStartDate();
                    } else {
                        date = pair.getEndDate();
                    }
                    builder.append(format.format(date));
                }
                return builder.toString();
            }

            /**
             * Functions much like the parse function of DateFormat, but uses the pattern
             * @param string The string to parse for a DatePair
             * @return the valid DatePair found in the string
             * @throws ParseException if an error occured while parsing
             */
            public DatePair parse(String string) throws ParseException {
                DatePair pair = new DatePair();
                String[] dateStrings = string.split(mInterlude);
                pair.setFormat(this);
                pair.setStartDate(mFirstFormat.parse(dateStrings[0]));

                boolean startLeniet = mSecondFormat.isLenient();
                mSecondFormat.setLenient(false);
                ParsePosition position = new ParsePosition(0);
                Date temp = mSecondFormat.parse(dateStrings[1],position);
                if (temp == null) {
                    // the error index should be the first character that's part of the
                    // end format so just back it up by 1 and continue.
                    temp = mSecondFormat.parse(dateStrings[1].substring(0, position.getErrorIndex()-1));
                }
                mSecondFormat.setLenient(startLeniet); // restore original leniency.
                pair.setEndDate(temp);
                return pair;
            }
    }

        private Date mStart;
        private Date mEnd;
        private PairFormat mFormat;

        public DatePair() {
            mFormat = new PairFormat();
        }

        public DatePair(Date start, Date end) {
            mStart = start;
            mEnd = end;
            mFormat = new PairFormat();
        }

        public void setFormat(PairFormat format) {
            mFormat = format;
        }

        public void setFormat(String pattern) {
            mFormat = new PairFormat(pattern);
        }

        /**
         * @return the mStart
         */
        public Date getStartDate() {
            return mStart;
        }

        /**
         * @param start the mStart to set
         */
        public void setStartDate(Date start) {
            this.mStart = start;
        }

        /**
         * @return the mEnd
         */
        public Date getEndDate() {
            return mEnd;
        }

        /**
         * @param end the mEnd to set
         */
        public void setEndDate(Date end) {
            this.mEnd = end;
        }

        @Override
        public String toString() {
            //todo why was that here to begin with???
//            new SimpleDateFormat().parse("",null);
            return mFormat.format(this);

        }


    }

    public CourseData() {
        // Todo what do we want here?

        mID = -1;
    }

    // Todo do I really want this exposed?
    public void setID(int id) {
        this.mID = id;
    }

    public int getID() {
        return mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTeacher() {
        return mTeacher;
    }

    public void setTeacher(String teacher) {
        this.mTeacher = teacher;
    }

    public String getRoom() {
        return mRoom;
    }

    public void setRoom(String room) {
        this.mRoom = room;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

    public DatePair[] getTimes() {
        return mTimes;
    }

    public String getTimesAsString() {
        StringBuilder builder = new StringBuilder();
        for (DatePair pair : mTimes) {
            builder.append(pair.toString());
            builder.append(';');
        }
        return builder.toString();
    }

    public void setTimes(DatePair[] times) {
        this.mTimes = times;
    }

    private int mID;
    private String mTitle;
    private String mTeacher;
    private String mRoom;
    private String mNotes;
    private DatePair[] mTimes;
    // todo Add assignment array

    public static void copy(CourseData from, CourseData to) {
        if (from ==null || to == null) return;
        to.setTimes(from.getTimes());
        to.setRoom(from.getRoom());
        to.setTeacher(from.getTeacher());
        to.setTitle(from.getTitle());
        to.setID(from.getID());
        to.setNotes(from.getNotes());
    }



}
