package com.javaking.clanteam.studentutils.courses;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockListFragment;
import com.javaking.clanteam.studentutils.R;

/**
 * Created by Scott on 7/25/13.
 */
public class CourseListFragment extends SherlockListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String[] from = {null};
    static final int[] to = {};
    SimpleCursorAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new SimpleCursorAdapter(getActivity(),
                android.R.layout.simple_list_item_1, // todo make my own?
                null, from, to, 0);

        // start with a progress bar
        setListShown(false);

        setHasOptionsMenu(true);

        setListAdapter(mAdapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CourseCursorLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);

        // the list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        // called when the last cursor is closed. Ensure it's not being used.
        mAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Reload the courses
        getLoaderManager().restartLoader(0,null,this);
    }
}
