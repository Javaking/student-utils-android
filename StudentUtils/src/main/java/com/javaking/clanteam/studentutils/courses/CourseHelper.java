package com.javaking.clanteam.studentutils.courses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.javaking.clanteam.studentutils.courses.CourseData.DatePair;

import java.text.ParseException;

/**
 * Created by Scott on 7/25/13. b
 */
public class CourseHelper extends SQLiteOpenHelper implements BaseColumns {

    private static CourseHelper instance;

    public static final String TAG = "CourseHelper";
    
    private SQLiteDatabase mDb;
    private boolean mCanWrite = false;

    // Database info
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "course.db";

    // Course table info
    public static final String COURSE_TABLE_NAME = "courses";
    public static final String COLUMN_ID = _ID;
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEACHER = "teacher";
    public static final String COLUMN_ROOM = "room";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_TIMES = "times";
    public static final String COLUMN_ASSIGNMENT_IDS = "assignments";

    protected static final String COURSE_TABLE_CREATE = "CREATE TABLE "
            + COURSE_TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TITLE + " TEXT NOT NULL,"
            + COLUMN_TEACHER + " TEXT,"
            + COLUMN_ROOM + " TEXT,"
            + COLUMN_NOTES + " TEXT,"
            + COLUMN_TIMES + " TEXT NOT NULL,"
            + COLUMN_ASSIGNMENT_IDS + " TEXT);"; // unused for now. Will be added later.

    private Context mContext;

    /**
     * Returns the instance of CourseHelper if it has been initialized
     * @return The instance of CourseHelper
     * @throws InstantiationException If you attempt to get the instance before
     *          the helper has been initialized
     */
    public static CourseHelper getInstance() throws InstantiationException {
        if (instance==null) {
            // hasn't been initialized yet
            throw new InstantiationException("CourseHelper has not yet been initialized");
        }
        return instance;
    }

    public static CourseHelper getInstance(Context context) {
        if (instance==null) {
            // hasn't been initialized yet
            instance = new CourseHelper(context);
        }
        return instance;
    }

    private CourseHelper(Context context)
    {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);

        mContext = context;
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COURSE_TABLE_CREATE);
        Log.i(TAG,"Course table created.");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * <p/>
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO make this better...

        db.execSQL("DROP TABLE IF EXISTS ?", new Object[] {COURSE_TABLE_NAME});
    }

    public void addCourse(CourseData courseData) throws SQLiteException {
        if (courseData == null) throw new NullPointerException("courseData must not be null");
        ensureWriteAccessOrFail();

        // Add all data to a cv
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, courseData.getTitle());
        cv.put(COLUMN_TEACHER, courseData.getTeacher());
        cv.put(COLUMN_ROOM, courseData.getRoom());
        cv.put(COLUMN_NOTES, courseData.getNotes());
        cv.put(COLUMN_TIMES, courseData.getTimesAsString());
        // cv.put(COLUMN_ASSIGNMENT_IDS,);

        mDb.insertOrThrow(COURSE_TABLE_NAME,null,cv);

        Log.i(TAG, String.format("Successfully added \"%s\" to the database!", courseData.getTitle()));
    }

    protected void ensureWriteAccessOrFail() {
        if (!ensureWriteAccess()) throw new IllegalStateException("No writable database could be opened.");
    }

    protected boolean ensureWriteAccess() {
        if (mCanWrite) return true;
        try {
            openDatabase();
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    private void openDatabase() {
        // Try to get a writable database, but reading will have to do for
        // some circumstances.
        try {
            mDb = getWritableDatabase();
            mCanWrite = true;
        } catch (SQLiteException e) {
            mDb = getReadableDatabase();
            mCanWrite = false;
        }
    }

    /**
     * Search the database for a source with an id matching that provided in the courseData
     * Will assign all values in courseData to those found in the database. If none could be
     * found, courseData will be left untouched and will return false
     * @param courseData the course to look for
     */
    private boolean findCourseByID(CourseData courseData) {
        if (courseData == null) return false;
        CourseData parsedCourse = findCourseByID(courseData.getID());

        if (parsedCourse == null) return false;
        CourseData.copy(parsedCourse, courseData);

        return true;
    }

    private CourseData findCourseByID(int id) {
        if (id == -1) {
            // There's no id to search for..
            return null;
        }

        //select all rows that have the id coresponding to
        Cursor cursor = mDb.query(COURSE_TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_ID + "==?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor.getCount() == 0) return null;
        return parseCursor(cursor)[0]; //sqlite won't allow multiple entries with same id.

    }

    /**
     * Parse all courses accessible through the provider cursor
     * @param cursor cursor containing course data.
     * @return An array of all valid courses accessible from the cursor. If the cursor is non-null,
     * but contains no rows, a blank array will be returned
     */
    private CourseData[] parseCursor(Cursor cursor) {
        if (cursor == null) return null;
        CourseData[] courses = new CourseData[cursor.getCount()];

        // starts before the first row.
        while (cursor.moveToNext()) {
            CourseData tmp = courses[cursor.getPosition()];
            // All the column indices should be in the order we created them in.
            // Checks should be unnecessary. If it becomes a problem, will change.
            tmp.setID(cursor.getInt(0));
            tmp.setTitle(cursor.getString(1));
            tmp.setTeacher(cursor.getString(2));
            tmp.setRoom(cursor.getString(3));
            tmp.setNotes(cursor.getString(4));
            String string = cursor.getString(5);
            if (string == null) continue;

            String[] times = string.split(";");
            DatePair[] datePairs = new DatePair[times.length];
            DatePair.PairFormat pairFormat = new DatePair.PairFormat();
            for (int i = 0; i < times.length; i++) {
                String time = times[i];
                try {
                    datePairs[i] = pairFormat.parse(time);
                } catch (ParseException e) {
                    Log.e(TAG, String.format("Failed to parse \"%s\"", time),e);
                }
            }
            tmp.setTimes(datePairs);
        }

        return courses;
    }
}
