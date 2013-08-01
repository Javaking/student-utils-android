package com.javaking.clanteam.studentutils.courses;

import android.content.Context;
import android.database.Cursor;

import com.javaking.clanteam.studentutils.SimpleCursorLoader;

public class CourseCursorLoader extends SimpleCursorLoader{

    CourseHelper mHelper;

    public CourseCursorLoader(Context context) {
        super(context);
        mHelper = CourseHelper.getInstance(context);
    }


    @Override
    public Cursor loadInBackground() {

        return null;
    }
}
