package com.example.android.MyMovie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.MyMovie.models.Movie;

public class MainActivity extends AppCompatActivity implements MainFragment.BundleCallback {

    private static final String DETAIL_FRAGMENT_TAG = "DTAG";
    public final static String MOVIE_TAG = "MOVIE";

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movieDetail) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movieDetail, new DetailFragment(), DETAIL_FRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onItemSelected(Movie movie) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(MOVIE_TAG, movie);
            DetailFragment fragment = new DetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movieDetail, fragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(MOVIE_TAG, movie);
            startActivity(intent);
        }
    }

}
