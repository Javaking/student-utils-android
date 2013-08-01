package com.javaking.clanteam.studentutils;

import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.javaking.clanteam.studentutils.courses.CourseListFragment;


public class MainActivity extends SherlockFragmentActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdManager.checkForAds(this);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentFrame,new CourseListFragment(),"courseList")
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.activity_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId())
        {
        case R.id.settings:
            startActivity(new Intent(this,SettingsActivity.class));
            break;
        }
        return super.onMenuItemSelected(featureId, item);
    }

}
