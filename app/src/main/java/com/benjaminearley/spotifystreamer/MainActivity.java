package com.benjaminearley.spotifystreamer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = ArtistSearchFragment.class.getName();
    public boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, e.toString());
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        if (findViewById(R.id.fragment2) != null) {

            mTwoPane = true;

        }

        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new ArtistSearchFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isTwoPane() {
        return mTwoPane;
    }
}

