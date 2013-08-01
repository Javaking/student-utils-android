package com.javaking.clanteam.studentutils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.javaking.clanteam.studentutils.courses.CourseListFragment;
import com.javaking.clanteam.studentutils.courses.assignments.AssignmentsListFragment;

public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

    public AppSectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                // the course list
                return new CourseListFragment();
            case 1:
                // assignment list
                return new AssignmentsListFragment();
        }

        // at least return something to hopefully avoid the dreaded NullPointerException
        return new Fragment();
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // todo find a efficient way to make this tie to the getItem() func
        switch (position) {
            case 0:
                return "Courses";
            case 1:
                return "Assignments";
        }

        // default impl does this so it must be fine.
        return null;
    }
}
